package com.example.groupexpensemanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private String[] namearray =null;
	private List<String> list = new ArrayList<String>();
	private List<boolean[]> checkedItems = null;
	private boolean[] tempCheckedItems = null;
	private CharSequence[] items;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent  intent = getIntent();
		grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
		grpid = intent.getIntExtra(GroupsActivity.GROUP_ID,0);
		namearray = intent.getStringArrayExtra(GroupSummaryActivity.listofmember);
		String new_title= grpName+" - "+String.valueOf(this.getTitle());
		this.setTitle(new_title);
		setContentView(R.layout.activity_add_event);
		checkedItems = new ArrayList<boolean[]>();
		
		MemberList();
		
		addMember1(null);
		addMember2(null);
		EditText event = (EditText) findViewById(R.id.AddEventEventName);
		event.requestFocus();
		getWindow().setSoftInputMode(
				   WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_event, menu);
		return true;
	}

	public void MemberList(){
	
		items = new CharSequence [namearray.length];
		for (int j=0; j<namearray.length; j++) {
			list.add(namearray[j]);
			items[j] = namearray[j];
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

	private void addScroll1(){
		new Handler().postDelayed(new Runnable() {            
			public void run() {
				Button b = (Button)findViewById(R.id.addbuttonAddevent1);
				ScrollView sv = (ScrollView)findViewById(R.id.addEventScroller);
				sv.scrollBy(0, b.getHeight());
			}
		},0);
	}

	public void addMember2(View v) {
		tempCheckedItems = new boolean[namearray.length];
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

		Button shareButton = new Button(this);
		shareButton.setText("Share");
		shareButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		shareButton.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
				
			
			}
		});
		tr.addView(shareButton);
		tr.requestFocus();

		tl.addView(tr,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));	
		addScroll2();
	}
	/*public Dialog popUpCheckBox() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Consumed By");
		builder.setMultiChoiceItems(items, tempCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
			
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		AlertDialog alert = builder.create();
	
	}*/
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case 0:
	            return new AlertDialog.Builder(this)
	            .setTitle("Dialog with simple text")
	            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

	                public void onClick(DialogInterface dialog, int which) {
	                    for (int i = 0; i < items.length; i++) {
	                    if (tempCheckedItems[i]) {
	                        Toast.makeText(getBaseContext(), items[i] + " checked!", Toast.LENGTH_LONG).show();
	                    }
	                }
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	                public void onClick(DialogInterface dialog, int which) {
	                    Toast.makeText(getBaseContext(), "Cancel clicked!", Toast.LENGTH_LONG).show();
	                }
	            })
	            .setMultiChoiceItems(items, tempCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {

	                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	                    Toast.makeText(getBaseContext(), items[which] + (isChecked ? "checked!" : "unchecked!"), Toast.LENGTH_SHORT).show();
	                }
	            })
	            .create();
	        }

	        return null;
	    }


	
	private void addScroll2(){
		new Handler().postDelayed(new Runnable() {            
			public void run() {
				Button b = (Button)findViewById(R.id.donebuttonAddevent);
				ScrollView sv = (ScrollView)findViewById(R.id.addEventScroller);
				sv.scrollTo(0, b.getBottom());
			}
		},0);
	}

	public float sumArray(float[] arr){
		float sum=0;
		for(int j=0;j<arr.length;j++){
			sum+=arr[j];
		}
		return sum;
	}

	public void addEvent(String eventName, float[] amountPaid, int[] paidMembers, float[] amountConsumed, boolean[][] whoConsumed){
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
				//groupDb.execSQL("INSERT INTO "+ MainActivity.TransTable + " ( ID, MemberId, Amount, EventId ) VALUES ( '" + ID2+"', '"+mId+"', '"+amountPaid[j]+"', '"+ID1+"' );" );
				memberBalance[paidMembers[j]]+=amountPaid[j];
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

	public void doneAddEvent(View v){
		EditText event = (EditText) findViewById(R.id.AddEventEventName);
		String eventname = event.getText().toString();
		TableLayout t1 = (TableLayout) findViewById(R.id.AddEventTableLayout1);
		float[] amountpaid = new float[t1.getChildCount()];
		int[] paidmembers = new int[t1.getChildCount()];
		for (int k=0; k<t1.getChildCount(); k++) {
			TableRow tr = (TableRow) t1.getChildAt(k);
			EditText et = (EditText) tr.getChildAt(1);
			Spinner sp = (Spinner) tr.getChildAt(0); 
			String amt = et.getText().toString();
			if(amt.equals("")){
				amountpaid[k]=0;
			}
			else{
				amountpaid[k]=Float.valueOf(amt);
			}
			paidmembers[k]=sp.getSelectedItemPosition();
			/*if (amt.equals("")){
				Toast n = Toast.makeText(AddEventActivity.this,"Error! Cannot leave Amount empty", Toast.LENGTH_SHORT);
				n.setGravity(Gravity.CENTER_VERTICAL,0,0);
				n.show();
				return;
			}*/
		}
		TableLayout t2 = (TableLayout) findViewById(R.id.AddEventTableLayout2);
		float[] amountconsumed = new float[t2.getChildCount()];
		for (int k=0; k<t2.getChildCount(); k++) {
			TableRow tr = (TableRow) t2.getChildAt(k);
			EditText et = (EditText) tr.getChildAt(0);
			String amt = et.getText().toString();
			if(amt.equals("")){
				amountconsumed[k]=0;
			}
			else{
				amountconsumed[k]=Float.valueOf(amt);
			}
		}
	}

}
