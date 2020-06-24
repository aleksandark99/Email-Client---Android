package com.example.email.job_service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.email.R;
import com.example.email.repository.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyJobScheduler extends JobService {

    //private Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    //private MessageService mMessageService = mRetrofit.create(MessageService.class);
    private JobParameters params;

    private static int notificationID = 1;
    private static String channelID = "M_CH_ID";

    private JobExecutor mJobExecutor;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {
        //final JobParameters par = params;
        //Log.i("TAG", "onStartJob: pocinje fetchDaTA()");
        // fetchData();
        mJobExecutor = new JobExecutor(getApplicationContext())
        {

            @Override
            protected String doInBackground(Void... voids) {

                String result;
                String inputLine;

                try {
                    //Create a URL object holding our url
                    URL myUrl = new URL("http://10.0.2.2:8080/messages/notify/"+String.valueOf(Repository.activeAccount.getId()));
                    //Create a connection
                    HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                    connection.setRequestMethod("GET");

                    //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Authorization", Repository.jwt);
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(15000);
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

            @Override
            protected void onPostExecute(String i){
                Toast.makeText(getApplicationContext(), "Integer returned " + i, Toast.LENGTH_SHORT).show();
                Log.i("registrovan text", String.valueOf(i));

                //provera za ako je veci od 0
                int numberOfNewMessages =Integer.valueOf(i);
                if (numberOfNewMessages > 0){
                    createNotificationChannel();
                    //NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelID);

                    mBuilder.setSmallIcon(R.drawable.ic_message_black_24dp);
                    mBuilder.setContentTitle(getApplicationContext().getString(R.string.new_message));
                    mBuilder.setContentText(i + " new messages");
                    mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(notificationID, mBuilder.build());
                }


                jobFinished(params, false);


            }
        };

        mJobExecutor.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobExecutor.cancel(true);
        return false;
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "PersonalNotif";
            String desc = "aaaaa";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, name, importance);
            notificationChannel.setDescription(desc);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
