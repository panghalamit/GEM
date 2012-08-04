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

    	MemberList(grpName);
    	
    	addItemsOnSpinner1();
    	addItemsOnSpinner2();
    	addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cash_transfer, menu);
        return true;
    }
    
    public int GroupNameToDatabaseId(String GroupName){
    	int databaseId=0;
    	SQLiteDatabase commonDb=null;
    	try{
    		commonDb = this.openOrCreateDatabase(MainActivity.CommonDatabase, MODE_PRIVATE, null);
	        Cursor idquery = commonDb.rawQuery("SELECT ID FROM " + MainActivity.GroupTable +" WHERE Name = '"+GroupName+"';", null);
	        idquery.moveToFirst();
        	databaseId = idquery.getInt(0);;
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(commonDb!=null)
        		commonDb.close();
        }
    	return databaseId;
    }
    
   public void MemberList(String GroupName){
    	int databaseId=GroupNameToDatabaseId(GroupName);
    	String gdName="Database_"+databaseId;
    	SQLiteDatabase groupDb=null;
    	int count=0;
        
        String[] name = new String[5];
        
    	try{
	        groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
	        Cursor mquery = groupDb.rawQuery("SELECT Name FROM " + MainActivity.MemberTable+";",null);
	        //int count=0;
	        //count = mquery.getCount();
	        
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
    
    public int MemberNameToId(int GroupID,String member){
    	int memberId=0;
    	String database="Database_"+GroupID;
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
    
    public void CashTransfer(String groupName, String fromM, String toM, float amount){
    	int GroupId=GroupNameToDatabaseId(groupName);
    	int fromMember=MemberNameToId(GroupId,fromM);
    	int toMember=MemberNameToId(GroupId,toM);
    	SQLiteDatabase groupDb=null;
    	String database="Database_"+GroupId;
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
		
		CashTransfer(grpName, fM, tM, a);
		this.finish();	
    }
    
}
