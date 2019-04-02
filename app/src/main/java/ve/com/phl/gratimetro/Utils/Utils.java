package ve.com.phl.gratimetro.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by ghoss on 14/09/2018.
 */
public class Utils {
    public static String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public static void vibrar(int timev, Context context) {
        final Vibrator vibe = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibe.vibrate(timev);
    }

    public static String getTimeString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
        return dateFormat.format(new Date());
    }

    public static boolean compareTime(String time,String startime ,String endtime) {

        String pattern = "HH:mm:ss a";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(startime);
            Date date3 = sdf.parse(endtime);

            if(date1.after(date2) && date1.before(date3)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

}