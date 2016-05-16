package com.example.tothe.myapplication.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.example.tothe.myapplication.MainActivity;
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
public class HttpCommunicator extends AsyncTask<File, Void, String> {

    public static final String DESCRIPTOR_CONTENT = "descriptor_content";

    public static final String DESCRIPTOR_NAME = "descriptor_name";

    public static final String LOGGED_CONTENT = "logged_content";

    public static final String LOGGED_NAME = "logged_name";

    public static final String BASE_URL = "https://bootjava8-tudorb.rhcloud.com/";

    public void multipartPost(File loggedFile) throws IOException {


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        try {

            String fileContent = FileUtils.readFileToString(loggedFile.getAbsoluteFile());

            params.put(DESCRIPTOR_NAME, loggedFile.getName());
            params.put(DESCRIPTOR_CONTENT, fileContent);


            params.put(LOGGED_NAME, "test");
            params.put(LOGGED_CONTENT, "test content");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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

    }



    private String getFileContent(File f) {
        return null;
    }

    @Override
    protected String doInBackground(File... params) {

        String resp = "";
        for (File f : params) {
            try {
                multipartPost(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resp;

    }

    public String writeFiles(File... files) {
        return doInBackground(files);
    }



}
