package com.poligdzie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.poligdzie.interfaces.Nameable;

//TODO: sprawdzic czy to potrzebne

public class SearchAutoCompleteTextView extends AutoCompleteTextView
{

	public SearchAutoCompleteTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected CharSequence convertSelectionToString(Object selectedItem)
	{
		return ((Nameable) selectedItem).getName();
	}
}
