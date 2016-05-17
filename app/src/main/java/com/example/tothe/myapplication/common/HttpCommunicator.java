package com.example.tothe.myapplication.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.example.tothe.myapplication.MainActivity;
import com.example.tothe.myapplication.models.SessionData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tothe on 5/9/16.
 */
public class HttpCommunicator {

    public static final String DESCRIPTOR_CONTENT = "descriptor_content";

    public static final String DESCRIPTOR_NAME = "descriptor_name";

    public static final String LOGGED_CONTENT = "logged_content";

    public static final String LOGGED_NAME = "logged_name";

    public static final String BASE_URL = "https://bootjava8-tudorb.rhcloud.com/";


    public void multipartPost(SessionData data) throws IOException {


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        try {

            params.put(LOGGED_NAME, data.getLogged().getName());
            params.put(LOGGED_CONTENT, FileUtils.readFileToString(data.getLogged().getAbsoluteFile()));

            params.put(DESCRIPTOR_NAME, data.getDescriptor().getName());
            params.put(DESCRIPTOR_CONTENT, FileUtils.readFileToString(data.getDescriptor().getAbsoluteFile()));


            client.post(BASE_URL, params, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(MainActivity.getAppContext(), "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(MainActivity.getAppContext(), "Failed to upload!", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getAppContext(), "Failed to upload, could not find files!", Toast.LENGTH_SHORT).show();

        }



    }





}
