package ve.com.phl.gratimetro.Service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import ve.com.phl.gratimetro.GratitudeActivity;
import ve.com.phl.gratimetro.R;
import ve.com.phl.gratimetro.Utils.StorageUtils;
import ve.com.phl.gratimetro.Utils.Utils;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "gratimetro";
    private static final int NOTIFICATION_ID = 65000;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            if (StorageUtils.getUserName(NotificationService.this)!=null)

                if (StorageUtils.getNotification(NotificationService.this))
                {
                    if (Utils.compareTime(Utils.getTimeString(),"09:00:00 AM","09:00:30 AM"))
                    {
                        if (!StorageUtils.getNotification1(NotificationService.this)) {
                            showNotification();
                            StorageUtils.setNotification1(NotificationService.this,true);
                        }
                    }
                    else
                        StorageUtils.setNotification1(NotificationService.this,false);


                    if (Utils.compareTime(Utils.getTimeString(),"03:00:00 PM","03:00:30 PM"))
                    {
                        if (!StorageUtils.getNotification2(NotificationService.this)) {
                            showNotification();
                            StorageUtils.setNotification2(NotificationService.this,true);
                        }
                    }
                    else
                        StorageUtils.setNotification2(NotificationService.this,false);


                    if (Utils.compareTime(Utils.getTimeString(),"09:00:00 PM","09:00:30 PM"))
                    {
                        if (!StorageUtils.getNotification3(NotificationService.this)) {
                            showNotification();
                            StorageUtils.setNotification3(NotificationService.this,true);
                        }
                    }
                    else
                        StorageUtils.setNotification3(NotificationService.this,false);



                }


            timerHandler.postDelayed(this, 10000);

        }
    };


    private void showNotification(){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, GratitudeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String text1 = getString(R.string.would_you_like_to_thank_in_this_moment);
        String text = getString(R.string.hello)+" "+ StorageUtils.getUserName(this)+" "+ getString(R.string.would_you_like_to_thank_in_this_moment);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.hands)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text1)
                .setStyle(new NotificationCompat.BigTextStyle()
                      .bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        if (StorageUtils.getVibration(this))
            Utils.vibrar(100,this);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
