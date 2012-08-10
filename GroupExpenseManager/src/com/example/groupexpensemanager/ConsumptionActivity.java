package com.example.groupexpensemanager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConsumptionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_consumption, menu);
        return true;
    }
}
