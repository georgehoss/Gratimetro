package ve.com.phl.gratimetro.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ghoss on 14/11/2018.
 */
public class StorageUtils {

    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }
    public static void saveUserName(Context context, String name) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences pref = getSharedPref(context);
        String name = pref.getString("name", "");
        return name;
    }

    public static void saveUserEmail(Context context, String email) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences pref = getSharedPref(context);
        String name = pref.getString("email", "");
        return name;
    }

    public static void setSound(Context context, boolean sound) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("sound", sound);
        editor.commit();
    }

    public static boolean getSound(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("sound", true);
    }

    public static void setVibration(Context context, boolean vibration) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("vibration", vibration);
        editor.commit();
    }

    public static boolean getVibration(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("vibration", true);
    }

    public static void setNotification(Context context, boolean vibration) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notification", vibration);
        editor.commit();
    }

    public static boolean getNotification(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("notification", true);
    }

    public static void setNotification1(Context context, boolean vibration) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notification1", vibration);
        editor.commit();
    }

    public static boolean getNotification1(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("notification1", false);
    }


    public static void setNotification2(Context context, boolean vibration) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notification2", vibration);
        editor.commit();
    }

    public static boolean getNotification2(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("notification2", false);
    }

    public static void setNotification3(Context context, boolean vibration) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notification3", vibration);
        editor.commit();
    }

    public static boolean getNotification3(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getBoolean("notification3", false);
    }

    public static void setPosMsg(Context context, int pos) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("pos", pos);
        editor.commit();
    }

    public static int getPosMsg(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getInt("pos", 0);
    }


    public static void removeUser(Context context) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


}
