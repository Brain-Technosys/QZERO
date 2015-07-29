package com.example.qzero.CommonFiles.Helpers;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.CustomDialog;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;

import com.example.qzero.R;

public class AlertDialogHelper {

	static CustomDialog dialog;

	static TextView txtViewTitle;
	static TextView txtViewAlertMsg;

	static TextView txtViewOk;

	public static void showAlertDialog(Activity context, String message,
			String title) {

		setLayout(context, message, title);

		txtViewOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		dialog.show();

	}

	public static void showAlertDialogFinish(final Activity context,
			String message, String title) {

		txtViewOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				context.finish();
			}

		});
		dialog.show();

	}

	public static void setLayout(Activity context, String message, String title) {
		dialog = new CustomDialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_alert_box);

		txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
		FontHelper.applyFont(context, txtViewTitle,FontType.FONT);
		txtViewTitle.setText(title);

		txtViewAlertMsg= (TextView) dialog
				.findViewById(R.id.txtViewAlertMsg);
		txtViewAlertMsg.setText(message);

		FontHelper.applyFont(context, txtViewAlertMsg, FontType.FONT);


		txtViewOk  = (TextView) dialog.findViewById(R.id.txtViewOk);

		FontHelper.applyFont(context, txtViewOk, FontType.FONT);

	}

}
