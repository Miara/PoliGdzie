package com.poligdzie.interfaces;


public interface Constants {

	// baza danych
	public String DATABASE_NAME = "Poligdzie.db";
	public int DATABASE_VERSION = 14;

	// Ustawienia aplikacji w PreferenceManager
	public String START_FIELD_VALUE = "";
	public String STOP_POINT_VALUE = "";
	public String SEARCH_POINT_VALUE = "";

	public String PROMPT_MODE = "";
	public String PROMPT_MODE_SEARCH = "Search button mode";
	public String PROMPT_MODE_START = "Start button mode";
	public String PROMPT_MODE_STOP = "Stop button mode";
	
	public String GOOGLE_MAP_MODE = "walking";
	
	public String MAP_MODE_OUTDOOR = "outdoor";
	public String MAP_MODE_INDOOR_FIRST = "indoor_first";
	public String MAP_MODE_INDOOR_LAST = "indoor_last";
	
    public int  INVALID_CODE = -1;
    public long DOUBLE_CLICK_DURATION = 600;
    public double VIEW_ZOOM_IN = 1.35;
    public String TAG = "Poligdzie";

	//tymczasowe sta�e marginesu do okienka po kliknieciu na marker
	public int MARGIN_LEFT = 55;
	public int MARGIN_TOP = 10;

}
