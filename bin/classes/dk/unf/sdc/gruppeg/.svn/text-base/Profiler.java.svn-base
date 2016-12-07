package dk.unf.sdc.gruppeg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import dk.unf.sdc.gruppeg.data.ProfilesDatabaseConnection;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Profiler {

	private List<Profil> alleProfiler;
	private Context context;

	public Profiler(Context context) {
		this.context = context;


		/*if (brugerPosition()) {
			// Note: context er måske ikke nødvendig, Context kan også bruges
			// (?)
			LocationManager myLocation = (LocationManager) this.context
					.getSystemService(this.context.LOCATION_SERVICE);

			// if.
			// https://github.com/commonsguy/cw-android/blob/master/Internet/Weather/src/com/commonsware/android/internet/WeatherDemo.java
			// bør/kan denne komando ligge i onResume
			myLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					1, 500, onLocationChange);
		}*/
	}

	/**
	 * Her har vi lige fået en ny location. Den vidergives til alle profiler med
	 * behovet for location-objekter. Der tages ikke højde for manglende data.
	 */
	/*
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location location) {
			for (Profil profil : alleProfiler) {
				if (profil.isBrugLocation()) {
					profil.locationChanged(location);
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
			// required for interface, not used
		}
	};*/

	public boolean brugerPosition() {
		for (Profil profil : alleProfiler) {
			if (profil.isBrugLocation()) {
				return true;
			}
		}
		return false;
	}

}
