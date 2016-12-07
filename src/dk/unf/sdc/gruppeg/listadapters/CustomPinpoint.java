package dk.unf.sdc.gruppeg.listadapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomPinpoint extends ItemizedOverlay<OverlayItem>{
	private static final String TAG = "MyActivity";
	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context c; 
	
	public CustomPinpoint(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public CustomPinpoint(Drawable m, Context parsedContext) {
		this(m);
		c = parsedContext; 
	}

	@Override
	protected OverlayItem createItem(int i) {
		return pinpoints.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return pinpoints.size();
	}
	
	public void insertPinpoint(OverlayItem item){
		String title = item.getTitle();
		String snippet = item.getSnippet();
		pinpoints.add(item);
		this.populate();
	}
	


}
