package org.pierre.remotedroid.client.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.pierre.remotedroid.Model.Constants;
import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.app.PRemoteDroid;
import org.pierre.remotedroid.client.view.ControlView;
import org.pierre.remotedroid.protocol.PRemoteDroidActionReceiver;
import org.pierre.remotedroid.protocol.action.KeyboardAction;
import org.pierre.remotedroid.protocol.action.MouseClickAction;
import org.pierre.remotedroid.protocol.action.MouseMoveAction;
import org.pierre.remotedroid.protocol.action.MouseWheelAction;
import org.pierre.remotedroid.protocol.action.PRemoteDroidAction;
import org.pierre.remotedroid.protocol.action.ScreenCaptureResponseAction;

public class ControlActivity extends AppCompatActivity implements PRemoteDroidActionReceiver {
    //private static final int FILE_EXPLORER_MENU_ITEM_ID = 0;
    //private static final int KEYBOARD_MENU_ITEM_ID = 1;
    //private static final int CONNECTIONS_MENU_ITEM_ID = 2;
    //private static final int SETTINGS_MENU_ITEM_ID = 0;
    private static final int HELP_MENU_ITEM_ID = 0;

    private PRemoteDroid application;
    private SharedPreferences preferences;
    Button keyboard, settings, mMediaplayer;
    private ControlView controlView;

    private MediaPlayer mpClickOn;
    private MediaPlayer mpClickOff;
    Button mPlay;
    Button mPrevious;
    Button mNext;
    Button mVolmeup;
    Button mVolumedown;

    private boolean feedbackSound;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.application = (PRemoteDroid) this.getApplication();

        this.preferences = this.application.getPreferences();

        this.checkFullscreen();

        this.setContentView(R.layout.control);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        keyboard = (Button) findViewById(R.id.Keyboard);
        mMediaplayer = (Button) findViewById(R.id.Media_player);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleKeyboard();
            }

            private void toggleKeyboard() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, 0);
            }
        });

        settings = (Button) findViewById(R.id.Settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = (new Intent(getApplicationContext(), SettingsActivity.class));
                startActivity(settingsIntent);
            }
        });

        mMediaplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              startActivity(new Intent(ControlActivity.this, MediaActivity.class));


/*
                Dialog dialog = new Dialog(ControlActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_media);
                dialog.show();*/
                /*mNext = (Button) dialog.findViewById(R.id.btn_Next);
                mPrevious = (Button) dialog.findViewById(R.id.btn_Prevous);
                mPlay = (Button) dialog.findViewById(R.id.btn_play);
                mVolmeup = (Button) dialog.findViewById(R.id.btn_volumeup);
                mVolumedown = (Button) dialog.findViewById(R.id.btn_volumedown);
                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        application.sendAction(new KeyboardAction(Constants.mNext));
                        Log.e("TEsting keyy>>>", "dialog next");
                    }
                });

                mPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        application.sendAction(new KeyboardAction(Constants.mPrevious));
                        Log.e("TEsting keyy>>>", "dialog previous");
                    }
                });
                mPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        application.sendAction(new KeyboardAction(Constants.mPlay));
                        Log.e("TEsting keyy>>>", "dialog play");
                    }
                });
                mVolmeup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        application.sendAction(new KeyboardAction(Constants.mMediaVolumeup));
                        Log.e("TEsting keyy>>>", "dialog volume up");
                    }
                });

                mVolumedown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        application.sendAction(new KeyboardAction(Constants.mMediaVolumedown));
                        Log.e("TEsting keyy>>>", "dialog down");
                    }
                });


   */         }
        });


        this.controlView = (ControlView) this.findViewById(R.id.controlView);

        this.mpClickOn = MediaPlayer.create(this, R.raw.clickon);
        this.mpClickOff = MediaPlayer.create(this, R.raw.clickoff);

        this.checkOnCreate();
    }

    protected void onResume() {
        super.onResume();

        this.application.registerActionReceiver(this);

        this.feedbackSound = this.preferences.getBoolean("feedback_sound", false);
    }

    protected void onPause() {
        super.onPause();

    //    this.application.unregisterActionReceiver(this);
    }

    Boolean mCheckState = false;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //   int unicode = event.getUnicodeChar();
        int Keycode = event.getKeyCode();
        Log.e("TEsting keyy>>>", "" + event.getKeyCode());
        //  Log.e("Testing", "" + KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN);
        mCheckState = event.isShiftPressed();
        //Log.e("Testing", "" + KeyEvent.KEYCODE_AT);
       /* mCheckattherate = KeyEvent.KEYCODE_AT;
        mCheckDot = KeyEvent.KEYCODE_NUMPAD_DOT;

        Log.e("Testing states", ""+mCheckDot+">>>>"+ mCheckattherate );
        // Log.e("Testing Control Activity", "" + unicode);
        if (Keycode == 0 && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            Keycode = KeyboardAction.UNICODE_BACKSPACE;
        }
        if (mCheckattherate == KeyEvent.KEYCODE_AT) {
            Log.e("TEsting keyy>>>", "called keycode at");
            this.application.sendAction(new KeyboardAction(Constants.mRate));
            mCheckattherate = 0;
        }
        if (mCheckDot==KeyEvent.KEYCODE_NUMPAD_DOT) {
            Log.e("TEsting keyy>>>", "called Dot" );
            this.application.sendAction(new KeyboardAction(Constants.mDot));
            mCheckDot = 0;



        }*/


       /*if (unicode == 0 && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            unicode = KeyboardAction.UNICODE_BACKSPACE;
        }*/

       /* if (unicode != 0) {
            this.application.sendAction(new KeyboardAction(unicode));
        }*/


        switch (Keycode) {
           /* case KeyEvent.KEYCODE_PLUS:

                this.application.sendAction(new KeyboardAction(Constants.mPlus));

                break;*/
            case KeyEvent.KEYCODE_POUND:

                this.application.sendAction(new KeyboardAction(Constants.mPound));

                break;
          /*  case KeyEvent.KEYCODE_MINUS:

                this.application.sendAction(new KeyboardAction(Constants.mMinus));

                break;*/
            case KeyEvent.KEYCODE_SLASH:

                this.application.sendAction(new KeyboardAction(Constants.mSlash));

                break;
            /*case KeyEvent.KEYCODE_STAR:

                this.application.sendAction(new KeyboardAction(Constants.mStar));

                break;
            case KeyEvent.KEYCODE_APOSTROPHE:

                this.application.sendAction(new KeyboardAction(Constants.mAthospere));

                break;*/
            case KeyEvent.KEYCODE_SEMICOLON:

                this.application.sendAction(new KeyboardAction(Constants.mSemicolon));

                break;
            case KeyEvent.KEYCODE_VOLUME_UP:

                Log.e("TEsting keyy>>>", "called volumeup" + Keycode);

                this.application.sendAction(new KeyboardAction(Constants.Volumeup));

                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                Log.e("TEsting keyy>>>", "called volume down" + Keycode);

                this.application.sendAction(new KeyboardAction(Constants.Volumedown));

                break;
            case KeyEvent.KEYCODE_A:

                if (mCheckState) {

                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsA)));
                    Log.e("testing if A", "called");

                } else {

                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallA)));
                    Log.e("testing else A", "called");


                }
                mCheckState = false;
                break;

            case KeyEvent.KEYCODE_B:
                Log.e("TEsting keyy>>>", "B");
                //your Action code
                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsB)));
                    Log.e("testing if B", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallB)));
                    Log.e("testing else B", "called");


                }


                break;
            case KeyEvent.KEYCODE_C:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsC)));
                    Log.e("testing if C", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallC)));
                    Log.e("testing else C", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_D:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsD)));
                    Log.e("testing if D", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallD)));
                    Log.e("testing else D", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.META_CAPS_LOCK_ON:
                Log.e("TEsting keyy>>>", "caps llock on");
            case KeyEvent.KEYCODE_E:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsE)));
                    Log.e("testing if E", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallE)));
                    Log.e("testing else E", "called");
                }
                mCheckState = false;

                break;


            case KeyEvent.KEYCODE_F:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsF)));
                    Log.e("testing if F", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallF)));
                    Log.e("testing else F", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_G:
                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsG)));
                    Log.e("testing if G", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallG)));
                    Log.e("testing else G", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_H:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsH)));
                    Log.e("testing if H", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallH)));
                    Log.e("testing else H", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_I:


                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsI)));
                    Log.e("testing if I", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallI)));
                    Log.e("testing else I", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_J:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsJ)));
                    Log.e("testing if J", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallJ)));
                    Log.e("testing else J", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_K:
                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsK)));
                    Log.e("testing if G", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallK)));
                    Log.e("testing else G", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_L:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsL)));
                    Log.e("testing if L", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallL)));
                    Log.e("testing else L", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_M:
                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsM)));
                    Log.e("testing if M", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallM)));
                    Log.e("testing else M", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_N:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsN)));
                    Log.e("testing if N", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallN)));
                    Log.e("testing else N", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_O:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsO)));
                    Log.e("testing if O", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallO)));
                    Log.e("testing else O", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_P:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsP)));
                    Log.e("testing if P", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallP)));
                    Log.e("testing else P", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_Q:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsQ)));
                    Log.e("testing if Q", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallQ)));
                    Log.e("testing else Q", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_R:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsR)));
                    Log.e("testing if R", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallR)));
                    Log.e("testing else R", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_S:
                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsS)));
                    Log.e("testing if S", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallS)));
                    Log.e("testing else S", "called");
                }
                mCheckState = false;


                break;
            case KeyEvent.KEYCODE_T:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsT)));
                    Log.e("testing if T", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallT)));
                    Log.e("testing else T", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_U:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsU)));
                    Log.e("testing if U", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallU)));
                    Log.e("testing else U", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_V:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsV)));
                    Log.e("testing if V", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallV)));
                    Log.e("testing else V", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_W:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsW)));
                    Log.e("testing if W", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallW)));
                    Log.e("testing else w", "called");
                }
                mCheckState = false;
                break;
            case KeyEvent.KEYCODE_X:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsX)));
                    Log.e("testing if X", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallX)));
                    Log.e("testing else X", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_Y:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsY)));
                    Log.e("testing if Y", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallY)));
                    Log.e("testing else Y", "called");
                }
                mCheckState = false;

                break;
            case KeyEvent.KEYCODE_Z:

                if (mCheckState) {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mCapsZ)));
                    Log.e("testing if Z", "called");

                } else {
                    this.application.sendAction(new KeyboardAction(mStringConverter(Constants.mSmallZ)));
                    Log.e("testing else Z", "called");
                }
                mCheckState = false;


                break;

            case KeyEvent.KEYCODE_1:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum1)));

                break;
            case KeyEvent.KEYCODE_2:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum2)));


                break;
            case KeyEvent.KEYCODE_3:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum3)));


                break;
            case KeyEvent.KEYCODE_4:


                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum4)));


                break;
            case KeyEvent.KEYCODE_5:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum5)));

                break;
            case KeyEvent.KEYCODE_6:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum6)));

                break;
            case KeyEvent.KEYCODE_7:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum7)));

                break;
            case KeyEvent.KEYCODE_8:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum8)));

                break;
            case KeyEvent.KEYCODE_9:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum9)));

                break;
            case KeyEvent.KEYCODE_0:

                this.application.sendAction(new KeyboardAction(Integer.parseInt(Constants.mNum0)));

                break;
            case KeyEvent.KEYCODE_DEL:

                this.application.sendAction(new KeyboardAction(Constants.mBackspace));

                break;
            case KeyEvent.KEYCODE_ENTER:

                this.application.sendAction(new KeyboardAction(Constants.mEnter));

                break;

            case KeyEvent.KEYCODE_AT:
                Log.e("TEsting keyy>>>", "called at te rate");
                this.application.sendAction(new KeyboardAction(Constants.mRate));

                break;
            case KeyEvent.KEYCODE_PERIOD:

                this.application.sendAction(new KeyboardAction(Constants.mDot));

                break;
        /*    case KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN:

                Log.e("TEsting keyy>>>", "called right pattrn");

                break;*/
            case KeyEvent.KEYCODE_SPACE:

                this.application.sendAction(new KeyboardAction(Constants.mSpace));

                break;

        }

        return super.onKeyDown(keyCode, event);
    }

    public int mStringConverter(String val) {
        Log.e("Testing" + "" + val, "" + (int) val.charAt(0));
        return (int) val.charAt(0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(Menu.NONE, FILE_EXPLORER_MENU_ITEM_ID, Menu.NONE, this.getResources().getString(R.string.text_file_explorer));
        //menu.add(Menu.NONE, KEYBOARD_MENU_ITEM_ID, Menu.NONE, this.getResources().getString(R.string.text_keyboard));
        //menu.add(Menu.NONE, CONNECTIONS_MENU_ITEM_ID, Menu.NONE, this.getResources().getString(R.string.text_connections));
        //menu.add(Menu.NONE, SETTINGS_MENU_ITEM_ID, Menu.NONE, this.getResources().getString(R.string.text_settings));
        menu.add(Menu.NONE, HELP_MENU_ITEM_ID, Menu.NONE, this.getResources().getString(R.string.text_help));

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case FILE_EXPLORER_MENU_ITEM_ID:
                this.startActivity(new Intent(this, FileExplorerActivity.class));
				break;
			case KEYBOARD_MENU_ITEM_ID:
				this.toggleKeyboard();
				break;
			case CONNECTIONS_MENU_ITEM_ID:
				this.startActivity(new Intent(this, ConnectionListActivity.class));
				break;
			case SETTINGS_MENU_ITEM_ID:
				this.startActivity(new Intent(this, SettingsActivity.class));
				break;*/
            case HELP_MENU_ITEM_ID:
                this.startActivity(new Intent(this, HelpActivity.class));
                break;
        }

        return true;
    }

    public void receiveAction(PRemoteDroidAction action) {
        if (action instanceof ScreenCaptureResponseAction) {
            this.controlView.receiveAction((ScreenCaptureResponseAction) action);
        }
    }

    public void mouseClick(byte button, boolean state) {
        this.application.sendAction(new MouseClickAction(button, state));

        if (this.feedbackSound) {
            if (state) {
                this.playSound(this.mpClickOn);
            } else {
                this.playSound(this.mpClickOff);
            }
        }
    }

    public void mouseMove(int moveX, int moveY) {
        this.application.sendAction(new MouseMoveAction((short) moveX, (short) moveY));
    }

    public void mouseWheel(int amount) {
        this.application.sendAction(new MouseWheelAction((byte) amount));
    }

    private void playSound(MediaPlayer mp) {
        if (mp != null) {
            mp.seekTo(0);
            mp.start();
        }
    }

	/*private void toggleKeyboard()
    {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, 0);
	}*/

    private void checkFullscreen() {
        if (this.preferences.getBoolean("fullscreen", false)) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void checkOnCreate() {
        if (this.checkFirstRun()) {
            this.firstRunDialog();
        } else if (this.checkNewVersion()) {
            this.newVersionDialog();
        }
    }

    private boolean checkFirstRun() {
        return this.preferences.getBoolean("debug_firstRun", true);
    }

    private void firstRunDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.text_first_run_dialog);
        builder.setPositiveButton(R.string.text_yes, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ControlActivity.this.startActivity(new Intent(ControlActivity.this, HelpActivity.class));
                ControlActivity.this.disableFirstRun();
            }
        });
        builder.setNegativeButton(R.string.text_no, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ControlActivity.this.disableFirstRun();
            }
        });
        builder.create().show();
    }

    private void disableFirstRun() {
        Editor editor = this.preferences.edit();
        editor.putBoolean("debug_firstRun", false);
        editor.commit();

        this.updateVersionCode();
    }

    private boolean checkNewVersion() {
        try {
            if (this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_META_DATA).versionCode != this.preferences.getInt("app_versionCode", 0)) {
                return true;
            }
        } catch (NameNotFoundException e) {
            this.application.debug(e);
        }

        return false;
    }

    private void newVersionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.text_new_version_dialog);
        builder.setNeutralButton(R.string.text_ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ControlActivity.this.updateVersionCode();
            }
        });
        builder.create().show();
    }

    private void updateVersionCode() {
        try {
            Editor editor = this.preferences.edit();
            editor.putInt("app_versionCode", this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_META_DATA).versionCode);
            editor.commit();
        } catch (NameNotFoundException e) {
            this.application.debug(e);
        }
    }

}
