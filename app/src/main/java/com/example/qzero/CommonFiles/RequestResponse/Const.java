package com.example.qzero.CommonFiles.RequestResponse;

public class Const {

    //Initialize base url
    public static String BASE_URL = "http://qzero.etrueconcept.com/API/";

    //Initialize app key
    public static String APP_KEY = "OAI9/IAkW/jA4u+zosKrBKTcqDP9DHM9MtCEvaLZPSg=";

    //Initialize image url
    public static String IMAGE_URL = "Venue/GetItemImage/";

    //Initialize advertisemnent image url
    public static String AD_IMAGE_URL = "Venue/GetAdvertisementImage/";

    //Initialize time out
    public static int TIME_OUT = 60000;

    //Intilialize urls for response
    public static String LOGIN_URL = "Account/Login";
    public static String REGISTER_URL = "Account/Register";
    public static String SEARCH_VENUE = "searchvenue";
    public static String SEARCH_OUTLET = "searchoutlet";
    public static String SEARCH_ITEM = "searchitem";
    public static String GET_OUTLETS = "venue/getoutlets";
    public static String GET_ITEMS = "venue/getitems/";
    public static String DASHBOARD_URL = "Dashboard";
    public static String PROFILE_INFO_URL = "ProfileInfo";
    public static String CHANGE_PASSWORD_URL = "Account/ChangePassword";
    public static String UPDATE_PROFILE_URL = "ProfileInfo";
    public static String GET_ORDER_URL = "Order";
    public static String GET_ORDER_ITEM = "Order/OrderItems";
    public static String FORGOT_PASSWORD = "Account/ForgotPassword";
    public static String GET_ITEM_DETAIL = "venue/getitemdetail";
    public static String POST_CHECKOUT = "checkout/PostCheckout";
    public static String GET_CHKOUT_DETAIL = "checkout/GetCheckoutDetails?Outletid=";
    public static String ADD_EDIT_BILL_ADDRESS="checkout/AddEditBillingAddress?";
    public static String GET_COUNTRY="checkout/GetStateByCountryId";
    public static String POST_SHIPPING_ADD="checkout/AddEditShippingAddress";
    public static String POST_BILLING_ADD="checkout/AddEditBillingAddress";
    public static String DELETE_ADDRESS="checkout/DeleteAddress";
    public static String POST_FINAL_PAYMENT="checkout/FinalPaymentOrder";
    public static String GET_DELIVERY_TYPE="checkout/GetDeliveryType?";
    public static String GET_PAYMENTGATEWAY_DETAILS="checkout/PaymentGatewayDetails?VenueId=";
    public static String POST_REGISTER_DEVICE="checkout/DeviceRegisteration";
    public static String GET_REGISTER_DEVICE="checkout/getDeviceRegistered";
    public static String POST_DEVICE_LOGIN_LOGOUT="checkout/DeviceLoginLogout";
    public static String GET_VENUE_ADVERTISEMENT="Venue/GetVenueAdvertisements/";

    //Intialize jsonArray parameter

    public static String TAG_JsonObj = "result";

    public static String TAG_JsonItemObj = "items";
    public static String TAG_JsonOutletObj = "outlets";
    public static String TAG_JsonCatObj = "categories";
    public static String TAG_JsonSubCatObj = "subCategoryModels";
    public static String TAG_JsonDetailObj = "itemDetails";
    public static String TAG_JsonChoiceObj = "choiceGroups";
    public static String TAG_JsonModObj = "modifiers";
    public static String TAG_MODIFIER_ID = "modifierId";
    public static String TAG_JsonState = "state";
    public static String TAG_JsonAd = "advertisements";


    //Initialize message and status parameter

    public static String TAG_STATUS = "status";
    public static String TAG_MESSAGE = "message";
    public static String TAG_RESULT = "result";


    //Initialize dashboard variables

    public static String TAG_WALLET_AMOUNT = "walletAmount";
    public static String TAG_CLUB_COUNT = "clubsCount";
    public static String TAG_ORDER_COUNT = "inProcessOrdersCount";

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
    public static String TAG_OUTLET_ID = "outletId";
    public static String TAG_NAME = "name";
    public static String TAG_OUTLET_DESC = "outletHeading";
    public static String TAG_PH_NUM = "phoneNumber";
    public static String TAG_MOB_NUM = "mobileNumber";
    public static String TAG_OUTLET_ACTIVE = "isActive";
    public static String TAG_OUTLET_IMAGE = "imageUrl";

    //Initialize item variables
    public static String TAG_CAT_ITEM_NAME = "name";
    public static String TAG_PRICE = "price";
    public static String TAG_DESC = "description";
    public static String TAG_IMAGE = "image";
    public static String TAG_SUB_ID = "itemId";

    //Initialize categories variables
    public static String TAG_CAT_ID = "categoryId";
    public static String TAG_CAT_NAME = "name";

    //Initialize sub categories variables
    public static String TAG_SUB_CAT_ID = "subCategoryId";
    public static String TAG_SUB_CAT_NAME = "name";


    //Initialize profile variables
    public static String TAG_STATE = "state";
    public static String TAG_COUNTRY = "country";
    public static String TAG_PIN_CODE = "pinCode";
    public static String TAG_USER_NAME = "userName";
    public static String TAG_FIRST_NAME = "firstName";
    public static String TAG_LAST_NAME = "lastName";
    public static String TAG_EMAIL = "email";
    public static String TAG_CREATED_ON = "createdDate";
    public static String TAG_UPDATED_ON = "updatedDate";
    public static String TAG_IS_ACTIVE = "isActive";
    public static String TAG_PHONE = "phoneNumber";
    public static String TAG_MOBILE = "mobileNumber";
    public static String TAG_USER_ADDRESS = "address";
    public static String TAG_USER_PROFILE_ID = "userProfileId";
    public static String TAG_USER_DETAIL_ID = "userDetailId";

    // Initialize change password variable
    public static String TAG_OLD_PASSWORD = "oldPassword";
    public static String TAG_NEW_PASSWORD = "newPassword";
    public static String TAG_CNF_PASSWORD = "confirmPassword";

    // Creating Order Constant
    public static String TAG_ORDER_ID = "orderId";
    public static String TAG_PURCHASE_DATE = "purchaseDate";
    public static String TAG_IS_SHIPPED = "isShipped";
    public static String TAG_ORDER_STATUS = "orderStatus";
    public static String TAG_ITEM_COUNT = "itemsCount";
    public static String TAG_CUSTOMER = "customer";
    public static String TAG_SHIPPING_ADDRESS = "shippingAdress";
    public static String TAG_BILLING_ADDRESS = "orderBillingAdress";
    public static String TAG_DISCOUNT = "dicountAmount";
    public static String TAG_AMOUNT = "orderAmount";
    public static String TAG_FORMATTED_DATE = "formatedPurchaseDate";
    public static String TAG_TOTAL_AMOUNT = "totalAmount";
    public static String TAG_NET_AMOUNT = "netAmount";
    public static String TAG_QUANTITY = "quantity";
    public static String TAG_DISC = "discount";
    public static String TAG_DISCOUNT_AMOUNT = "discountAmount";
    public static String TAG_DELIVERY_TYPE = "deliveryType";
    public static String TAG_SEAT_NO = "seatNo";

    //Constant for choice group
    public static String TAG_IS_COMPULSORY = "isCompulsory";

    //Constant for modifiers
    public static String TAG_IS_DEFAULT = "isDefault";


    //Create odered items constant
    public static String TAG_TIMINGS = "timing";
    public static String TAG_ITEM_STATUS = "itemStatus";
    public static String TAG_ITEM_PRICE = "itemPrice";
    public static String TAG_REMARKS = "remarks";
    public static String TAG_ITEM_CODE = "itemCode";

    //Create getItemDetails constant
    public static String TAG_DISC_DETAIL = "discountDescription";
    public static String TAG_AFTER_DISC = "afterDiscount";
    public static String TAG_ID = "id";


    //Create getChkOutDetail Constant

    public static String TAG_CHKOUT_BILLING_ADDRESS = "billingAddress";
    public static String TAG_CHKOUT_SHIPPING_ADDRESS = "shipping_Address";
    public static String TAG_TABLE_NO = "tableNumbers";


    public static String TAG_SHIPPING_ID = "shippingAddressId";
    public static String TAG_BILLING_ID = "billingAddressId";
    public static String TAG_CUST_ID = "customerId";
    public static String TAG_FNAME = "firstName";
    public static String TAG_LNAME = "lastName";
    public static String TAG_ADDRESS1 = "address1";
    public static String TAG_ADDRESS2 = "address2";
    public static String TAG_COUNTRY_NAME="countryName";
    public static String TAG_STATE_NAME="stateName";
    public static String TAG_COUNTRY_ID = "countryID";
    public static String TAG_ZIPCODE= "zipCode";
    public static String TAG_EMAIL_ADD = "emailAddress";
   // public static String TAG_PHONE_NO = "phoneNo";

//    public static String TAG_VENUE_ID = "venueId";
//    public static String TAG_OUTLET_ID = "outletId";
    public static String TAG_PREFIX = "prefix";
    public static String TAG_SEAT_NUMBER = "seatNumber";
    public static String TAG_ASSIGN_NUMBER= "assignSeatNumber";

    public static String TAG_TABLE_NO_ID="tablenoId";

    //ADD EDIT SHIPPING BILLING ADDRESS
    public static String TAG_ADD_FIRSTNAME="FirstName";
    public static String TAG_ADD_LASTNAME="LastName";
    public static String TAG_ADD_ADDRESS="Address";
    public static String TAG_ADD_COUNTRYID="CountryId";
    public static String TAG_ADD_STATEID="StateId";
    public static String TAG_ADD_CITY="City";
    public static String TAG_ADD_ZIPCODE="ZipCode";
    public static String TAG_ADD_PHONE="PhoneNo";
    public static String TAG_ADD_EMAIL="Email";
    public static String TAG_ADD_BILLING_ADDRESS_ID ="BillingAddressId";
    public static String TAG_ADD_SHIPPING_ADDRESS_ID="ShippingAddressId";

    //Get county and state AddAddressActivity
    public static String TAG_COUNTRYID="countryId";
    public static String TAG_STATE_ID="stateId";

    //Get delivery type
    public static String TAG_DELIVERY_ID="deliveryTypeId";
    public static String TAG_DELIVERY_NAME="deliveryTypeName";

    //Get paymentgateway details
    public static String TAG_KEY="key";
    public static String TAG_GATEWAY_NAME="gatewayName";

    //Get Advertisement details
    public static String TAG_ADD_ID="id";
    public static String TAG_ADD_URL="advertisementUrl";
    public static String TAG_ADD_ISADMIN="isAdminAdvertisement";






}
