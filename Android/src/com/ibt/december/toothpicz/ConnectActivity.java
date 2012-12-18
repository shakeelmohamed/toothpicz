package com.ibt.december.toothpicz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConnectActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		Button connectButton = (Button)findViewById(R.id.connectButton);
		connectButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch( ((Button) v).getId() ){
		case R.id.connectButton:
			doFacebookAuth();
			break;
		};
	}
	private void doFacebookAuth(){
		
	}
}
