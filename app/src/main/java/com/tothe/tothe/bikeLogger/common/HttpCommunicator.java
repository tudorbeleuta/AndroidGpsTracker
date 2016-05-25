package com.tothe.tothe.bikeLogger.common;

import android.widget.Toast;

import com.tothe.tothe.bikeLogger.MainActivity;
import com.tothe.tothe.bikeLogger.models.SessionData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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

    AsyncHttpClient client;
    int requests;
    boolean fullUpload;

    public HttpCommunicator() {
        client = new AsyncHttpClient();
    }

    public void postSingleSession(SessionData data) {


        RequestParams params = new RequestParams();
        try {

            params.put(LOGGED_NAME, data.getLogged().getName());
            params.put(LOGGED_CONTENT, FileUtils.readFileToString(data.getLogged().getAbsoluteFile()));

            params.put(DESCRIPTOR_NAME, data.getDescriptor().getName());
            params.put(DESCRIPTOR_CONTENT, FileUtils.readFileToString(data.getDescriptor().getAbsoluteFile()));



            client.post(BASE_URL, params, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    toastStatus("Successfully uploaded!");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    toastStatus("Failed to upload!");
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getAppContext(), "Failed to upload, could not find files!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void toastStatus(String status) {
        Toast.makeText(MainActivity.getAppContext(), status, Toast.LENGTH_SHORT).show();

    }

    private boolean clearFiles(boolean fullDelete, List<SessionData> sessions) {

        boolean successfullDel = true;
        for (SessionData session : sessions) {
            boolean deletedSession = fullDelete ? session.getLogged().delete() && session.getDescriptor().delete() : session.getDescriptor().delete();
            if (!deletedSession) {
                successfullDel = false;
            }

        }
        return successfullDel;
    }

    public void postMultipleSessions(final List<SessionData> multipleData, final boolean fullDelete) {
        RequestParams params;

        requests = multipleData.size();
        fullUpload = true;
        for (SessionData data : multipleData) {
            params = new RequestParams();

            try {

                params.put(LOGGED_NAME, data.getLogged().getName());
                params.put(LOGGED_CONTENT, FileUtils.readFileToString(data.getLogged().getAbsoluteFile()));

                params.put(DESCRIPTOR_NAME, data.getDescriptor().getName());
                params.put(DESCRIPTOR_CONTENT, FileUtils.readFileToString(data.getDescriptor().getAbsoluteFile()));

                //   client.cancelAllRequests(true);
                client.post(BASE_URL, params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        requests--;
                        toastStatus("Are files are uploaded. Thanks!");
                        clearFiles(fullDelete, multipleData);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        toastStatus("Failed to upload some files, please check your connection!");
                        fullUpload = false;
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.getAppContext(), "Failed to upload, could not find files!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }


}
