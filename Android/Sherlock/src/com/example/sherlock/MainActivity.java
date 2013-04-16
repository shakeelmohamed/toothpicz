package com.example.sherlock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.*;

public class MainActivity extends SherlockActivity {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private Button buttonLoginLogout;
    private TextView textInstructionsOrLink;
    private TextView timeStart;
    private TextView timeStop;
    private Button buttonTimestamp;
    private static String my_app_id = "503312976357139";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
		textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);
        buttonTimestamp = (Button)findViewById(R.id.buttonTimestamp);
        buttonTimestamp.setVisibility(View.INVISIBLE);
        timeStart = (TextView) findViewById(R.id.timeStart);
    	timeStop = (TextView) findViewById(R.id.timeStop);
        
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session.Builder(this).setApplicationId(my_app_id).build();
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save")
            .setIcon(R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Search")
            .setIcon(R.drawable.ic_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }
	
	@Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
	private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	String urlUSERIDprefix = "https://graph.facebook.com/me?";
        	String fields = "fields=id,name&access_token=";
        	String token = session.getAccessToken();
        	String getMyId = urlUSERIDprefix + fields + token;
        	String result = getMyId;
        	try {
        		buttonTimestamp.setText("Start");
        		buttonTimestamp.setVisibility(View.VISIBLE);
        		timeStart.setVisibility(View.VISIBLE);
            	timeStop.setVisibility(View.VISIBLE);
        		buttonTimestamp.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) { onClickTimestamp(); }
                });
				result = readUrl(getMyId);
				JSONObject json = new JSONObject(readUrl(getMyId));
				String userId = (String)json.getString("id");
				String name = (String)json.getString("name");
				result = "user: "+userId;
				result += "\n";
				result += "name: "+name;
			} catch (Exception e) {
				e.printStackTrace();
			}
        	textInstructionsOrLink.setText(result);
            buttonLoginLogout.setText("Logout");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
            
        } else {
        	buttonTimestamp.setVisibility(View.INVISIBLE);
        	timeStart.setVisibility(View.INVISIBLE);
        	timeStop.setVisibility(View.INVISIBLE);
            textInstructionsOrLink.setText("Login to create a link to fetch account data");
            buttonLoginLogout.setText("Login");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

	private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
	private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    
    private void onClickTimestamp() {
    	TextView start = (TextView) findViewById(R.id.timeStart);
    	TextView stop = (TextView) findViewById(R.id.timeStop);
    	if(buttonTimestamp.getText() == "Start"){
    		buttonTimestamp.setText("Stop");
    		stop.setText("");
        	start.setText("Start: "+getUnixTimestamp());
    	}
    	else {
    		buttonTimestamp.setText("Start");
    		stop.setText("End :"+getUnixTimestamp());
    	}
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    public long getUnixTimestamp(){
		return System.currentTimeMillis() / 1000L;
	}
}