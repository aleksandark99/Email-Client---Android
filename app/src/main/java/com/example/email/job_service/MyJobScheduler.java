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
        mJobExecutor = new JobExecutor(getApplicationContext(), String.valueOf(Repository.activeAccount.getId()))
        {
            @Override
            protected void onPostExecute(String i){
                Toast.makeText(getApplicationContext(), "Integer returned " + i, Toast.LENGTH_SHORT).show();
                Log.i("registrovan text", String.valueOf(i));

                //provera za ako je veci od 0 kreiraj notifikaciju
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
