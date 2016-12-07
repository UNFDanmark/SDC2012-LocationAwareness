package dk.unf.sdc.gruppeg.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import dk.unf.sdc.gruppeg.R;

public class EffectsActivityAdaptor extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;
	List<String> list;
	List<String> descriptionList;
	List<String> radioNames;
	List<Boolean> enabeledList = new ArrayList<Boolean>();
	public static List<Integer> radioList;
	Context context;
	
	public EffectsActivityAdaptor(List<String>input, List<String> description,List<Boolean> enabeldState, List<String> radioNames,Context context) {
		mLayoutInflater = LayoutInflater.from(context);
		radioList = new ArrayList<Integer>();
		this.enabeledList = enabeldState;
		this.radioNames = radioNames;
		this.descriptionList = description;
		this.list = input;
		this.context = context;
		for(int i =0;i<enabeledList.size(); i++){
			radioList.add(0);
		}
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		String navn = list.get(position);
		TextView myDescriptionTextView; 
		String[] radioNamesArray = radioNames.get(position).split(",");
		
		convertView = mLayoutInflater.inflate(R.layout.effects_list_view_entry, null);
		final CheckedTextView text = (CheckedTextView) convertView.findViewById(R.id.list_view_checked_text_view);
		final RadioButton radioButton1 = (RadioButton) convertView.findViewById(R.id.radioButton1);
		final RadioButton radioButton2 = (RadioButton) convertView.findViewById(R.id.radioButton2);
		final RadioButton radioButton3 = (RadioButton) convertView.findViewById(R.id.radioButton3);
		myDescriptionTextView = (TextView) convertView.findViewById(R.id.list_view_description_text_view);
		final RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
		
		text.setChecked(enabeledList.get(position));
		
		text.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				enabeledList.set(position, !text.isChecked());
				text.setChecked(!text.isChecked());
				Log.v("", ""+text.isChecked());
	
				if(enabeledList.get(position)){
					radioGroup.setVisibility(View.VISIBLE);
				}else{
					radioGroup.setVisibility(View.GONE);
				}
				
			}
		});
		if(radioList.get(position)==0){
			radioButton1.setChecked(false);
			radioButton2.setChecked(false);
			radioButton3.setChecked(false);
		}
		if(radioList.get(position)==1){
			radioButton1.setChecked(true);
			radioButton2.setChecked(false);
			radioButton3.setChecked(false);
		}
		if(radioList.get(position)==2){
			radioButton1.setChecked(false);
			radioButton2.setChecked(true);
			radioButton3.setChecked(false);
		}
		if(radioList.get(position)==3){
			radioButton1.setChecked(false);
			radioButton2.setChecked(false);
			radioButton3.setChecked(true);
		}
		
		if(radioNamesArray[0].equals("false")){
			radioButton1.setVisibility(View.GONE);
		}else{
			radioButton1.setText(radioNamesArray[0]);
		}
		
		if(radioNamesArray[1].equals("false")){
			radioButton2.setVisibility(View.GONE);
		}else{
			radioButton2.setText(radioNamesArray[1]);
		}
		
		if(radioNamesArray[2].equals("false")){
			radioButton3.setVisibility(View.GONE);
		}else{
			radioButton3.setText(radioNamesArray[2]);
		}
		
		if(enabeledList.get(position)){
			radioGroup.setVisibility(View.VISIBLE);
		}else{
			radioGroup.setVisibility(View.GONE);
		}
		
		text.setText(navn);
		myDescriptionTextView.setText(descriptionList.get(position));
		
		radioButton1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				radioList.add(position,1);
				
			}
		});
		radioButton2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				radioList.add(position,2);
			}
		});
		radioButton3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				radioList.add(position,3);
			}
		});
		return convertView;
	}
	

	
}