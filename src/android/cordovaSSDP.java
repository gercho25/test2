package com.scott.plugin;

import org.apache.cordova.*;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import java.util.Scanner;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class cordovaSSDP extends CordovaPlugin {

    private static final String TAG = "scott.plugin.cordovaSSDP";

    public static String parseHeaderValue(String content, String headerName) {
        Scanner s = new Scanner(content);
        s.nextLine();
        while (s.hasNextLine()) {
          String line = s.nextLine();
          int index = line.indexOf(':');

          if (index == -1) {
            return null;
          }
          String header = line.substring(0, index);
          if (headerName.equalsIgnoreCase(header.trim())) {
            return line.substring(index + 1).trim();
          }
        }
        return null;
    }

    public void search(String service, CallbackContext callbackContext) throws IOException {
        final int SSDP_PORT = 1900;
        final int SSDP_SEARCH_PORT = 1901;
        final String SSDP_IP = "239.255.255.250";
        int TIMEOUT = 3000;

        InetSocketAddress srcAddress = new InetSocketAddress(SSDP_SEARCH_PORT);
        InetSocketAddress dstAddress = new InetSocketAddress(InetAddress.getByName(SSDP_IP), SSDP_PORT);

        // Clear the cached Device List every time a new search is called
		JSONArray deviceList = new JSONArray();

        LOG.d(TAG, "srcAddress" + srcAddress);
		
        // M-Search Packet
        StringBuffer discoveryMessage = new StringBuffer();
        discoveryMessage.append("M-SEARCH * HTTP/1.1\r\n");
        discoveryMessage.append("HOST: " + SSDP_IP + ":" + SSDP_PORT + "\r\n");
        
        discoveryMessage.append("ST:"+service+"\r\n");
        //discoveryMessage.append("ST:ssdp:all\r\n");
        discoveryMessage.append("MAN: \"ssdp:discover\"\r\n");
        discoveryMessage.append("MX: 2\r\n");
        discoveryMessage.append("\r\n");
        System.out.println("Request: " + discoveryMessage.toString() + "\n");
        byte[] discoveryMessageBytes = discoveryMessage.toString().getBytes();
        DatagramPacket discoveryPacket = new DatagramPacket(discoveryMessageBytes, discoveryMessageBytes.length, dstAddress);

        // Send multi-cast packet
        MulticastSocket multicast = null;
        try {
            multicast = new MulticastSocket(null);
            multicast.bind(srcAddress);
            multicast.setTimeToLive(4);
			LOG.d(TAG, "Send multicast request...");
            // ----- Sending multi-cast packet ----- //
            multicast.send(discoveryPacket);
        } finally {
			LOG.d(TAG, "Multicast ends. Close connection....");
            multicast.disconnect();
            multicast.close();
        }

        // Create a socket and cross your fingers for a response
        DatagramSocket wildSocket = null;
        DatagramPacket receivePacket = null;
        try {
            wildSocket = new DatagramSocket(SSDP_SEARCH_PORT);
            wildSocket.setSoTimeout(TIMEOUT);

            while (true) {
                try {
					LOG.d(TAG, "Receive ssdp");
                    receivePacket = new DatagramPacket(new byte[1536], 1536);
                    wildSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());   
                    try {
                        JSONObject device = new JSONObject();
                        device.put("USN", parseHeaderValue(message, "USN"));
                        device.put("LOCATION", parseHeaderValue(message, "LOCATION"));
                        device.put("ST", parseHeaderValue(message, "ST"));
                        device.put("Server", parseHeaderValue(message, "Server"));
                        //createServiceObjWithXMLData(parseHeaderValue(message, "LOCATION"), device);
						deviceList.put(device);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e) {
					LOG.d(TAG, "Time out");
                    LOG.d(TAG, "" + deviceList);
                    callbackContext.success(deviceList);
                    break;
                }
            }
        } finally {
            if (wildSocket != null) {
                wildSocket.disconnect();
                wildSocket.close();
            }
        }

    }

}
