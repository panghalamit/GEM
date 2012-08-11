package com.example.groupexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class EditGroupActivity extends Activity {
	public String[] namearray;
	public String groupName="";
	public int groupid = 0;
	public float scale = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		groupName=intent.getStringExtra(GroupsActivity.GROUP_NAME);
		groupid = intent.getIntExtra(GroupsActivity.GROUP_ID,0);
		namearray = intent.getStringArrayExtra(GroupSummaryActivity.listofmember);
		setContentView(R.layout.activity_edit_group);
		populatelist();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_group, menu);
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

	public void populatelist() {
		TableLayout tl = (TableLayout)findViewById(R.id.EditGrouptableLayout);
		scale = tl.getContext().getResources().getDisplayMetrics().density;
		TableRow tr1 = addmember("Group Name",groupName);
		tl.addView(tr1,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		for(int k=0;k<namearray.length;k++){
			TableRow tr = addmember("Member",namearray[k]);
			tl.addView(tr,new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
		}
	}
	public TableRow addmember(String text1, String text2){
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT)
				);
		int ten = (int) (10 * scale + 0.5f);
		int five = (int) (5 * scale + 0.5f);
		tr.setPadding(0, five, 0, ten);
		//Add a TextView
		TextView tv = new TextView(this);
		tv.setText(text1);
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
		et.setText(text2);
		LayoutParams l2 = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
				8f);
		l2.setMargins(0, ten, ten, 0);
		et.setLayoutParams(l2);
		tr.addView(et);
		return tr;
	}

}
