package com.example.qzero.Helpers;

import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.R;
import com.example.qzero.CommonFiles.Common.CustomDialog;

public class AlertDialogHelper {


	//Initialize
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
		FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT,context);
		txtViewTitle.setText(title);

		txtViewAlertMsg= (TextView) dialog
				.findViewById(R.id.txtViewAlertMsg);
		txtViewAlertMsg.setText(message);
		FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT, context);

		txtViewOk  = (TextView) dialog.findViewById(R.id.txtViewOk);
		FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT, context);
	}

}
