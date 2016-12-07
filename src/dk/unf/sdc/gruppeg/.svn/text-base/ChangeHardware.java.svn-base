package dk.unf.sdc.gruppeg;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.provider.Settings;

public class ChangeHardware {

	public void changeWiFi(Context context, boolean enabled) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(enabled);
	}

	public void changeBlueTooth(boolean enabled) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (enabled) {
			mBluetoothAdapter.enable();
		} else {
			mBluetoothAdapter.disable();
		}
	}

	public void changeVibrator(Context context, boolean enabled) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int currentRingerMode = audioManager.getRingerMode();
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
/**
 * Sætter lysstyrke til tal mellem 0 og 255 (?)
 * @param context
 * @param brightness Lysstyrken
 */
	private void changeBrightness(Context context, int brightness) {
		android.provider.Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness);
	}
/**
 * Sæt lysstyrken fuld tændt (true) eller så langt ned som muligt (false;
 * @param context
 * @param brightness tændt eller sluk
 */
	private void changeBrightness(Context context, boolean brightness) {
		android.provider.Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness?255:0);
	}
	
	private void changeFlightmode(Context context, boolean enabled){
		int on = enabled ? 1 : 0;
		android.provider.Settings.System.putInt(
		      context.getContentResolver(),
		      Settings.System.AIRPLANE_MODE_ON, on);

		// Post an intent to reload
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", on);
		context.sendBroadcast(intent);
	}
	
	/*
	public void changeDatanetwork(Context context, boolean enabled) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		connectivityManager.setNetworkPreference(preference)
	}*/
}
