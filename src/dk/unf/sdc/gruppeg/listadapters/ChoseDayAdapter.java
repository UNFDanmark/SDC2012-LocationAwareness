package dk.unf.sdc.gruppeg.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import dk.unf.sdc.gruppeg.R;

public class ChoseDayAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;
	List<String> stringList = new ArrayList<String>();
	Context context;
	
	public ChoseDayAdapter(List<String>profiler, Context context) {
		mLayoutInflater = LayoutInflater.from(context);
		this.stringList = profiler;
		this.context = context;
	}

	public int getCount() {
		return stringList.size();
	}

	public Object getItem(int position) {
		return stringList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		CheckedTextView text;
		
		convertView = mLayoutInflater.inflate(R.layout.main_menu_list_view_entry, null);
		text = (CheckedTextView) convertView.findViewById(R.id.list_view_checked_text_view);
		
		text.setText(stringList.get(position));
		
		return convertView;
	}
	
}
