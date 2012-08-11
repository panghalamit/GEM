package com.example.groupexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class PossibleSolution extends Activity {

	public String[] namearray;
	public float[] balancearray;
	public int[] idarray;
	public int countmembers;
	public String[][] solutionarray;
	public String groupName="";
	public int ntransactions = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		groupName=intent.getStringExtra(GroupsActivity.GROUP_NAME);
		String new_title= groupName+" - "+String.valueOf(this.getTitle());
		this.setTitle(new_title);
		idarray = intent.getIntArrayExtra(GroupSummaryActivity.listofid);
		balancearray = intent.getFloatArrayExtra(GroupSummaryActivity.listofbalance);
		namearray = intent.getStringArrayExtra(GroupSummaryActivity.listofmember);
		countmembers = intent.getIntExtra(GroupSummaryActivity.stringcount, 0);
		setContentView(R.layout.activity_possible_solution);
		compute();
		filltable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_possible_solution, menu);
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

	public void filltable(){
		TableLayout tl = (TableLayout)findViewById(R.id.PossibeSolutionTable);
		for(int i=0;i<ntransactions;i++){
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			TextView v1= new TextView(this);
			v1.setText(solutionarray[i][0]);
			TextView v2= new TextView(this);
			v2.setText(solutionarray[i][2]);
			TextView v3= new TextView(this);
			v3.setText(solutionarray[i][4]);
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
	public void compute(){
		int minindex, maxindex;
		float net, tramount;
		solutionarray = new String[countmembers][5];
		ntransactions = 0;
		while(true){
			minindex = 0;
			maxindex = 0;
			net = 0;
			tramount = 0;
			for(int i=1;i<countmembers;i++){
				if(balancearray[i]<balancearray[minindex]){
					minindex=i;
				}
				else if(balancearray[i]>balancearray[maxindex]){
					maxindex=i;
				}
			}
			if(balancearray[minindex]==0 || balancearray[maxindex]==0){
				break;
			}

			net = balancearray[minindex] + balancearray[maxindex];
			if(net>0){
				tramount = - balancearray[minindex];
				balancearray[minindex]=0;
				balancearray[maxindex]=net;
				solutionarray[ntransactions][4] = String.valueOf(tramount); 
			}
			else{
				tramount = balancearray[maxindex];
				balancearray[minindex]=net;
				balancearray[maxindex]=0;
				solutionarray[ntransactions][4] = String.valueOf(tramount); 
			}
			solutionarray[ntransactions][0] = namearray[minindex];
			solutionarray[ntransactions][1] = String.valueOf(idarray[minindex]);
			solutionarray[ntransactions][2] = namearray[maxindex];
			solutionarray[ntransactions][3] = String.valueOf(idarray[maxindex]);
			ntransactions++;
		}
	}
}
