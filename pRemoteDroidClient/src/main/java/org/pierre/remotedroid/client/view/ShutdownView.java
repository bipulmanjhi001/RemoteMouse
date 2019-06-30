package org.pierre.remotedroid.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.activity.ControlActivity;
import org.pierre.remotedroid.client.app.PRemoteDroid;
import org.pierre.remotedroid.protocol.action.ShutdownAction;

/**
 * Created by Sourav Adhikary on 24-02-2016.
 */
public class ShutdownView extends Button {

    private ControlActivity controlActivity;
    private PRemoteDroid application;
    private byte button;
    public ShutdownView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.controlActivity = (ControlActivity) context;
        this.application = (PRemoteDroid) this.controlActivity.getApplication();

        switch (this.getId())
        {
            case R.id.leftClickView:
                this.button = ShutdownAction.SHUTDOWN;
                break;

        }
    }
}
