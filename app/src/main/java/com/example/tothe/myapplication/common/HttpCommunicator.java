package com.example.tothe.myapplication.common;

import android.provider.MediaStore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by tothe on 5/9/16.
 */
public class HttpCommunicator {


    public void send() throws IOException {


        URL url;
        HttpURLConnection urlConn;
        url = new URL("http://routecollector-tudorb.rhcloud.com/sessionroute");
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        urlConn.connect();
        OutputStream os = urlConn.getOutputStream();
        os.write("test".getBytes("UTF-8"));
        os.flush();
        os.close();

        if (urlConn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + urlConn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (urlConn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        urlConn.disconnect();

    }

    private String getFileContent(File f) {
        return null;
    }

}
