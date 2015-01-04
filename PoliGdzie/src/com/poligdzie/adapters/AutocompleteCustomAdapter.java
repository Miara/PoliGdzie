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
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.Imageable;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;



public class AutocompleteCustomAdapter extends ArrayAdapter implements WithDrawableId{

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
		    View view = inflater.inflate(R.layout.prompt_item, parent, false);
		    
		    TextView name = (TextView) view.findViewById(R.id.autocomplete_name);
		    TextView desc = (TextView) view.findViewById(R.id.autocomplete_description);
		    ImageView icon = (ImageView) view.findViewById(R.id.autocomplete_icon);
		    
		    Object object = new Object();
		    
		    object = objects.get(position);
		    
		    name.setText(((Nameable) object).getName());
		    int imgRes;
		    	
		    if(object instanceof Building) {
			    desc.setText("Budynek");
			    imgRes = getDrawableId(((Imageable) object).getImageResource(),context);
			    icon.setImageResource(imgRes);
			}
		    
		    if(object instanceof Unit) {
		        desc.setText("Jednostka organizacyjna");
		        imgRes = getDrawableId(((Unit) object).getBuilding().getImageResource(),context);
		        icon.setImageResource(imgRes);
			}
		    
		    if(object instanceof Room) {
		    	desc.setText("Pomieszczenie");
		    	imgRes = getDrawableId(((Room) object).getBuilding().getImageResource(),context);
		    	icon.setImageResource(imgRes);
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

	@Override
	public int getDrawableId(String name, Context context) {
		int resId =context.getResources().getIdentifier(name, "drawable", context.getPackageName());
		return resId;
	}
	



	

}
