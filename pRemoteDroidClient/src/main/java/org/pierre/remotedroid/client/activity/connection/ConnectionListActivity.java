package org.pierre.remotedroid.client.activity.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.pierre.remotedroid.client.R;
import org.pierre.remotedroid.client.activity.ControlActivity;
import org.pierre.remotedroid.client.app.PRemoteDroid;
import org.pierre.remotedroid.client.bluetooth.BluetoothChecker;
import org.pierre.remotedroid.client.connection.Connection;
import org.pierre.remotedroid.client.connection.ConnectionBluetooth;
import org.pierre.remotedroid.client.connection.ConnectionList;
import org.pierre.remotedroid.client.connection.ConnectionWifi;

public class ConnectionListActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener, OnClickListener
{
	Toolbar toolbar;
	private static final int NEW_MENU_ITEM_ID = 0;

	private PRemoteDroid application;

	private ConnectionList connections;
	 ListView listView;
Button back;
	private ConnectionListAdapter adapter;

	private AlertDialog alertDialogNew;

	private AlertDialog alertDialogItemLongClick;

	private int itemLongClickPosition;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_list_activity);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");

		back=(Button)findViewById(R.id.button);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			}
		});
		listView=(ListView)findViewById(R.id.listView);
		this.application = (PRemoteDroid) this.getApplication();

		this.connections = this.application.getConnections();

		this.adapter = new ConnectionListAdapter(this, this.connections);

		listView.setAdapter(this.adapter);

		listView.setOnItemClickListener(this);

		listView.setOnItemLongClickListener(this);

		this.init();
	}

	protected void onResume()
	{
		super.onResume();

		this.refresh();

		if (this.connections.getCount() == 0)
		{
			this.application.showToast(R.string.text_no_connection);
		}
	}

	protected void onPause()
	{
		super.onPause();

		this.connections.save();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, NEW_MENU_ITEM_ID, Menu.NONE, R.string.text_new);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case NEW_MENU_ITEM_ID:
				this.alertDialogNew.show();
				break;
		}

		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		this.useConnection(position);
		this.startActivity(new Intent(this, ControlActivity.class));
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		this.itemLongClickPosition = position;

		this.alertDialogItemLongClick.show();

		return true;
	}

	public void onClick(DialogInterface dialog, int which)
	{
		if (dialog == this.alertDialogNew)
		{
			this.addConnection(which);
		}
		else if (dialog == this.alertDialogItemLongClick)
		{
			this.menu(which);
		}
	}

	private void menu(int which)
	{
		Connection connection = this.connections.get(this.itemLongClickPosition);

		switch (which)
		{
			case 0:
				this.useConnection(this.itemLongClickPosition);
				break;
			case 1:
				connection.edit(this);
				break;
			case 2:
				this.removeConnection();
				break;
		}
	}

	private void addConnection(int which)
	{
		if (which == Connection.BLUETOOTH && !BluetoothChecker.isBluetoohAvailable())
		{
			this.application.showToast(R.string.text_bluetooth_not_available_version);
		}
		else
		{
			Connection connection = this.connections.add(which);

			this.refresh();

			connection.edit(this);
		}
	}

	private void useConnection(int position)
	{
		this.connections.use(position);
		this.refresh();
	}

	private void removeConnection()
	{
		this.connections.remove(this.itemLongClickPosition);
		this.refresh();
	}

	private void refresh()
	{
		this.connections.sort();
		this.adapter.notifyDataSetChanged();
	}

	private void init()
	{
		this.initAlertDialogNew();

		this.initAlertDialogItemLongClick();
	}

	private void initAlertDialogNew()
	{
		String[] connectionTypeName = {
		        this.getResources().getString(R.string.text_wifi), this.getResources().getString(R.string.text_bluetooth)
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.text_connection_type);
		builder.setItems(connectionTypeName, this);
		this.alertDialogNew = builder.create();
	}

	private void initAlertDialogItemLongClick()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(R.array.connection_action_name, this);
		this.alertDialogItemLongClick = builder.create();
	}

	private class ConnectionListAdapter extends BaseAdapter
	{
		private ConnectionList connections;
		private LayoutInflater layoutInflater;

		private int connectionUsedPosition;

		public ConnectionListAdapter(Context context, ConnectionList connections)
		{
			this.connections = connections;

			this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			this.connectionUsedPosition = this.connections.getUsedPosition();
		}

		public void notifyDataSetChanged()
		{
			super.notifyDataSetChanged();

			this.connectionUsedPosition = this.connections.getUsedPosition();
		}

		public int getCount()
		{
			return this.connections.getCount();
		}

		public Connection getItem(int position)
		{
			return this.connections.get(position);
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ConnectionViewHolder holder;

			if (convertView == null)
			{
				holder = new ConnectionViewHolder();

				convertView = this.layoutInflater.inflate(R.layout.connection, null);

				holder.use = (RadioButton) convertView.findViewById(R.id.use);
				holder.use.setVisibility(convertView.INVISIBLE);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ConnectionViewHolder) convertView.getTag();
			}

			Connection connection = this.connections.get(position);

			holder.use.setChecked(position == this.connectionUsedPosition);

			if (connection instanceof ConnectionWifi)
			{
				holder.icon.setImageResource(R.drawable.wi);
			}
			else if (connection instanceof ConnectionBluetooth)
			{
				holder.icon.setImageResource(R.drawable.bt);
			}

			holder.name.setText(connection.getName());

			return convertView;
		}

		private class ConnectionViewHolder
		{
			public RadioButton use;
			public ImageView icon;
			public TextView name;
		}
	}
}

