package com.poligdzie.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poligdzie.R;
import com.poligdzie.activities.BuildingInfoActivity;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.persistence.Building;

public class SearchBuildingsFragment extends PoliGdzieBaseFragment implements
		OnClickListener, WithDrawableId {

	private List<Building> buildings;
	ListView list;
	List<String> names = new ArrayList<String>();
	List<Integer> images = new ArrayList<Integer>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_buildings_fragment,
				container, false); 
		
		try {
			buildings = dbHelper.getBuildingDao().queryForAll();
			for(Building b : buildings)
			{
				names.add(b.getName()); 
				int resId = getDrawableId(b.getImageResource(), getActivity());
				images.add(resId); 
			}
			CustomListAdapter adapter = new CustomListAdapter(getActivity(), names, images);
		    list=(ListView)rootView.findViewById(R.id.building_list_view);
		    Log.i("POLI", "T3");
		    list.setAdapter(adapter);
		    list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		    {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) 
                {
                	Toast.makeText(getActivity(),"You clicked:"+names.get(position), 
                            Toast.LENGTH_SHORT).show();
                	Intent intent = new Intent(getActivity(), BuildingInfoActivity.class);
        			startActivity(intent);
                }
            });
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private class CustomListAdapter extends ArrayAdapter<String>{
		private final Activity context;
		private final List<String> buildingNames;
		private final List<Integer> images;
		
		public CustomListAdapter(Activity context,List<String> mNames, List<Integer> mImages) 
		{
			super(context, R.layout.building_list_item, mNames);
			this.context = context;
			this.buildingNames = mNames;
			this.images = mImages;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) 
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.building_list_item, null, true);
			
			TextView txtTitle = (TextView) rowView.findViewById(R.id.building_item_name);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.building_item_image);
			txtTitle.setText(buildingNames.get(position));
			imageView.setImageResource(images.get(position));
			return rowView;
		}
	}

	@Override
	public int getDrawableId(String name, Context context) 
	{
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}

	public SearchBuildingsFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
}
