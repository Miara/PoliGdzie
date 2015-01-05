package com.poligdzie.helpers;

import java.util.HashMap;

import com.poligdzie.fragments.PoliGdzieMapFragment;

public class FragmentMapHelper {
	private HashMap<String, PoliGdzieMapFragment> fragments;
		
	
	
	public PoliGdzieMapFragment getFragmentByTag(String tag) {
		return fragments.get(tag);
	}
	
	public void addFragment(String key, PoliGdzieMapFragment value) {
		fragments.put(key, value);
	}

	public FragmentMapHelper() {
		fragments = new HashMap<String, PoliGdzieMapFragment>();
	}


	public int getSize() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

	public Object getFragments() {
		// TODO Auto-generated method stub
		return fragments;
	}

	public void clear() {
		// TODO Auto-generated method stub
		fragments.clear();
		
	}

	
}
