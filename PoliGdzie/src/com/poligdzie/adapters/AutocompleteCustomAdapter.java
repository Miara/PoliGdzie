package com.poligdzie.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Imageable;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;



public class AutocompleteCustomAdapter extends ArrayAdapter {

	private Context context;
	private List <Object> objects;
	private int position;
	
	public AutocompleteCustomAdapter(Context context, int textViewResourceId,
			List <Object> objects) {
		
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflater.inflate(R.layout.position_prompt, parent, false);
		    
		    TextView name = (TextView) view.findViewById(R.id.autocomplete_name);
		    TextView desc = (TextView) view.findViewById(R.id.autocomplete_description);
		    ImageView icon = (ImageView) view.findViewById(R.id.autocomplete_icon);
		    
		    Object object = new Object();
		    
		    object = objects.get(position);
		    
		    name.setText(((Nameable) object).getName());
		    
		    	
		    if(object instanceof Building) {
			    desc.setText("Budynek");
			    icon.setImageResource(((Imageable) object).getImageResource());
			}
		    
		    if(object instanceof Unit) {
		        desc.setText("Jednostka organizacyjna");
		        icon.setImageResource(((Unit) object).getBuilding().getImageResource());
			}
		    
		    if(object instanceof Room) {
		    	desc.setText("Pomieszczenie");
		    	icon.setImageResource(((Room) object).getBuilding().getImageResource());
			}
		    
		    
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
