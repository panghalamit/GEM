package com.example.groupexpensemanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class AddEventActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_event, menu);
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
    
    public float sumArray(float[] arr){
    	float sum=0;
    	for(int j=0;j<arr.length;j++){
    		sum+=arr[j];
    	}
    	return sum;
    }
    
    public void addEvent(String groupName, String eventName, float[] amountPaid, String[] paidMembers, float[] amountConsumed, boolean[][] whoConsumed){
    	if(sumArray(amountPaid)!=sumArray(amountConsumed)){
    		//TODO error pop-up
    		return;
    	}
    	int groupId=GroupNameToDatabaseId(groupName);
    	SQLiteDatabase groupDb=null;
    	String database="Database_"+groupId;
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
	        	int mId=MemberNameToId(groupId,paidMembers[j]);
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
