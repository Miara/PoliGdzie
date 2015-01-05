package com.poligdzie.widgets;

import java.util.HashMap;

import com.poligdzie.interfaces.Nameable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;


//TODO: sprawdzic czy to potrzebne, jesli tak, to zrobic porzadek
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
		return ((Nameable) selectedItem).getName();
	}
}
