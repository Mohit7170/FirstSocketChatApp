package com.testapps.livestreamingchatting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.testapps.livestreamingchatting.R;
import com.testapps.livestreamingchatting.dialogs.CustomLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static final String TAG = "Helper";

    static CustomLoader customLoader;

    public static void openUrl(Activity activity, String url) {
        try {
            Uri uri = Uri.parse(url);
            Log.d(TAG, "openUrl: Calling Url Link : " + url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "openUrl: Getting Exception : " + e);
            showSnackBar(activity, "Unable to Open");
        }
    }

    public static void showLoader(Context context) {
        try {
            customLoader = new CustomLoader(context);
            customLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customLoader.show();
            Log.d(TAG, "showLoader: From Activity -- " + context.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideLoader() {
        try {
            if ((customLoader != null) && customLoader.isShowing())
                customLoader.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBar(Activity activity, String message) {
        Snackbar.make(activity.getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG)
                .setAction(activity.getString(R.string.hide), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .show();
        Log.d(TAG, "showSnackBar: From Activity -- " + activity.getClass().getSimpleName());
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showSnackBar: From Activity -- " + activity.getClass().getSimpleName());
    }

    public static void sendEmail(Activity activity, String mailTo) {
        Log.d(TAG, "sendEmail: From Activity -- " + activity.getClass().getSimpleName());

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(mailTo));

        try {
            activity.startActivity(Intent.createChooser(intent, "Select Email App"));
        } catch (Exception e) {
            Log.d(TAG, "sendEmail: Failed to Send Email -- " + e);
            Helper.showSnackBar(activity, "No App for Sending Email");
        }
    }

    public static boolean isNetConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        }
        return false;
    }

    public static void shareContent(Activity activity, String shareText) {
        try {
            Log.d(TAG, "shareApp: Sharing App with Text : " + shareText);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            activity.startActivity(Intent.createChooser(intent, "Share Via"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "shareApp: Getting Exception : " + e);
            Helper.showSnackBar(activity, "Unable to Share");
        }

    }

    public static String getYouTubeId(String url) {
        String videoId = url;
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        Log.d(TAG, "getYouTubeId: id -- " + videoId);
        return videoId;
    }


}
