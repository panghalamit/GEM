package com.example.groupexpensemanager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(11)
public class NewGroupActivity extends Activity {

	public int numberMembers = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_group, menu);
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

	public void addMember(View v) {
		numberMembers++;
		TableLayout tl = (TableLayout)findViewById(R.id.newGrouptableLayout);
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT)
		);
		final float scale = tl.getContext().getResources().getDisplayMetrics().density;
		int ten = (int) (10 * scale + 0.5f);
		int five = (int) (5 * scale + 0.5f);
		tr.setPadding(0, five, 0, ten);
		tr.setId(numberMembers+100);
		//Add a TextView
		TextView tv = new TextView(this);
		tv.setText("Member");
		tv.setTextColor(Color.parseColor("#FFFFFF"));
		LayoutParams l1 = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
				0.01f);
		l1.setMargins(0, ten, ten, 0);
		tv.setLayoutParams(l1);
		tr.addView(tv);
		
		//Add a Text Field
		EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		LayoutParams l2 = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
				8f);
		l2.setMargins(0, ten, ten, 0);
		et.setLayoutParams(l2);
		tr.addView(et);
		
		//Add the minus button
		ImageButton ib = new ImageButton(this);
		ib.setBackgroundResource(R.drawable.minus_back);
		ib.setId(numberMembers+200);
		int pixels = (int) (50 * scale + 0.5f);
		LayoutParams l3 = new LayoutParams(pixels, pixels, 0.1f);
		ib.setLayoutParams(l3);
		ib.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				removeMember(v);
			}
		});
		tr.addView(ib);
		tr.requestFocus();
		tl.addView(tr,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		focusScroll();
	}
	
	private void focusScroll(){
		/*final ScrollView sv = (ScrollView)findViewById(R.id.NewGroupScroller);
		final Button b = (Button)findViewById(R.id.doneButtonNewGroup);
		sv.post(new Runnable() {
			public void run() {
				sv.scrollTo(0,b.getBottom());
			}
		});*/
		new Handler().postDelayed(new Runnable() {            
            public void run() {
                ScrollView sv = (ScrollView)findViewById(R.id.NewGroupScroller);
                sv.scrollTo(0, sv.getBottom());
            }
        },0);
    }

	public void removeMember(View v) {
		TableLayout table = (TableLayout) findViewById(R.id.newGrouptableLayout);
		TableRow tr = (TableRow)findViewById(v.getId()-100);
		int index = table.indexOfChild(tr);
		table.removeView(tr);
		TableRow tr2 = (TableRow)table.getChildAt(index-1);
		EditText et = (EditText)tr2.getChildAt(1);
		et.requestFocus();
		numberMembers--;
	}

	public boolean isMemberof(String s, String[] sarray){
		for(int j=0;j<numberMembers;j++){
			if(s.equals(sarray[j])){
				return true;
			}
		}
		return false;
	}

	public void done(View v) {
		EditText editText = (EditText) findViewById(R.id.grpText);
		String message = editText.getText().toString();
		if(message.equals("")){
			Toast n = Toast.makeText(NewGroupActivity.this,"Error! Cannot leave the Group Name empty", Toast.LENGTH_SHORT);
			n.setGravity(Gravity.CENTER_VERTICAL,0,0);
			n.show();
			return;
		}
		String[] members = new String[numberMembers];
		for(int j=0;j<numberMembers;j++){
			members[j]="";
		}
		TableLayout table = (TableLayout) findViewById(R.id.newGrouptableLayout);
		for (int k=0; k<numberMembers; k++) {
			EditText editTextMember;
			editTextMember = (EditText) (((ViewGroup) table.getChildAt(k+1)).getChildAt(1));
			String temp = editTextMember.getText().toString();
			if (temp.equals("")){
				Toast n = Toast.makeText(NewGroupActivity.this,"Error! Cannot leave Member Name empty", Toast.LENGTH_SHORT);
				n.setGravity(Gravity.CENTER_VERTICAL,0,0);
				n.show();
				return;
			}
			else if(isMemberof(temp,members)){
				Toast n = Toast.makeText(NewGroupActivity.this,"Error! Member "+temp+" already there", Toast.LENGTH_SHORT);
				n.setGravity(Gravity.CENTER_VERTICAL,0,0);
				n.show();
				return;
			}
			else {
				members[k] = temp;
			}
		}
		insertToDatabase(message, members);
	}

	public void insertToDatabase(String groupName, String[] members) {
		SQLiteDatabase myDB=null;
		String TableName=MainActivity.GroupTable;
		String CommonDatabase=MainActivity.CommonDatabase;
		int ID=1;
		int newID=1;
		try {
			myDB = this.openOrCreateDatabase(CommonDatabase, MODE_PRIVATE, null);
			Cursor isPresent=myDB.rawQuery("SELECT ID FROM " + TableName + " WHERE Name = '"+groupName+"';", null);
			if(isPresent.getCount()>0){
				Toast n = Toast.makeText(NewGroupActivity.this,"Error! Group "+groupName+" already exists", Toast.LENGTH_SHORT);
				n.setGravity(Gravity.CENTER_VERTICAL,0,0);
				n.show();
				return;
			}
			Cursor count = myDB.rawQuery("SELECT ID FROM " + TableName , null);
			if(count.moveToFirst()){
				do{
					ID=count.getInt(0);
					if(ID!=newID){
						break;
					}
					newID++;
				}while(count.moveToNext());
				ID=newID;
			}
			myDB.execSQL("INSERT INTO " + TableName + " ( ID, Name ) VALUES ( '" + ID+"', '"+groupName + "' );" );

			String DatabaseName="Database_"+ID;

			createTables(DatabaseName,members);	        


		}
		catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(myDB!=null)
				myDB.close();
		}

		this.finish();
	}

	public void createTables(String databaseName,String[] members){
		SQLiteDatabase groupDatabase=null;
		try{
			groupDatabase=this.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
			groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MainActivity.MemberTable
					+ " ( ID int(11) NOT NULL, Name varchar(255) NOT NULL, Balance float NOT NULL );");
			/*groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
     	           + MainActivity.EventTable
     	           + " ( ID int(11) NOT NULL, Name varchar(255) NOT NULL );");
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
      	           + MainActivity.TransTable
      	           + " ( ID int(11) NOT NULL, MemberId int(11) NOT NULL, Amount float NOT NULL, EventId int(11) NOT NULL );");
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
       	           + MainActivity.CashTable
       	           + " ( ID int(11) NOT NULL, FromMemberId int(11) NOT NULL, ToMemberId int(11) NOT NULL, Amount float NOT NULL);");
			 */
			int length=members.length;
			for(int j=0;j<length;j++){
				groupDatabase.execSQL("INSERT INTO "+MainActivity.MemberTable + " ( ID, Name, Balance ) VALUES ( '" + (j+1)+"', '"+members[j] + "', '"+0+"' );" );
			}
		}catch(Exception e) {
			Log.e("Error", "Error", e);
		}
		finally{ 
			if(groupDatabase!=null)
				groupDatabase.close();
		}
	}
}
