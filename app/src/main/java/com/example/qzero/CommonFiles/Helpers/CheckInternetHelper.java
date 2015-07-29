package com.example.qzero.CommonFiles.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckInternetHelper {

	public static Boolean checkInternetConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting()
				|| connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.isConnectedOrConnecting()) {
			return true;
		}
		return false;

	}

}
