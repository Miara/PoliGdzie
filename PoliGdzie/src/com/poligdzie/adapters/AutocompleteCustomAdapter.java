package com.poligdzie.adapters;

import java.util.HashMap;
import java.util.List;

import com.example.poligdzie.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;



public class AutocompleteCustomAdapter extends ArrayAdapter {

	private Context context;
	private List <HashMap <String, String>> objects;
	private int position;
	
	public AutocompleteCustomAdapter(Context context, int textViewResourceId,
			List <HashMap <String, String>> objects) {
		
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i("POLIGDZIE", "---------");
		Log.i("POLIGDZIE", this.objects.get(position).get("name"));
		Log.i("POLIGDZIE", "---------");
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflater.inflate(R.layout.position_prompt, parent, false);
		    
		    TextView name = (TextView) view.findViewById(R.id.autocomplete_name);
		    TextView desc = (TextView) view.findViewById(R.id.autocomplete_description);
		    ImageView icon = (ImageView) view.findViewById(R.id.autocomplete_icon);
		    
		    name.setText(objects.get(position).get("name"));
		    desc.setText(objects.get(position).get("description"));
		    icon.setImageResource(Integer.parseInt(objects.get(position).get("icon")));
		    
		    // change the icon for Windows and iPhone
		    
		    

		return view;
		
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				
				return new FilterResults();
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				AutocompleteCustomAdapter.this.notifyDataSetChanged();
			}
			
		};
	}


}
