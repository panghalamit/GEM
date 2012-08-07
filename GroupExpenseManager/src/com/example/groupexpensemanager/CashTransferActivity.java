package com.example.groupexpensemanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@TargetApi(11)
public class CashTransferActivity extends Activity {
	public String grpName = "";
	public int grpid = 0;
	private Spinner spin1, spin2;
	private Button btnDone;
	private List<String> list = new ArrayList<String>();

	public final static String GROUP_NAME = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_transfer);

		Intent  intent = getIntent();
		grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
		grpid = intent.getIntExtra(GroupsActivity.GROUP_ID,0);
		MemberList();

		addItemsOnSpinner1();
		addItemsOnSpinner2();
		addListenerOnButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_cash_transfer, menu);
		return true;
	}

	public void MemberList(){
		String gdName="Database_"+grpid;
		SQLiteDatabase groupDb=null;

		try{
			groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
			Cursor mquery = groupDb.rawQuery("SELECT Name FROM " + MainActivity.MemberTable+";",null);

			mquery.moveToFirst();
			do{
				list.add(mquery.getString(0));
			}while(mquery.moveToNext());	        
		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(groupDb!=null)
				groupDb.close();
		}
	}

	public int MemberNameToId(String member){
		int memberId=0;
		String database="Database_"+grpid;
		SQLiteDatabase commonDb=null;
		try{
			commonDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
			Cursor idquery = commonDb.rawQuery("SELECT ID FROM " + MainActivity.MemberTable +" WHERE Name = '"+member+"';", null);
			idquery.moveToFirst();
			memberId=idquery.getInt(0);
		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(commonDb!=null)
				commonDb.close();
		}
		return memberId;
	}

	public void CashTransfer(String fromM, String toM, float amount){

		int fromMember=MemberNameToId(fromM);
		int toMember=MemberNameToId(toM);
		SQLiteDatabase groupDb=null;
		String database="Database_"+grpid;
		//int ID=1;
		try{
			groupDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
			/*Cursor count = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.CashTable , null);
	        if(count.getCount()>0){
	        	count.moveToFirst();
	        	ID=count.getInt(0)+1;
	        }
	        groupDb.execSQL("INSERT INTO " + MainActivity.CashTable + " ( ID, FromMemberId, ToMemberId, Amount ) VALUES ( '" + ID+"', '"+fromMember+ "', '"+toMember+"', '"+amount+"' );" );
			 */
			groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance+'"+amount+"' WHERE ID = '"+fromMember+"';");
			groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance-'"+amount+"' WHERE ID = '"+toMember+"';");
		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(groupDb!=null)
				groupDb.close();
		}
	}

	public void addItemsOnSpinner1() {

		spin1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin1.setAdapter(dataAdapter);
	}

	public void addItemsOnSpinner2() {

		spin2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin2.setAdapter(dataAdapter);
	}

	public void addListenerOnButton() {
		spin1 = (Spinner) findViewById(R.id.spinner1);
		spin2 = (Spinner) findViewById(R.id.spinner2);
		btnDone = (Button) findViewById(R.id.button1);
		btnDone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				transferDone();
			}
		});
	}
	
	public void transferDone() {
		String fM = String.valueOf(spin1.getSelectedItem());
		String tM = String.valueOf(spin2.getSelectedItem());

		EditText editText;
		editText = (EditText) findViewById(R.id.amountText);
		float a = Float.valueOf(editText.getText().toString());

		CashTransfer(fM, tM, a);
		this.finish();	
	}
}
