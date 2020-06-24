package com.example.email.job_service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JobExecutor extends AsyncTask<Void, Void, String> {


    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private Context context;

    private String idAccount;


    public JobExecutor(Context context ) {
        this.context = context;

    }

    @Override
    protected String doInBackground(Void... voids) {

        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL("http://10.0.2.2:8080/messages/notify/"+idAccount);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            //Connect to our url
            connection.connect();


            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            // Log.i("TAaaaaG", String.valueOf(result));
            return result;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
