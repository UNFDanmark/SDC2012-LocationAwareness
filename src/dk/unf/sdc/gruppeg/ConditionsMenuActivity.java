package dk.unf.sdc.gruppeg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import dk.unf.sdc.gruppeg.listadapters.ChoseDayAdapter;

public class ConditionsMenuActivity extends Activity {

	private CheckedTextView timeCheck;
	private Button startTime;
	private Button stopTime;
	private Button dayPick;
	private String activitaedDays = "";
	private Button timeLocationSetDoneButton;
	private CheckedTextView locationCheck;
	final int TIME_DIALOG_ID_START = 0;
	final int TIME_DIALOG_ID_STOP = 1;
	final int DAY_DIALOG = 2;
	private double latitudeData;
	private double longtitudeData;
	private int radius = 0;
	private List<Boolean> daysChosen = new ArrayList<Boolean>();

	private ListView dayListView;
	private String daysPicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_conditions_layout);

		timeCheck = (CheckedTextView) findViewById(R.id.timeCheckedTextView);
		locationCheck = (CheckedTextView) findViewById(R.id.locationCheckedTextView);
		startTime = (Button) findViewById(R.id.startTimePicker);
		stopTime = (Button) findViewById(R.id.stopTimePicker);
		dayPick = (Button) findViewById(R.id.dayPicker);
		timeLocationSetDoneButton = (Button) findViewById(R.id.timeLocationSetDone);

		startTime.setOnClickListener(startTimeListener);
		stopTime.setOnClickListener(stopTimeListener);
		dayPick.setOnClickListener(dayPickListener);
		timeLocationSetDoneButton.setOnClickListener(timeLocationSetDone);
		timeCheck.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				timeCheck.toggle();
				if (timeCheck.isChecked()) {
					startTime.setVisibility(View.VISIBLE);
					stopTime.setVisibility(View.VISIBLE);
					dayPick.setVisibility(View.VISIBLE);
				} else {
					startTime.setVisibility(View.GONE);
					stopTime.setVisibility(View.GONE);
					dayPick.setVisibility(View.GONE);
				}
			}
		});

		locationCheck.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				locationCheck.toggle();
				if (locationCheck.isChecked()) {
					Intent intent = new Intent(ConditionsMenuActivity.this,
							SetLocationOnMapActivity.class);
					startActivityForResult(intent, 0);
				}
			}
		});

	}

	private OnClickListener startTimeListener = new OnClickListener() {

		public void onClick(View v) {
			showDialog(TIME_DIALOG_ID_START);

		}
	};

	private OnClickListener stopTimeListener = new OnClickListener() {

		public void onClick(View v) {
			showDialog(TIME_DIALOG_ID_STOP);

		}
	};

	private OnClickListener dayPickListener = new OnClickListener() {

		public void onClick(View v) {
			showDialog(DAY_DIALOG);

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		final Calendar cal = Calendar.getInstance();
		int pHour, pMinute;

		switch (id) {
		case TIME_DIALOG_ID_START:

			pHour = cal.get(Calendar.HOUR_OF_DAY);
			pMinute = cal.get(Calendar.MINUTE);
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListenerStart, pHour,
					pMinute, true);
		case TIME_DIALOG_ID_STOP:
			pHour = cal.get(Calendar.HOUR_OF_DAY);
			pMinute = cal.get(Calendar.MINUTE);
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListenerStop, pHour,
					pMinute, true);
		case DAY_DIALOG:
			// set up dialog
			List<String> daysInWeek = new ArrayList<String>();

			daysInWeek.add("Søndag");
			daysInWeek.add("Mandag");
			daysInWeek.add("Tirsdag");
			daysInWeek.add("Onsdag");
			daysInWeek.add("Torsdag");
			daysInWeek.add("Fredag");
			daysInWeek.add("Lørdag");
			

			AlertDialog.Builder builder;
			AlertDialog alertDialog;

			for (int i = 0; i < daysInWeek.size(); i++) {
				daysChosen.add(false);
			}

			Context mContext = ConditionsMenuActivity.this;
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.day_picker_custom_dialog,
					(ViewGroup) findViewById(R.id.dayPickerCustomDialog));
			dayListView = (ListView) layout
					.findViewById(R.id.dayPickingListView);

			dayListView.setAdapter(new ChoseDayAdapter(daysInWeek,
					ConditionsMenuActivity.this));
			dayListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long arg3) {
					final CheckedTextView myCheckedTextView = (CheckedTextView) view
							.findViewById(R.id.list_view_checked_text_view);
					myCheckedTextView.toggle();
					if (myCheckedTextView.isChecked()) {
						daysChosen.set(pos, myCheckedTextView.isChecked());
					} else {
						daysChosen.set(pos, myCheckedTextView.isChecked());
					}
				}
			});

			// set up button
			Button button = (Button) layout
					.findViewById(R.id.dayPickerSaveButton);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String finalString = "";
					for (int i = 0; i < daysChosen.size(); i++) {
						if (daysChosen.get(i)) {

							finalString += "," + i;

						}
					}
					if (finalString.startsWith(",")) {
						finalString = finalString.replaceFirst(",", "");
					}
					activitaedDays = finalString;
					Log.v("",activitaedDays);
					dismissDialog(DAY_DIALOG);
				}

			});
			builder = new AlertDialog.Builder(mContext);
			builder.setView(layout);
			alertDialog = builder.create();

			return alertDialog;

		}
		;
		return null;

	}

	private String timeStart;
	private String timeStop;

	private TimePickerDialog.OnTimeSetListener timePickerListenerStart = new TimePickerDialog.OnTimeSetListener() {
		String hourStart;
		String minuteStart;

		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			if (selectedHour < 10) {
				hourStart = "0" + selectedHour;
			} else {
				hourStart = "" + selectedHour;
			}

			if (selectedMinute < 10) {
				minuteStart = "0" + selectedMinute;
			} else {
				minuteStart = "" + selectedMinute;
			}

			timeStart = hourStart + ":" + minuteStart;

			Toast.makeText(getApplicationContext(), "Starttiden er sat.",
					Toast.LENGTH_SHORT).show();
			startTime.setText("Starttid: " + hourStart + ":" + minuteStart);

		}
	};

	private TimePickerDialog.OnTimeSetListener timePickerListenerStop = new TimePickerDialog.OnTimeSetListener() {
		String hourStop;
		String minuteStop;

		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			if (selectedHour < 10) {
				hourStop = "0" + selectedHour;
			} else {
				hourStop = "" + selectedHour;
			}

			if (selectedMinute < 10) {
				minuteStop = "0" + selectedMinute;
			} else {
				minuteStop = "" + selectedMinute;
			}

			timeStop = hourStop + ":" + minuteStop;

			Toast.makeText(getApplicationContext(), "Sluttiden er sat.",
					Toast.LENGTH_SHORT).show();
			stopTime.setText("Sluttid: " + hourStop + ":" + minuteStop);

		}
	};

	private OnClickListener timeLocationSetDone = new OnClickListener() {

		public void onClick(View v) {
			Intent timeSet = new Intent();
			if (timeCheck.isChecked()) {
				timeSet.putExtra("daysActivated", activitaedDays);
				Log.e("","Dage "+activitaedDays);
				timeSet.putExtra("startTime", timeStart);
				timeSet.putExtra("stopTime", timeStop);
				timeSet.putExtra("useTime", "1");
				Log.e("","Tider "+timeStart+"-"+timeStop);
			}
			if (locationCheck.isChecked()) {
				timeSet.putExtra("lat", latitudeData);
				timeSet.putExtra("lng", longtitudeData);
				timeSet.putExtra("radius", radius);
			}
			setResult(RESULT_OK, timeSet);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		latitudeData = data.getDoubleExtra("lat", latitudeData);
		longtitudeData = data.getDoubleExtra("lng", longtitudeData);
		radius = data.getIntExtra("radius", 0);
		Log.e("", radius + "Radius er");
		Toast.makeText(getApplicationContext(), "Lokation tilføjet",
				Toast.LENGTH_SHORT).show();
		Log.v("", latitudeData + ", " + longtitudeData);

	}

}
