package com.example.groupexpensemanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

public class AddEventActivity extends Activity {
	public String grpName = "";
	public int grpid = 0;
	private List<String> list = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		Intent  intent = getIntent();
		grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
		grpid = intent.getIntExtra(GroupsActivity.GROUP_ID,0);
		MemberList();
		Spinner spin1 = (Spinner) findViewById(R.id.addEventSpinner1);
		Spinner spin2 = (Spinner) findViewById(R.id.addEventSpinner2);
		addItemsOnSpinner(spin1);
		addItemsOnSpinner(spin2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_event, menu);
		return true;
	}

	public void MemberList(){
		String gdName="Database_"+grpid;
		SQLiteDatabase groupDb=null;
		int count=0;

		String[] name = new String[5];

		try{
			groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
			Cursor mquery = groupDb.rawQuery("SELECT Name FROM " + MainActivity.MemberTable+";",null);

			mquery.moveToFirst();
			do{
				name[count] = mquery.getString(0);
				count++;
			}while(mquery.moveToNext());


		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(groupDb!=null)
				groupDb.close();
		}
		for (int j=0; j<count; j++) {
			list.add(name[j]);
		}		
	}

	public void addItemsOnSpinner(Spinner spin1) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin1.setAdapter(dataAdapter);
	}

	public void addMember1(View v) {
		TableLayout tl = (TableLayout)findViewById(R.id.AddEventTableLayout1);
		tl.setStretchAllColumns(true);
		TableRow tr = new TableRow(this);

		tr.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		Spinner sp = new Spinner(this);
		sp.setPadding(10, 0, 15, 0);
		sp.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT));
		addItemsOnSpinner(sp);
		tr.addView(sp);

		EditText et = new EditText(this);
		et.setPadding(15, 0, 10, 0);
		et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		et.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT));
		tr.addView(et);
		tr.requestFocus();
		
		tl.addView(tr,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		addScroll1();
	}
	
	private final void addScroll1(){
        new Handler().postDelayed(new Runnable() {            
            public void run() {
                ScrollView sv = (ScrollView)findViewById(R.id.AddEventScroller1);
                sv.scrollTo(0, sv.getBottom());
            }
        },0);
    }
	
	public void addMember2(View v) {
		TableLayout tl = (TableLayout)findViewById(R.id.AddEventTableLayout2);
		tl.setStretchAllColumns(true);
		TableRow tr = new TableRow(this);

		tr.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		EditText et = new EditText(this);
		et.setPadding(15, 0, 10, 0);
		et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		et.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT));
		tr.addView(et);

		Spinner sp = new Spinner(this);
		sp.setPadding(10, 0, 15, 0);
		sp.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT));
		addItemsOnSpinner(sp);
		tr.addView(sp);
		tr.requestFocus();
		
		tl.addView(tr,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));	
		addScroll2();
	}
	private final void addScroll2(){
        new Handler().postDelayed(new Runnable() {            
            public void run() {
                ScrollView sv = (ScrollView)findViewById(R.id.AddEventScroller2);
                sv.scrollTo(0, sv.getBottom());
            }
        },0);
    }
	public void doneAddEvent(View v){

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

	public float sumArray(float[] arr){
		float sum=0;
		for(int j=0;j<arr.length;j++){
			sum+=arr[j];
		}
		return sum;
	}

	public void addEvent(String eventName, float[] amountPaid, String[] paidMembers, float[] amountConsumed, boolean[][] whoConsumed){
		if(sumArray(amountPaid)!=sumArray(amountConsumed)){
			Toast n = Toast.makeText(AddEventActivity.this,"Error! Total paid amount != Total Amount Consumed", Toast.LENGTH_SHORT);
			n.setGravity(Gravity.CENTER_VERTICAL,0,0);
			n.show();
			return;
		}
		SQLiteDatabase groupDb=null;
		String database="Database_"+grpid;
		//int ID1=1;
		//int ID2=1;
		try{
			groupDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
			/*Cursor count = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.CashTable , null);
	        if(count.getCount()>0){
	        	count.moveToFirst();
	        	ID1=count.getInt(0)+1;
	        }
	        groupDb.execSQL("INSERT INTO " + MainActivity.EventTable + " ( ID, Name ) VALUES ( '" + ID1+"', '"+eventName+"' );" );
			 */
			float[] memberBalance;
			memberBalance=new float[whoConsumed[0].length];
			for(int j=0;j<memberBalance.length;j++){
				memberBalance[j]=0;
			}
			/*Cursor count2 = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.TransTable , null);
	        if(count2.getCount()>0){
	        	count2.moveToFirst();
	        	ID2=count2.getInt(0)+1;
	        }*/
			for(int j=0;j<amountPaid.length;j++){
				int mId=MemberNameToId(paidMembers[j]);
				//groupDb.execSQL("INSERT INTO "+ MainActivity.TransTable + " ( ID, MemberId, Amount, EventId ) VALUES ( '" + ID2+"', '"+mId+"', '"+amountPaid[j]+"', '"+ID1+"' );" );
				memberBalance[mId-1]+=amountPaid[j];
				//ID2++;	        	
			}
			int share;
			float eachshare;
			for(int j=0;j<amountConsumed.length;j++){
				share=0;
				eachshare=0;
				for(int i=0;i<whoConsumed[j].length;i++){
					if(whoConsumed[j][i]){
						share++;
					}
				}
				eachshare=(-1)*amountConsumed[j]/share;
				for(int i=0;i<whoConsumed[j].length;i++){
					if(whoConsumed[j][i]){
						//groupDb.execSQL("INSERT INTO "+ MainActivity.TransTable + " ( ID, MemberId, Amount, EventId ) VALUES ( '" + ID2+"', '"+(i+1)+"', '"+eachshare+"', '"+ID1+"' );" );
						memberBalance[i]+=eachshare;
					}
				}
			}
			for(int i=0;i<memberBalance.length;i++){
				groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance+'"+memberBalance[i]+"' WHERE ID = '"+(i+1)+"';");
			}	        
		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(groupDb!=null)
				groupDb.close();
		}
	}
}
