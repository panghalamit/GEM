package com.example.groupexpensemanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class PossibleSolution extends Activity {
	
	public String[] namearray;
    public float[] balancearray;
    public int[] idarray;
    public int countmembers;
    public String[][] solutionarray;
    public int ntransactions = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        idarray = intent.getIntArrayExtra(GroupSummaryActivity.listofid);
        balancearray = intent.getFloatArrayExtra(GroupSummaryActivity.listofbalance);
        namearray = intent.getStringArrayExtra(GroupSummaryActivity.listofmember);
        countmembers = intent.getIntExtra(GroupSummaryActivity.stringcount, 0);
        setContentView(R.layout.activity_possible_solution);
        compute();
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
    
    public void compute(){
    	int minindex, maxindex;
    	float net, tramount;
    	solutionarray = new String[5][5];
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
