package org.pierre.remotedroid.client.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.app.PRemoteDroid;

public class SettingsActivity extends PreferenceActivity
{
	private static final String[] tabFloatPreferences = {
	        "control_sensitivity", "control_acceleration", "control_immobile_distance", "screenCapture_cursor_size", "buttons_size", "wheel_bar_width"
	};
	private static final String[] tabIntPreferences = {
	        "control_click_delay", "control_hold_delay"
	};
	
	private static final int resetPreferencesMenuItemId = 0;
	
	private PRemoteDroid application;
	private SharedPreferences preferences;
	Toolbar Tbar;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AppBarLayout bar;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
			bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
			root.addView(bar, 0);
		} else {
			ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
			ListView content = (ListView) root.getChildAt(0);
			root.removeAllViews();
			bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);

			int height;
			TypedValue tv = new TypedValue();
			if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
				height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
			}else{
				height = bar.getHeight();
			}

			content.setPadding(0, height, 0, 0);

			root.addView(content);
			root.addView(bar);
		}
		 Tbar = (Toolbar) findViewById(R.id.toolbar2);
		 Tbar = (Toolbar) bar.getChildAt(0);

		Tbar.setNavigationOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});


		//this.prepareLayout();
		this.addPreferencesFromResource(R.xml.settings);

		this.application = (PRemoteDroid) this.getApplication();
		this.preferences = this.application.getPreferences();
	}

	/*private void prepareLayout() {
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		View content = root.getChildAt(0);
		LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.setting_toolbar, null);

		root.removeAllViews();
		toolbarContainer.addView(content);
		root.addView(toolbarContainer);

		mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
		mToolBar.setTitle("");
	}*/

	protected void onPause()
	{
		super.onPause();
		
		this.checkPreferences();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, resetPreferencesMenuItemId, Menu.NONE, this.getResources().getString(R.string.text_reset_preferences));
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case resetPreferencesMenuItemId:
				this.resetPreferences();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void checkPreferences()
	{
		Editor editor = this.preferences.edit();
		
		for (String s : tabFloatPreferences)
		{
			try
			{
				Float.parseFloat(this.preferences.getString(s, null));
			}
			catch (NumberFormatException e)
			{
				this.application.debug(e);
				editor.remove(s);
			}
		}
		
		for (String s : tabIntPreferences)
		{
			try
			{
				Integer.parseInt(this.preferences.getString(s, null));
			}
			catch (NumberFormatException e)
			{
				this.application.debug(e);
				editor.remove(s);
			}
		}
		
		editor.commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.settings, true);
	}
	
	private void resetPreferences()
	{
		this.setPreferenceScreen(null);
		
		Editor editor = this.preferences.edit();
		editor.clear();
		editor.commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.settings, true);
		
		this.addPreferencesFromResource(R.xml.settings);
	}
}
