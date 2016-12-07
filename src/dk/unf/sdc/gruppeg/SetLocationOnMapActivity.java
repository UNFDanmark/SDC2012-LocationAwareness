package dk.unf.sdc.gruppeg;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import dk.unf.sdc.gruppeg.listadapters.CustomPinpoint;

public class SetLocationOnMapActivity extends MapActivity {

	MapView map;
	long start;
	long stop;
	MyLocationOverlay myLocationOverlay;
	MapController controller;
	static CustomPinpoint myCustomPinpoint;
	List<Overlay> overlayList;
	int x, y;
	TextView myTextView;
	SeekBar mySeeker;
	GeoPoint touchedPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_location_layout);
		map = (MapView) findViewById(R.id.mapView);
		map.setBuiltInZoomControls(true);

		Button myButton = (Button) findViewById(R.id.sendDataBackButton);
		Drawable myDrawable = getResources().getDrawable(R.drawable.notofication_icon);
		myCustomPinpoint = new CustomPinpoint(myDrawable, getApplicationContext());
		myButton.setOnClickListener(sendData);
		
		mySeeker = (SeekBar) findViewById(R.id.seekBarRadius);
		mySeeker.setOnSeekBarChangeListener(mySeekListner);
		myTextView = (TextView) findViewById(R.id.show_progress_text_view);
		Touchy t = new Touchy();
		myLocationOverlay = new MyLocationOverlay(
				SetLocationOnMapActivity.this, map);
		myLocationOverlay.enableMyLocation();
		overlayList = map.getOverlays();
		overlayList.add(t);
		overlayList.add(myLocationOverlay);
		
		controller = map.getController();
		
		
	}
	
	public void addItemToOverLay(GeoPoint myGeoPoint){
		OverlayItem item = new OverlayItem(myGeoPoint, "", "");
		myCustomPinpoint.insertPinpoint(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			Drawable myDrawable = getResources().getDrawable(R.drawable.notofication_icon);
			myCustomPinpoint = new CustomPinpoint(myDrawable, getApplicationContext());

			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				
				overlayList.clear();
				touchedPoint = map.getProjection().fromPixels(x, y);
				OverlayItem item = new OverlayItem(touchedPoint, "", "");
				myCustomPinpoint.insertPinpoint(item);
				map.invalidate();
				overlayList.add(new Touchy());
				overlayList.add(myCustomPinpoint);
				myLocationOverlay.enableMyLocation();
				overlayList.add(myLocationOverlay);
				
			}

			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}

			if (stop - start > 1500) {
				AlertDialog alert = new AlertDialog.Builder(
						SetLocationOnMapActivity.this).create();
				alert.setButton("Vælg punkt",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								Log.v("",
										"" + touchedPoint.getLatitudeE6() / 1E6
												+ "; "
												+ touchedPoint.getLongitudeE6()
												/ 1E6);

							}
						});
				alert.show();
				return true;
			}

			return false;
		}
	}

	@Override
	protected void onDestroy() {
		myLocationOverlay.disableMyLocation();
		super.onDestroy();
	}

	private OnClickListener setLocationDoneListener = new OnClickListener() {

		public void onClick(View v) {
			Intent locationSet = new Intent();
			locationSet.putExtra("lat",
					touchedPoint.getLatitudeE6() / 1E6);
			locationSet.putExtra("lng",
					touchedPoint.getLongitudeE6() / 1E6);
			locationSet.putExtra("radius", getRadius());
			setResult(RESULT_OK, locationSet);
			finish();
		}
	};
	public void sendDataBack(){
		Intent locationSet = new Intent();
		locationSet.putExtra("lat",
				touchedPoint.getLatitudeE6() / 1E6);
		locationSet.putExtra("lng",
				touchedPoint.getLongitudeE6() / 1E6);
		locationSet.putExtra("radius", getRadius());
		setResult(RESULT_OK, locationSet);
		finish();
	}
	private OnClickListener sendData = new OnClickListener() {
		
		public void onClick(View v) {
			if(touchedPoint!=null)
				sendDataBack();
		}
	};
	
	private OnSeekBarChangeListener mySeekListner = new OnSeekBarChangeListener() {

		public void onStopTrackingTouch(SeekBar seekBar) {
			myTextView.setText("Radius: " + getRadius() + " meter");
			Log.v("", getRadius()+"");
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}
	};

	private int getRadius() {

		int radius = mySeeker.getProgress();
		if (radius < 50) {
			radius = 0;
		}
		return radius;
	}

}
