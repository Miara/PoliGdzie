package com.poligdzie.adapters;


import com.example.poligdzie.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] web;
	private final Integer[] imageId;
	public CustomListAdapter(Activity context,String[] web, Integer[] imageId) {
		super(context, R.layout.building_list_item, web);
		this.context = context;
		this.web = web;
		this.imageId = imageId;
		}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.building_list_item, null, true);
		Button txtTitle = (Button) rowView.findViewById(R.id.building_item_button);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.building_item_img);
		txtTitle.setText(web[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}
