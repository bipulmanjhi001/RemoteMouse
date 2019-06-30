package org.pierre.remotedroid.client.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.pierre.remotedroid.Model.Constants;
import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.app.PRemoteDroid;
import org.pierre.remotedroid.protocol.PRemoteDroidActionReceiver;
import org.pierre.remotedroid.protocol.action.KeyboardAction;
import org.pierre.remotedroid.protocol.action.PRemoteDroidAction;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener,PRemoteDroidActionReceiver {

    private PRemoteDroid application;
    ImageView mPlay;
    ImageView mNext;
    ImageView mPrevious;
    ImageView mVolumeup;
    ImageView mVolumedown;
boolean isPause=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initID();

    }

    private void initID() {

        this.application = (PRemoteDroid) this.getApplication();

        mPlay = (ImageView) findViewById(R.id.btn_Pause);
        mNext = (ImageView) findViewById(R.id.btn_next);
        mPrevious = (ImageView) findViewById(R.id.btn_Previous);
        mVolumeup = (ImageView) findViewById(R.id.btn_volumeup);
        mVolumedown = (ImageView) findViewById(R.id.btn_volumedown);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mVolumedown.setOnClickListener(this);
        mVolumeup.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.application.registerActionReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.application.unregisterActionReceiver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.btn_next:
                this.application.sendAction(new KeyboardAction(Constants.mNext));
                Log.e("MediaActivity", " testing>>> calledd");

                break;
            case R.id.btn_Previous:
                this.application.sendAction(new KeyboardAction(Constants.mPrevious));
                Log.e("Media Activity ", " testing>>>calledd");
                break;
            case R.id.btn_volumeup:
                this.application.sendAction(new KeyboardAction(Constants.mMediaVolumeup));
                Log.e("Media Activity ", " testing>>>calledd");
                break;
            case R.id.btn_volumedown:
                Log.e("Media Activity testing>>>", "calledd");
                this.application.sendAction(new KeyboardAction(Constants.mMediaVolumedown));
                break;
            case R.id.btn_Pause:

                if(isPause){
                    isPause=false;
                    mPlay.setImageResource(R.drawable.pause_button);
                }else {
                    isPause=true;
                    mPlay.setImageResource(R.drawable.play_button);
                }

                this.application.sendAction(new KeyboardAction(Constants.mPlay));
                break;
        }


    }

    public int mStringConverter(String val) {
        Log.e("Testing" + "" + val, "" + (int) val.charAt(0));
        return (int) val.charAt(0);
    }

    @Override
    public void receiveAction(PRemoteDroidAction action) {

    }
}
