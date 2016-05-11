package com.example.tothe.myapplication.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by tothe on 5/9/16.
 */
public class HttpCommunicator extends AsyncTask<File, Void, String> {


    /*public void send() throws IOException {


        URL url;
        HttpURLConnection urlConn;
        url = new URL("http://bootjava8-tudorb.rhcloud.com/");
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("POST");



        urlConn.connect();
        OutputStream os = urlConn.getOutputStream();
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
*/
    public String multipartPost(File loggedFile) throws IOException {
        HttpURLConnection httpUrlConnection = null;
        URL url = new URL("http://bootjava8-tudorb.rhcloud.com/");
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;");

        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        FileInputStream fileInputStream = new FileInputStream(loggedFile);

        //request header start
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                loggedFile.getName() + "\";file=\"" +
                loggedFile.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        //request body, file


        byte[] buffer = new byte[1024];
        int count = 0;

        while ((count = fileInputStream.read(buffer)) >= 0) {
            request.write(buffer, 0, count);
        }

        //request ending

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);
        request.flush();
        request.close();


        InputStream responseStream = new
                BufferedInputStream(httpUrlConnection.getInputStream());

        BufferedReader responseStreamReader =
                new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();

        responseStream.close();

        httpUrlConnection.disconnect();

        return response;

    }



    private String getFileContent(File f) {
        return null;
    }

    @Override
    protected String doInBackground(File... params) {

        String resp = "";
        for (File f : params) {
            try {
                resp = multipartPost(f);
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
