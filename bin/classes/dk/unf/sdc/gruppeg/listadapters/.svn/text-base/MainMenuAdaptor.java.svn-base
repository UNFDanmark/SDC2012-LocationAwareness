package dk.unf.sdc.gruppeg.listadapters;

import java.util.ArrayList;
import java.util.List;

import dk.unf.sdc.gruppeg.Profil;
import dk.unf.sdc.gruppeg.R;
import dk.unf.sdc.gruppeg.R.id;
import dk.unf.sdc.gruppeg.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class MainMenuAdaptor extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;
	List<Profil> profileList = new ArrayList<Profil>();
	Context context;
	
	public MainMenuAdaptor(List<Profil>profiler, Context context) {
		mLayoutInflater = LayoutInflater.from(context);
		this.profileList = profiler;
		this.context = context;
	}

	public int getCount() {
		return profileList.size();
	}

	public Object getItem(int position) {
		return profileList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Profil profile = profileList.get(position);
		CheckedTextView text;
		
		convertView = mLayoutInflater.inflate(R.layout.main_menu_list_view_entry, null);
		text = (CheckedTextView) convertView.findViewById(R.id.list_view_checked_text_view);
		
		text.setText(profile.getName());
		text.setChecked(profile.getActive());
		
		return convertView;
	}
	
}
