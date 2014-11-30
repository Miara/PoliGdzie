package com.poligdzie.widgets;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/** Customizing AutoCompleteTextView to return Country Name   
 *  corresponding to the selected item
 */
public class SearchAutoCompleteTextView extends AutoCompleteTextView {
	
	public SearchAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** Returns the country name corresponding to the selected item */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		HashMap<String, String> item = (HashMap<String, String>) selectedItem;
		return item.get("name");
	}
}
