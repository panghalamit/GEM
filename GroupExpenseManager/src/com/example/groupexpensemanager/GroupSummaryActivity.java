package com.example.groupexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class GroupSummaryActivity extends Activity {

	public String grpName = "";
	public int grpId=0;
	public int countmembers=0;
	public final static String listofmember = "summaryActivity/listmember";
	public final static String listofbalance = "summaryActivity/listbalance";
	public final static String listofid = "summaryActivity/listid";
	public final static String stringcount = "summaryActivity/count";
    
	public String[] namearray;
    public float[] balancearray;
    public int[] idarray;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
        grpId=GroupNameToDatabaseId(grpName);
        setContentView(R.layout.activity_group_summary);
        TextView header = (TextView) findViewById(R.id.groupNametextView);
        header.setText(grpName);
        MemberListWithBalance();
        fillEntryInTable();
    }
     
    @Override
    public void onRestart() {
    	super.onRestart();
    	MemberListWithBalance();
    	correctEntryInTable();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group_summary, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    
    public void MemberListWithBalance(){
    	Log.i("sameer", "hello1");
    	String gdName="Database_"+grpId;
    	SQLiteDatabase groupDb=null;
    	countmembers=0;
        namearray = new String[10];
        idarray = new int[10];
        balancearray = new float[10];
        Log.i("sameer", "hello2");
    	try{
    		groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
	        Cursor mquery = groupDb.rawQuery("SELECT * FROM " + MainActivity.MemberTable+";",null);
	        	        
	        mquery.moveToFirst();
		    do{
		    	idarray[countmembers] = mquery.getInt(0);
		    	namearray[countmembers] = mquery.getString(1);
		    	balancearray[countmembers] = mquery.getFloat(2);
		    	countmembers++;
				}while(mquery.moveToNext());
	        
	        
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(groupDb!=null)
        		groupDb.close();
        }
    	Log.i("sameer", "hello3");
    }
    
    public void fillEntryInTable(){
    	TableLayout tl = (TableLayout)findViewById(R.id.groupSummaryTableLayout);
    	for(int j=0;j<countmembers;j++){
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			TextView v1= new TextView(this);
			v1.setText(namearray[j]);
			TextView v2= new TextView(this);
			TextView v3= new TextView(this);
			float a = balancearray[j];
			if (a<0) {
				v2.setText(String.valueOf(-a));
				v3.setText(null);
			}
			else {
				v3.setText(String.valueOf(a));
				v2.setText(null);
			}			
	        v1.setTextColor(Color.parseColor("#FFFFFF"));
	        v2.setTextColor(Color.parseColor("#FFFFFF"));
	        v3.setTextColor(Color.parseColor("#FFFFFF"));
	        /*LayoutParams l1 = new LayoutParams(
	                         LayoutParams.MATCH_PARENT,
	                         LayoutParams.MATCH_PARENT,
	                         1f);
	        LayoutParams l2 = new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.MATCH_PARENT,
	                1f);
	        LayoutParams l3 = new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.MATCH_PARENT,
	                1f);
	       
	         v1.setLayoutParams(l1);
	         v2.setLayoutParams(l2);
	         v3.setLayoutParams(l3);*/
	         tr.addView(v1);
	         tr.addView(v2);
	         tr.addView(v3);
	         tl.addView(tr);
    	}
    }
    
    public void correctEntryInTable(){
    	Log.i("sameer", "hello4");
    	TableLayout table = (TableLayout) findViewById(R.id.groupSummaryTableLayout);
		for (int k=0; k<countmembers; k++) {
			Log.i("sameer", String.valueOf(k));
			TableRow tr2 = (TableRow)table.getChildAt(k+1);
			TextView v1 = (TextView) (tr2.getChildAt(0));
			TextView v2 = (TextView) (tr2.getChildAt(1));
			TextView v3 = (TextView) (tr2.getChildAt(2));
			v1.setText(namearray[k]);
			float a = balancearray[k];
			if (a<0) {
				v2.setText(String.valueOf(-a));
				v3.setText(null);
			}
			else {
				v3.setText(String.valueOf(a));
				v2.setText(null);
			}			
		}
		Log.i("sameer", "hello5");
    }
    
    public void cashTransfer(View v) {
    	Intent intent = new Intent(this, CashTransferActivity.class);
    	intent.putExtra(GroupsActivity.GROUP_NAME, grpName);
    	intent.putExtra(GroupsActivity.GROUP_ID, grpId);
    	startActivity(intent);
    }
    
    public void showSolution(View v){
    	Intent intent = new Intent(this, PossibleSolution.class);
    	intent.putExtra(listofid, idarray);
    	intent.putExtra(listofmember, namearray);
    	intent.putExtra(listofbalance, balancearray);
    	intent.putExtra(stringcount, countmembers);
    	startActivity(intent);
    }
    
    public void showHistory(View v){
    	
    }
    
    public void nullify(View v){
    	SQLiteDatabase groupDb=null;
    	String database="Database_"+grpId;
    	try{
	        groupDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
	        groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = '0';");
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(groupDb!=null)
        		groupDb.close();
        }
    	this.onRestart();
    }
    
    public void editGroup(View v){
    	
    }
}
