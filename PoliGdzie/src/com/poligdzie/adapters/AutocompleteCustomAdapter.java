package com.poligdzie.adapters;

import java.sql.SQLException;
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
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Imageable;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class AutocompleteCustomAdapter extends ArrayAdapter implements
															WithDrawableId
{

	private Context			context;
	private List<Object>	objects;
	private int				position;
	private DatabaseHelper	dbHelper;

	public AutocompleteCustomAdapter(DatabaseHelper dbHelper, Context context,
			int textViewResourceId, List<Object> objects)
	{
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		this.dbHelper = dbHelper;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.prompt_item, parent, false);

		TextView name = (TextView) view.findViewById(R.id.autocomplete_name);
		TextView desc = (TextView) view
				.findViewById(R.id.autocomplete_description);
		ImageView icon = (ImageView) view.findViewById(R.id.autocomplete_icon);

		Object object = new Object();

		object = objects.get(position);

		name.setText(((Nameable) object).getName());
		int imgRes;

		if (object instanceof Building)
		{
			desc.setText("Budynek");
			imgRes = getDrawableId(((Imageable) object).getImageResource(),
					context);
			icon.setImageResource(imgRes);
		}

		if (object instanceof Unit)
		{

			Unit unit = (Unit) object;
			Building building = new Building();
			try
			{
				building = dbHelper.getBuildingDao().queryForId(
						unit.getBuilding().getId());

			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			desc.setText("Jednostka organizacyjna");
			imgRes = getDrawableId(building.getImageResource(), context);
			icon.setImageResource(imgRes);
		}

		if (object instanceof Room)
		{
			Room room = (Room) object;
			Building building = new Building();
			try
			{
				building = dbHelper.getBuildingDao().queryForId(
						room.getBuilding().getId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			desc.setText("Pomieszczenie");
			imgRes = getDrawableId(building.getImageResource(), context);
			icon.setImageResource(imgRes);
		}

		return view;

	}

	@Override
	public Filter getFilter()
	{
		return new Filter()
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				return new FilterResults();
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results)
			{
				AutocompleteCustomAdapter.this.notifyDataSetChanged();
			}

		};
	}

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}

}
