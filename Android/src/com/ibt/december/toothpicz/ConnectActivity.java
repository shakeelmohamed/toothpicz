package com.ibt.december.toothpicz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;

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
		final View currentView = v;
		switch( ((Button) v).getId() ){
		case R.id.connectButton:
			Log.d("ConnectActivity", ConnectActivity.class.getName());
			// start Facebook Login
		    Session.openActiveSession(this, true, new Session.StatusCallback() {

		      // callback when session changes state
		      @Override
		      public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {

		          // make request to the /me API
		          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		            // callback after Graph API response with user object
		            @Override
		            public void onCompleted(GraphUser user, Response response) {
		              if (user != null) {
		                /*String log = "getname(): "+user.getName();
		                log += "\ngetId(): "+user.getId();
		                long unixTime = System.currentTimeMillis() / 1000L;
		                log += "\nTimestamp: "+unixTime;*/
		                SharedPreferences settings = getSharedPreferences("ToothpiczPrefs", 0);
		                SharedPreferences.Editor editor = settings.edit();
		                editor.putString("startTS", String.valueOf(getUnixTimestamp()));
		                editor.putString("userID", user.getId());
		                editor.putString("userName", user.getName());
		                editor.putString("endTS", String.valueOf(getUnixTimestamp()));
		                editor.commit();
		                
		                TextView welcome = (TextView) findViewById(R.id.welcome);
		                settings = getSharedPreferences("ToothpiczPrefs", 0);
		                welcome.setText(settings.getAll().toString());
		                /* This part is broken
		                Intent myIntent = new Intent(currentView.getContext(), MainActivity.class);
		                startActivityForResult(myIntent, 0);
		                */
		              }
		            }
		          });
		        }
		      }
		    });
			break;
		};
	}
	private void doFacebookAuth(){
		//Test the rest of the code by putting it in here
	}
	
	public long getUnixTimestamp(){
		return System.currentTimeMillis() / 1000L; //Figure out how to show the length of time of current session
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
}
