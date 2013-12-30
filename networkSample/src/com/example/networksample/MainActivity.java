package com.example.networksample;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements android.view.View.OnClickListener{
	
	TextView txt3G;
	TextView txtWifi;
	String Tag = "networksample";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt3G = (TextView)this.findViewById(R.id.txt3G);
        txtWifi = (TextView)this.findViewById(R.id.txtWifi);
        Button btnAgain= (Button)this.findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(this);
    }
    
 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void checkNetworkInfo()
    {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        txt3G.setText("mobile state " + mobile); //显示3G网络连接状态
        
        NetworkInfo  wifiInfo =  conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //wifi
        State wifiState = wifiInfo.getState();
    
        txtWifi.setText("wifi state " + wifiState); //显示wifi连接状态
        
        Log.i(Tag, "mobile state " + mobile);
        Log.i(Tag, "wifi state " + wifiState);
        Log.i(Tag, "wifi is available ? " + wifiInfo.isAvailable());
        
       
        
    }
    
    public void onClick(View view){
    	if (view.getId() == R.id.btnAgain){
    		checkNetworkInfo();
    	}
    }
}
