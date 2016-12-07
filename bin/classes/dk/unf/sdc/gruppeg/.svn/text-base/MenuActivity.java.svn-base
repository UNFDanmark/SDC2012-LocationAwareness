package dk.unf.sdc.gruppeg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import dk.unf.sdc.gruppeg.MainService.LocalBinder;
import dk.unf.sdc.gruppeg.data.ProfilesDatabaseConnection;
import dk.unf.sdc.gruppeg.listadapters.MainMenuAdaptor;
import dk.unf.sdc.gruppeg.listadapters.SingleTextViewAdaptor;

public class MenuActivity extends Activity {

	private Button addProfileButton;
	MainService mService;
	public static Context menuContext;
	private ListView profileListView;
	private static final int HELLO_ID = 1;
	List<Profil> profileList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_activity);

		menuContext = getApplicationContext();
		profileList = new ArrayList<Profil>();
		addProfileButton = (Button) findViewById(R.id.main_menu_add_profile_button);
		profileListView = (ListView) findViewById(R.id.main_menu_list_view);

		profileList = getData();

		addProfileButton.setOnClickListener(addProfileListener);

		if (profileList.size() == 0) {
			List<String> tempStrings = new ArrayList<String>();
			tempStrings.add("Her vil dine profiler komme til at stÃ¥");
			profileListView.setAdapter(new SingleTextViewAdaptor(tempStrings,
					getApplicationContext()));
		} else {
			setUpListNormaly();
		}

	}

	private OnClickListener addProfileListener = new OnClickListener() {

		public void onClick(View v) {
			Intent myIntent = new Intent(getApplicationContext(),
					AddProfileMenuActivity.class);
			startActivity(myIntent);

		}
	};

	@Override
	// Forbinder vores service til vores activity nÃ¥r den starter
	public void onStart() {
		super.onStart();

		Intent counterService = new Intent(this, MainService.class);
		// startService(counterService);
		bindService(counterService, mConnection, BIND_AUTO_CREATE);

	}

	// Forbindelsen mellem vores service og activity
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e("", "service forbundet");
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			// showNotification();

		}

		public void onServiceDisconnected(ComponentName name) {
		}
	};

	// Modtager intents
	private BroadcastReceiver updateInterface = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Updatere vores textview hvis det er vores intent at gÃžre det

		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		switch (menuItemIndex) {
		case R.id.delete:
			removeProfile(profileList.get((int)info.id).getID());
			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		menu.setHeaderTitle(profileList.get(info.position).getName());
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu_context, menu);
	}

	public void removeProfile(int index) {
		try {
			ProfilesDatabaseConnection dbConnection = new ProfilesDatabaseConnection(
					getApplicationContext());
			dbConnection.removeProfile(index);
			dbConnection.close();
			setUpListNormaly();
		} catch (Exception e) {
		}
	}
	private void setProfileChecked(Profil profil, int pos, boolean state){
		try {
			profil.setActive(state);
			Log.v("", profil.getActive()+"");
			ProfilesDatabaseConnection db = new ProfilesDatabaseConnection(getApplicationContext());
			db.updateProfile(profil);
			db.close();

		} catch (Exception e) {
			Log.e("", "Could not edit database");
		}
	}

	public void setUpListNormaly() {

		registerForContextMenu(profileListView);
		profileList.clear();
		profileList = getData();
		for(int i =0;i<profileList.size();i++){
			Log.v("", "Profil er: "+profileList.get(i).getActive()+"");
		}

		profileListView.setAdapter(new MainMenuAdaptor(profileList,
				getApplicationContext()));
		profileListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {

				CheckedTextView myCheckedTextView = (CheckedTextView) view
						.findViewById(R.id.list_view_checked_text_view);
				myCheckedTextView.toggle();
				
				
				setProfileChecked(profileList.get(pos),pos,myCheckedTextView.isChecked());
			}
		});
	}

	@Override
	protected void onResume() {
		setUpListNormaly();
		bindService(new Intent(this, MainService.class), mConnection, BIND_AUTO_CREATE);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}

	private List<Profil> getData() {

		try {
			ProfilesDatabaseConnection dbConnection = new ProfilesDatabaseConnection(
					getApplicationContext());

			final List<Profil> myList = dbConnection.getProfiles();
			dbConnection.close();
			return myList;
		} catch (Exception e) {
			Log.e("", "Database crashed");
		}

		return new ArrayList<Profil>();
	}

}