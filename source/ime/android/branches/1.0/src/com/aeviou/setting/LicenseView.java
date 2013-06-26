package com.aeviou.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aeviou.*;
/**
 * @author Rex
 * The LisenceView is shown when the input method is activated for the first time.
 * Also when a user wants to or more likely "accidentally" chooses to view the license
 * information in the settings, this dialog-like view will pop out and request a confirmation
 * of license agreement.
 * 
 * There is no "NO" button designed because it simply is not useful. User should know that by
 * using this software is equivalent to acknowledging the license agreement. 
 * 
 * The view is designed in the license layout.
 */
public class LicenseView extends Activity implements OnClickListener{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * On creating the licenseView, simply inflate the contents using license xml resource
	 * and add a button for "OK".
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.license);
		Button okButton = (Button)this.findViewById(R.id.button1);
		okButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		this.finish();
	}

}
