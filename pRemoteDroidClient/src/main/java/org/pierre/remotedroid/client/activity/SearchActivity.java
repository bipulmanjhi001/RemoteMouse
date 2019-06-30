package org.pierre.remotedroid.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.activity.connection.ConnectionListActivity;

public class SearchActivity extends AppCompatActivity {

    Button BtnSearch_wifi,BtnSearch_bluetooth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        BtnSearch_wifi=(Button)findViewById(R.id.search_wifi);
        BtnSearch_bluetooth=(Button)findViewById(R.id.search_bluetooth);

        BtnSearch_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wifi_intent= new Intent(getApplicationContext(), ConnectionListActivity.class);
                startActivity(wifi_intent);
            }
        });

        BtnSearch_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bluetooth_intent= new Intent(getApplicationContext(), ConnectionListActivity.class);
                startActivity(bluetooth_intent);
            }
        });
    }

}
