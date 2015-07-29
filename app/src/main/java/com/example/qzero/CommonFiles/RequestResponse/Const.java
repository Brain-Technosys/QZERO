package com.example.qzero.CommonFiles.RequestResponse;

public class Const {

    //Initialize base url
    public static String BASE_URL = "http://qzero.etrueconcept.com/API/";

    //Initialize app key
    public static String APP_KEY = "OAI9/IAkW/jA4u+zosKrBKTcqDP9DHM9MtCEvaLZPSg=";

    //Initialize time out
    public static int TIME_OUT = 60000;

    //Intilialize urls for response
    public static String LOGIN_URL = "Account/Login";
    public static String REGISTER_URL = "Account/Register";
    public static String SEARCH_VENUE = "searchvenue";
    public static String SEARCH_OUTLET = "searchoutlet";
    public static String SEARCH_ITEM = "searchitem";
    public static String GET_OUTLETS = "venue/getoutlets";
    public static String GET_ITEMS="venue/getitems/";


    //Intialize jsonArray parameter

    public static String TAG_JsonObj = "result";
    public static String TAG_JsonItemObj="items";



    //Initialze search venue variables

    public static String TAG_VENUE_ID = "venueId";
    public static String TAG_VENUE_NAME = "venueName";
    public static String TAG_DOMAIN = "venueDomain";
    public static String TAG_PHONE_NO = "phoneNo";
    public static String TAG_MOB_N0 = "mobileNo";
    public static String TAG_CITY = "city";
    public static String TAG_ADDRESS = "address";
    public static String TAG_ZIP = "zipcode";
    public static String TAG_LOC = "location";

    //Initialize search outlet variables
    public static String TAG_OUTLET_NAME = "outletName";

    //Initialize search item variables
    public static String TAG_ITEM_ID = "itemId";
    public static String TAG_ITEM_NAME = "itemName";

    //Initialze outlet activity variables
    public static String TAG_OUTLET_ID="outletId";
    public static String TAG_NAME="name";
    public static String TAG_OUTLET_ACTIVE="isActive";

    //Initialize item variables
    public static String TAG_PRICE="price";
    public static String TAG_DESC="description";
    public static String TAG_IMAGE="image";
    public static String TAG_SUB_ID="itemId";





}
