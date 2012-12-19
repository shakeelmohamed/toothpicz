package com.ibt.december.toothpicz;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	String imei = "";
	String phoneNumber = "";
	@Override
	public void onClick(View v) {
		switch( ((Button) v).getId() ){
		case R.id.startButton:
			initiateScanner();
			break;
		};
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TelephonyManager t = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        this.imei = t.getDeviceId();
        this.phoneNumber = t.getLine1Number();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		Button start = (Button)findViewById(R.id.startButton);
        start.setOnClickListener(this);
	}	
	private void initiateScanner()
	{
		IntentIntegrator.initiateScan(this);	
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode) {
    	case IntentIntegrator.REQUEST_CODE: {
	    	if (resultCode != RESULT_CANCELED) {
		    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		    	if (scanResult != null) {
			    	String scanCode = scanResult.getContents();
			    	String scanType = scanResult.getFormatName();
			    	TextView tv = (TextView) findViewById(R.id.textView1);
			    	if(scanType.equals("QR_CODE")){
			    		if(isConnected(this)){
			    			tv.setText("QR code scanned & data connection is good.");
			    			try {
			    				//Photographer has scanned QR code from biz card
			    				//If code in DB:
			    					//Associate photos
			    				//Else:
			    					//Add code to DB, associated w/ photographer's userID
			    				
			    				//When start is pressed:
			    					//Push a timestamp to the DB associated w/ the current QR code sessionID
			    				//
			    				
			    				String url = "";
			    					//Abstract this getResponse method to take a URL & string[] of params
								String response = getResponse(this.imei, scanCode, url);
								String albumURL = "";
							} catch (Exception e) {
								e.printStackTrace();
							}
			    		}
			    		else {
			    			tv.setText("QR code scanned & data connection is bad.");
			    		}
			    	}
			    	else {
			    		//Handle error, or something
			    		tv.setText("A "+scanType+" was scanned");
			    	}
			    	
			    	
			    	//put whatever you want to do with the code here
			    	//This will push the code to the ui
			    	/*
			    	TextView tv = new TextView(this);
			    	String postme = "";
			    	postme += "The code's message was : "+ scanCode + "\n";
			    	tv = (TextView) findViewById(R.id.scanPageText); //My doesn't make a new view
			    	Button startScan = (Button)findViewById(R.id.startScan);
			    	startScan.setVisibility(View.GONE); //Hides the button if there's a code found
			    	if(isConnected(this)){
			    		try {
							//postme += 
							String suxess = getResponse(this.imei, scanCode);
							postme += suxess;
							if(suxess.toLowerCase().equals("true")){ //There could be a newline
								
							}
							else {
								//postme += "Sorry that code was already used!";
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	}
			    	else {
			    		postme += "No data connection!\nThis app will be useless until you setup a data or wifi connection.";
			    	}
			    	tv.setText(postme);
			    	*/
		    	}
	    	}
	    	break;
		}
	}
	}
	public static boolean isConnected(Context c) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return false;
        }
        return true;
    }
	public static String getResponse(String imei, String code, String url) throws Exception {
    	String ret = "";
    	BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            ret = page;
            } finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    	return ret;
    }
}
