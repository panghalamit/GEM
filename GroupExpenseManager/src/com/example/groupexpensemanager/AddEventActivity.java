package com.example.groupexpensemanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.text.InputType;

public class AddEventActivity extends Activity {
	public String gpName = "";
	private Spinner spin;
	private int IdNum = 0;
	private List<String> list = new ArrayList<String>();
	public final static String GROUP_NAME = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        
        Intent  intent = getIntent();
    	gpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);

    	MemberList(gpName);
    	spin = (Spinner) findViewById(R.id.memberSpinner);
    	
    	addItemsOnSpinner(spin);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_event, menu);
        return true;
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
    public void addItemsOnSpinner(Spinner spin1) {
     	 
    	
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin1.setAdapter(dataAdapter);
      }
    
    public void addMember(View v) {
    	  /* Find Tablelayout defined in main.xml */
        TableLayout tl = (TableLayout)findViewById(R.id.tableLayout1);
        tl.setStretchAllColumns(true);
        	 
             /* Create a new row to be added. */
             TableRow tr = new TableRow(v.getContext());
        
             //tr.setStyle(R.style.eventMemberStyle);
             tr.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                  /* Create a Button to be the row-content. */
                  Spinner sp = new Spinner(this);
                  sp.setPadding(10, 0, 15, 0);
                  sp.setLayoutParams(new LayoutParams(
                		  	LayoutParams.WRAP_CONTENT));
                  tr.addView(sp);
                  addItemsOnSpinner(sp);
                  EditText et = new EditText(this);
                  et.setPadding(15, 0, 10, 0);
                  et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                  
                  et.setLayoutParams(new LayoutParams(
              		  	LayoutParams.WRAP_CONTENT));
                  tr.addView(et);
                 /* Add row to TableLayout. */
         tl.addView(tr,new TableLayout.LayoutParams(
                          LayoutParams.FILL_PARENT,
                          LayoutParams.WRAP_CONTENT));	
        
    	
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
    
    
    
    public void addEvent(String groupName, String eventName, float[] amountPaid, String[] paidMembers, float[] amountConsumed, boolean[][] whoConsumed){
    	int groupId=GroupNameToDatabaseId(groupName);
    	SQLiteDatabase groupDb=null;
    	String database="Database_"+groupId;
    	int ID1=1;
    	int ID2=1;
    	try{
	        groupDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
	        Cursor count = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.CashTable , null);
	        if(count.getCount()>0){
	        	count.moveToFirst();
	        	ID1=count.getInt(0)+1;
	        }
	        groupDb.execSQL("INSERT INTO " + MainActivity.EventTable + " ( ID, Name ) VALUES ( '" + ID1+"', '"+eventName+"' );" );
	        float[] memberBalance;
	        memberBalance=new float[whoConsumed[0].length];
	        for(int j=0;j<memberBalance.length;j++){
	        	memberBalance[j]=0;
	        }
	        Cursor count2 = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.TransTable , null);
	        if(count2.getCount()>0){
	        	count2.moveToFirst();
	        	ID2=count2.getInt(0)+1;
	        }
	        for(int j=0;j<amountPaid.length;j++){
	        	int mId=MemberNameToId(groupId,paidMembers[j]);
	        	groupDb.execSQL("INSERT INTO "+ MainActivity.TransTable + " ( ID, MemberId, Amount, EventId ) VALUES ( '" + ID2+"', '"+mId+"', '"+amountPaid[j]+"', '"+ID1+"' );" );
	        	memberBalance[mId-1]+=amountPaid[j];
	        	ID2++;	        	
	        }
	        int share=0;
	        float eachshare=0;
	        for(int j=0;j<amountConsumed.length;j++){
	        	share=0;
	        	for(int i=0;i<whoConsumed[j].length;i++){
	        		if(whoConsumed[j][i]){
	        			share++;
	        		}
	        	}
	        	eachshare=(-1)*amountConsumed[j]/share;
	        	for(int i=0;i<whoConsumed[j].length;i++){
	        		if(whoConsumed[j][i]){
	        			groupDb.execSQL("INSERT INTO "+ MainActivity.TransTable + " ( ID, MemberId, Amount, EventId ) VALUES ( '" + ID2+"', '"+(i+1)+"', '"+eachshare+"', '"+ID1+"' );" );
	        			memberBalance[i]+=eachshare;
	        		}
	        	}
	        	for(int i=0;i<memberBalance.length;i++){
	        		groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance+'"+memberBalance[i]+"' WHERE ID = '"+(i+1)+"';");
		        }
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
