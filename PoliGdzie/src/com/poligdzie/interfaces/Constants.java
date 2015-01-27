package com.poligdzie.interfaces;

public interface Constants
{

	// baza danych
	public String	DATABASE_NAME			= "Poligdzie.db";

	public int		DATABASE_VERSION		= 69;

	// Ustawienia aplikacji w PreferenceManager
	public String	START_FIELD_VALUE			= "";
	public String	STOP_POINT_VALUE			= "";
	public String	SEARCH_POINT_VALUE			= "";

	public String	PROMPT_MODE					= "";
	public String	PROMPT_MODE_SEARCH			= "Search button mode";
	public String	PROMPT_MODE_START			= "Start button mode";
	public String	PROMPT_MODE_STOP			= "Stop button mode";

	public String	GOOGLE_MAP_MODE				= "walking";

	public String	OUTDOOR_MAP_TAG				= "outdoor";

	public int		INVALID_CODE				= -1;
	public long		DOUBLE_CLICK_DURATION		= 600;
	public double	VIEW_ZOOM_IN				= 1.35;
	public String	TAG							= "Poligdzie";

	// tymczasowe sta³e marginesu do okienka po kliknieciu na marker
	public int		MARGIN_LEFT					= 65;
	public int		MARGIN_TOP					= 20;

	// do braku rysowania bitmapy

	public String	NO_BITMAP				= "0";
	
	
	//TODO: zastapic publicznym enumem
	public int CSV_BUILDING = 1;
	public int CSV_FLOOR = 2;
	public int CSV_NAVIGATION_POINT = 3;
	public int CSV_BUILDING_ENTRY = 4;
	public int CSV_NAVIGATION_CONNECTION = 5;
	public int CSV_SPECIAL_CONNECTION = 6;
	public int CSV_ROOM = 7;
	public int CSV_UNIT = 8;
	
	public int INDOOR_MODE_FIRST = 1;
	public int INDOOR_MODE_LAST = 2;
	
	public int ENABLE_SWITCHING_FRAGMENT = 1;
	public int DISABLE_SWITCHING_FRAGMENT = 2;
	
	
	public int SPECIAL_CONNECTION_LENGTH = 15;
	
	public int ERROR_CODE = -1;
	
	public int NEW_NAVIGATION_POINT_ID = 90000;
	
	public int INTEGER_MAX_VALUE = 999999;
	


}
