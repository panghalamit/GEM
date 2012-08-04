package com.example.groupexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class GroupSummaryActivity extends Activity {

	public String grpName = "";
	public int grpId=0;
	public int countmembers=0;
	public final static String listofmember = "summaryActivity/listmember";
	public final static String listofbalance = "summaryActivity/listbalance";
	public final static String listofid = "summaryActivity/listid";
	public final static String stringcount = "summaryActivity/count";
    
	public String[] namearray = new String[5];
    public float[] balancearray = new float[5];
    public int[] idarray = new int[5];
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
        grpId=GroupNameToDatabaseId(grpName);
        setContentView(R.layout.activity_group_summary);
        TextView header = (TextView) findViewById(R.id.textView1);
        header.setText(grpName);
    }
    
    @Override
	public void onStart(){
		super.onStart();
		MemberListWithBalance();
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
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
    	String gdName="Database_"+grpId;
    	SQLiteDatabase groupDb=null;
    	countmembers=0;
        
        namearray = new String[5];
        balancearray = new float[5];
        
    	try{
	        groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
	        Cursor mquery = groupDb.rawQuery("SELECT * FROM " + MainActivity.MemberTable+";",null);
	        //int count=0;
	        //count = mquery.getCount();
	        
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
		
		for(int j=0;j<countmembers;j++){
			TextView n;
			TextView b;
			if (j==0) { n = (TextView) findViewById(R.id.nameText1); b = (TextView) findViewById(R.id.balanceText1); }
			else if (j==1) { n = (TextView) findViewById(R.id.nameText2); b = (TextView) findViewById(R.id.balanceText2); }
			else if (j==2) { n = (TextView) findViewById(R.id.nameText3); b = (TextView) findViewById(R.id.balanceText3); }
			else if (j==3) { n = (TextView) findViewById(R.id.nameText4); b = (TextView) findViewById(R.id.balanceText4); }
			else { n = (TextView) findViewById(R.id.nameText5); b = (TextView) findViewById(R.id.balanceText5); }
			n.setText(namearray[j]);
			b.setText(String.valueOf(balancearray[j]));			
		}
    	for (int j=countmembers; j<5; j++) {
    		View nameV;
    		View balanceV;
			if (j==0) { nameV = findViewById(R.id.nameText1); balanceV = findViewById(R.id.balanceText1); }
			else if (j==1) { nameV = findViewById(R.id.nameText2); balanceV = findViewById(R.id.balanceText2); }
			else if (j==2) { nameV = findViewById(R.id.nameText3); balanceV = findViewById(R.id.balanceText3); }
			else if (j==3) { nameV = findViewById(R.id.nameText4); balanceV = findViewById(R.id.balanceText4); }
			else { nameV = findViewById(R.id.nameText5); balanceV = findViewById(R.id.balanceText5); }
			nameV.setVisibility(View.GONE);
			balanceV.setVisibility(View.GONE);
    	}
    }
    
    public void cashTransfer(View v) {
    	Intent intent = new Intent(this, CashTransferActivity.class);
    	intent.putExtra(GroupsActivity.GROUP_NAME, grpName);
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
    }
    
    public void editGroup(View v){
    	
    }
}
