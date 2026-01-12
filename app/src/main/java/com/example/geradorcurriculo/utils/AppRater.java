package com.example.geradorcurriculo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.example.geradorcurriculo.R;

public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;

    public static void appLaunched(Context mContext) {
        android.content.SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        android.content.SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    private static void showRateDialog(final Context mContext, final android.content.SharedPreferences.Editor editor) {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.rate_title)
                .setMessage(R.string.rate_message)
                .setPositiveButton(R.string.rate_rate, (dialog, which) -> {
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, 
                            Uri.parse("market://details?id=" + mContext.getPackageName())));
                })
                .setNeutralButton(R.string.rate_later, (dialog, which) -> {
                    if (editor != null) {
                        editor.putLong("launch_count", 0);
                        editor.commit();
                    }
                })
                .setNegativeButton(R.string.rate_no, (dialog, which) -> {
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                })
                .show();
    }
}
