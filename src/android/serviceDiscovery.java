package com.scott.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public class serviceDiscovery extends CordovaPlugin {

	@Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
		
        //final cordovaSSDP mCordovaSSDP = new cordovaSSDP(this.cordova.getActivity().getApplicationContext());

        if (action.equals("getNetworkServices")) {

			callbackContext.success("aaa-bbb-ccc");
			/*
            final String service = data.getString(0);

            cordova.getThreadPool().execute(new Runnable() {
               @Override
               public void run() {
                  try{
                      mCordovaSSDP.search(service, context); //callbackContext);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
               }
            });  
			*/
            return true;

        } else {

            return false;

        }
		
    }
}
