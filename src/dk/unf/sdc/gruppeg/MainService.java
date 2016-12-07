package dk.unf.sdc.gruppeg;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import dk.unf.sdc.gruppeg.data.ProfilesDatabaseConnection;

public class MainService extends Service {
	private final IBinder mBinder = new LocalBinder();
	public final static String UPDATE_COUNTER = "dk.unf.gruppeG.power.loction.service";
	private Thread counter;
	private static final int HELLO_ID = 1;
	Location userLocation;
	BluetoothAdapter mBluetoothAdapter;
	LocationListener mlocListener = new myLocationManegerListener();
	private Context context;
	LocationManager myLocationManeger;
	String provider;
	int timeBetweenUpdates = 60000;

	private List<Profil> profilesCache= new ArrayList<Profil>();;

	// Bliver kalder noget vores activity binder til vores service
	@Override
	public IBinder onBind(Intent intent) {
		updateProfileList();
		mBluetoothAdapter= BluetoothAdapter
				.getDefaultAdapter();
		myLocationManeger = (LocationManager) MenuActivity.menuContext
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria crta = new Criteria();
		crta.setAccuracy(Criteria.ACCURACY_COARSE);
		crta.setAltitudeRequired(false);
		crta.setBearingRequired(false);
		crta.setCostAllowed(true);
		crta.setPowerRequirement(Criteria.POWER_LOW);
		provider = myLocationManeger.getBestProvider(crta, true);

		profilesCache = getData();
		showNotification("Started service.");
		
		myLocationManeger.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, timeBetweenUpdates, 500,
				mlocListener);

		Log.v("", "onBind bliver kaldt");
		// Starter en ny tråd hvor vi tæller tiden op i
		counter = new Thread(new Runnable() {
			public void run() {
				// Sørger for at den kører hele tiden
				while (true) {
					// Venter 1 sek (1000 millisekunder)
					try {
						// Log.v("", "Service køre");

						updateProfileList();
						updateHardwareSettings();
						Thread.sleep(timeBetweenUpdates/2);
						if(profilesCache==null||profilesCache.size()==0){updateProfileList();}
						Thread.sleep(timeBetweenUpdates/2);

					} catch (InterruptedException e) {
						Log.e("", "Could not sleep thread");
						e.printStackTrace();
					}

				}
			}
		});
		counter.start();

		// Returnerer for at sige at vi er forbundet til activiteten
		return mBinder;
	}

	public boolean brugerPosition(Profil profil) {
		if (profil.isBrugLocation()) {
			return true;
		}
		return false;
	}
	
	private void showNotification(String text) {
		Resources res = this.getResources();
		PendingIntent intent1 = PendingIntent.getActivity(this, 0, new Intent(
				this, MenuActivity.class), 0);

		Builder noti = new NotificationCompat.Builder(this);
		noti.setContentIntent(intent1)
				.setContentTitle("Location awareness")
				.setContentText(text)
				.setSmallIcon(R.drawable.notofication_icon)
				.setLargeIcon(
						BitmapFactory.decodeResource(res,
								R.drawable.ic_launcher));
		Notification myNoti = noti.getNotification();

		startForeground(HELLO_ID, myNoti);
	}

	/**
	 * Her har vi lige fået en ny location. Den vidergives til alle profiler med
	 * behovet for location-objekter. Der tages ikke højde for manglende data.
	 */
	public class myLocationManegerListener implements LocationListener {
		public void onLocationChanged(Location location) {
			Log.e("", "OnLocationChanged");
			//updateProfileList();
			if (profilesCache == null||profilesCache.size()==0) {
				Log.v("","Profile cache is empty!!! (location)");
			}
			List<Profil> profilesList = profilesCache;
			Log.v("", "Looping through " + profilesList.size()
					+ " profiles LOCATION");
			Log.e("", location.getLongitude() + "");
			for (int i = 0; i < profilesList.size(); i++) {
				Log.v("", "Current profile: " + profilesList.get(i).getName());
				if (profilesList.get(i).getActive() && profilesList.get(i).isBrugLocation()) {
					profilesList.get(i).locationChanged(location);
				}
			}

		}

		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}

		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e("", status + "");
			// required for interface, not used
		}
	};

	// Gør at vi kan binde vores activity til denne service
	public class LocalBinder extends Binder {
		MainService getService() {
			return MainService.this;
		}
	}

	public void updateHardwareSettings() {
		Log.v("", "Looping through profiles HARDWARE");

		userLocation = myLocationManeger.getLastKnownLocation(provider);
		if (userLocation != null) {
			Log.e("", userLocation.getLatitude() + "");
		}
		
		

		List<Profil> profilesList = profilesCache;//getData();
		Log.v("", profilesList.size() + " profiles to loop");
		for (int i = 0; i < profilesList.size(); i++) {
			/*boolean hentLokation = false; // TODO bliver ikke brugt andre steder
											// endnu
			if (profilesList.get(i).getActive()) {
				if (brugerPosition(profilesList.get(i))) {
					hentLokation = true;
				}
			}
			if (true) {

			}*/
			if (profilesList.get(i).isProfileConditionsSatisfied(userLocation) ) {
				Log.e("","Profilen "+profilesList.get(i).getName()+" skal sættes i gang");
				loopThroughHardwareSettings(profilesList.get(i));
				showNotification("Profile "+profilesList.get(i).getName()+" activated.");
				break;
			}
		}
	}
	
	public void updateProfileList(){
		List<Profil> newProfilesList = getData();
		for (int i = 0; i < profilesCache.size(); i++) {
			if(profilesCache.get(i).isLocated()){
				if(profilesCache.get(i).getActive()==false){
					continue;
				}
				int locatedID=profilesCache.get(i).getID();
				for (int n = 0; n < newProfilesList.size(); n++) {
					if(newProfilesList.get(i).getID()==locatedID){
						newProfilesList.get(i).setID(locatedID);
						break;
					}
				}
			}
		}
		profilesCache=newProfilesList;
	}

	public void loopThroughHardwareSettings(Profil profil) {
		Log.e("", "Chaning hardware profiles");

		// FIXME Lav context for klassen eller inde i metoden?
		changeWiFi(getApplicationContext(), profil.isWifi());
		Log.e("", (profil.isWifi() ? "Starter" : "Stopper") + " wifi");
		changeVibrator(getApplicationContext(), profil.isVibrator());
		Log.e("", (profil.isLydløs() ? "Starter" : "Stopper") + " vibrator");
		changeSound(getApplicationContext(), profil.isLydløs()); 
		Log.e("", (profil.isLydløs() ? "Starter" : "Stopper") + " lydløs");
		changeAirplaneMode(getApplicationContext(), profil.isFlymode());
		/*Log.e("", (profil.isFlymode() ? "Starter" : "Stopper") + " flymode");
		changeBrightness(getApplicationContext(), profil.isLysstyrke());*/
		Log.e("", (profil.isLysstyrke() ? "Starter" : "Stopper") + " lysstyrke");
		changeBlueTooth(profil.isBluetooth());
		Log.e("", (profil.isBluetooth() ? "Starter" : "Stopper") + " bluetooth");
	}

	public void changeWiFi(Context context, boolean enabled) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(enabled);
	}

	// FIXME: Virker kun nogle steder
	public void changeBlueTooth(boolean enabled) {
		if (enabled) {
			mBluetoothAdapter.enable();
		} else {
			mBluetoothAdapter.disable();
		}
	}

	/**
	 * 
	 * @param context
	 *            context
	 * @param enabled
	 *            false slår vibrator til.
	 */
	public void changeVibrator(Context context, boolean enabled) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (enabled) {
			audioManager.setRingerMode(AudioManager.VIBRATE_SETTING_OFF);
		} else {
			audioManager.setRingerMode(AudioManager.VIBRATE_SETTING_ON);
		}
	}

	public void changeSound(Context context, boolean enabled) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (enabled) {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}

	public void changeAirplaneMode(Context context, boolean enabled) {
		int on = enabled ? 1 : 0;
		android.provider.Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, on);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", on);
		context.sendBroadcast(intent);
	}

	private void changeBrightness(Context context, int brightness) {
		/*android.provider.Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness);*/
	}

	private void changeBrightness(Context context, boolean brightness) {
		/*android.provider.Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness ? 255 : 0);*/
	}

	@Override
	public boolean onUnbind(Intent intent) {
		showNotification("LONG TEST");
		return true;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private List<Profil> getData() {
		try {
			ProfilesDatabaseConnection dbConnection = new ProfilesDatabaseConnection(
					getApplicationContext());

			final List<Profil> myList = dbConnection.getProfiles();
			dbConnection.close();
			return myList;
		} catch (Exception e) {
			Log.e("", "Database crashed and return no profiles");
		}

		return new ArrayList<Profil>();
	}
}
