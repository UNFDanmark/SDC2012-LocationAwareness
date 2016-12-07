package dk.unf.sdc.gruppeg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import dk.unf.sdc.gruppeg.data.ProfilesDatabaseConnection;
import dk.unf.sdc.gruppeg.listadapters.EffectsActivityAdaptor;

public class AddProfileMenuActivity extends Activity {
	protected static final String String = null;
	private String addConditionsButtonText = "Betingelser";
	private Button addConditions;
	double lat = 0, lng = 0;
	private Button addProfile;
	private int radius = 0;
	private ListView effectsListView;
	private List<String> effectList;
	private List<String> descriptionList;
	private List<String> radioList;
	String activatedDays = "";
	private List<Boolean> enabledList;
	String startTimeData = "", stopTimeData = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_profile_layout);

		addConditions = (Button) findViewById(R.id.add_conditions_button);
		addProfile = (Button) findViewById(R.id.add_profile_button);
		effectsListView = (ListView) findViewById(R.id.effects_list_view);
		effectList = new ArrayList<String>();
		radioList = new ArrayList<String>();
		descriptionList = new ArrayList<String>();
		enabledList = new ArrayList<Boolean>();

		addConditions.setOnClickListener(addConditionsListener);
		addProfile.setOnClickListener(addProfileListener);

		effectList.add("Lyd");
		// effectList.add("Datatrafik");
		effectList.add("Flytilstand");
		effectList.add("Wi-Fi");
		effectList.add("Bluetooth");

		descriptionList.add("Markér for at påvirke lydstyrke");
		// descriptionList.add("Markér for at påvirke datatrafik");
		descriptionList.add("Markér for at påvirke flytilstand");
		descriptionList.add("Markér for at påvirke Wi-Fi");
		descriptionList.add("Markér for at påvirke Bluetooth");

		radioList.add("Max, Vibrator, Lydløs");
		// radioList.add("Til,Fra,false");
		radioList.add("Til,Fra,false");
		radioList.add("Til,Fra,false");
		radioList.add("Til,Fra,false");
		for (int i = 0; i < effectList.size(); i++) {
			enabledList.add(false);
		}

		effectsListView.setAdapter(new EffectsActivityAdaptor(effectList,
				descriptionList, enabledList, radioList,
				getApplicationContext()));

		effectsListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				final CheckedTextView myCheckedTextView = (CheckedTextView) view
						.findViewById(R.id.list_view_checked_text_view);
				/*
				 * final RadioButton radioButton1 = (RadioButton) view
				 * .findViewById(R.id.radioButton1); final RadioButton
				 * radioButton2 = (RadioButton) view
				 * .findViewById(R.id.radioButton2); final RadioButton
				 * radioButton3 = (RadioButton) view
				 * .findViewById(R.id.radioButton3);
				 */
				final RadioGroup radioGroup = (RadioGroup) view
						.findViewById(R.id.radioGroup);

				myCheckedTextView.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						/*
						 * myCheckedTextView.setChecked(!myCheckedTextView.isChecked
						 * ()); if(myCheckedTextView.isChecked()){
						 * radioGroup.setVisibility(View.VISIBLE); }else{
						 * radioGroup.setVisibility(View.GONE); }
						 */

					}
				});

				Log.v("", "item clicked");
				myCheckedTextView.toggle();
				if (myCheckedTextView.isChecked()) {
					radioGroup.setVisibility(View.VISIBLE);
				}
				enabledList.add(pos, !enabledList.get(pos));

			}
		});

	}

	private OnClickListener addConditionsListener = new OnClickListener() {

		public void onClick(View v) {
			Intent myIntent = new Intent(AddProfileMenuActivity.this,
					ConditionsMenuActivity.class);
			startActivityForResult(myIntent, 1);

		}
	};

	private OnClickListener addProfileListener = new OnClickListener() {

		public void onClick(View v) {
			EditText myEditText = (EditText) findViewById(R.id.editText1);
			Profil newProfile = new Profil(myEditText.getText().toString());
			for (int i = 0; i < effectList.size(); i++) {
				String temp = effectList.get(i);
				Log.v("", temp);

				if (temp.equals("Lyd") && enabledList.get(i)) {
					if (EffectsActivityAdaptor.radioList.get(i) == 0) {
					}
					// TODO: maxlyd
					if (EffectsActivityAdaptor.radioList.get(i) == 1) {
					}
					// TODO: vibrator
					if (EffectsActivityAdaptor.radioList.get(i) == 2) {
					}
					// TODO: ingen lyd
				}
				if (temp.equals("Flytilstand") && enabledList.get(i)) {
					if (EffectsActivityAdaptor.radioList.get(i) == 0)
						newProfile.setFlymode(true);
					if (EffectsActivityAdaptor.radioList.get(i) == 1)
						newProfile.setFlymode(false);
				}
				if (temp.equals("Wi-Fi") && enabledList.get(i)) {
					if (EffectsActivityAdaptor.radioList.get(i) == 0)
						newProfile.setWifi(true);
					if (EffectsActivityAdaptor.radioList.get(i) == 1)
						newProfile.setWifi(false);
				}
				if (temp.equals("Bluetooth") && enabledList.get(i)) {
					if (EffectsActivityAdaptor.radioList.get(i) == 0)
						newProfile.setBluetooth(true);
					if (EffectsActivityAdaptor.radioList.get(i) == 1)
						newProfile.setBluetooth(false);
					newProfile.setBluetooth(enabledList.get(i));
				}

			}

			addProfileToDataBase(newProfile);

			Toast.makeText(getApplicationContext(), "Tilføjet",
					Toast.LENGTH_LONG).show();
			finish();
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("", "onResult + code"+requestCode + " result "+resultCode);
		if (data != null) {
			try {
				Log.v("", "før alt");
				startTimeData = data.getStringExtra("startTime");
				if (startTimeData.endsWith(":0")) {
					startTimeData = startTimeData.replace(":0", ":00");
				}
				Log.v("", "efter1");
				stopTimeData = data.getStringExtra("stopTime");
				if (stopTimeData.endsWith(":0")) {
					stopTimeData = stopTimeData.replace(":0", ":00");
				}
				Log.v("", "efter2");
				/*
				addConditionsButtonText = (addConditionsButtonText + ": "
						+ startTimeData + " til " + stopTimeData);
				addConditions.setText(addConditionsButtonText + "");*/

				Toast.makeText(getApplicationContext(), "Betingelser tilføjet",
						Toast.LENGTH_SHORT).show();
				Log.v("", "efter3");
				activatedDays = data.getStringExtra("daysActivated");
				Log.e("", "Dage " + activatedDays);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				lat = data.getDoubleExtra("lat", 0);
				lng = data.getDoubleExtra("lng", 0);
				radius = data.getIntExtra("radius", 0);
				// addConditionsButtonText = (addConditionsButtonText + ", " +
				// locationData);
				addConditions.setText(addConditionsButtonText + " " + lng + ";"
						+ lat);
				Toast.makeText(getApplicationContext(), "Betingelser tilføjet",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
			}
		}

	}

	public void addProfileToDataBase(Profil profil) {
		if (radius != 0) {
			Log.v("", "ADDING LOCATION");
			Location cords = new Location("");
			cords.setLatitude(lat);
			cords.setLongitude(lng);
			profil.setBrugLocation(true);
			profil.setDistance(radius);
			profil.setLocation(cords);
		} else {
			profil.setBrugLocation(false);
		}
		Log.v("", "While adding days are = " +activatedDays);
		if (activatedDays.length() != 0) {
			profil.setBrugDag(true);
			profil.setDage(activatedDays);
		} else {
			profil.setDage("8");
			profil.setBrugDag(false);
		}
		if(startTimeData!=null&&startTimeData!=""){
			profil.setStartTid(startTimeData);
			profil.setSlutTid(stopTimeData);
			profil.setBrugTid(true);
		}
		ProfilesDatabaseConnection dbConnection;
		try {
			dbConnection = new ProfilesDatabaseConnection(
					getApplicationContext());
			dbConnection.addProfile(profil);
			dbConnection.close();
		} catch (Exception e) {
			Log.e("", "Database crashed -- profile NOT added!", e);
		}
	}

}