package com.scott.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public class serviceDiscovery extends CordovaPlugin {

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		/*
        if (action.equals("getNetworkServices")) {
			return true;
		}
		
		return false;
		*/

        final cordovaSSDP mCordovaSSDP = new cordovaSSDP(this.cordova.getActivity().getApplicationContext());

        if (action.equals("getNetworkServices")) {

            final String service = data.getString(0);

            cordova.getThreadPool().execute(new Runnable() {
               @Override
               public void run() {
                  try{
                      mCordovaSSDP.search(service,callbackContext);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
               }
            });  
            return true;

        } else {

            return false;

        }
		
    }
}
