/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.app.ActivityManager
 *  android.app.ActivityManager$TaskDescription
 *  android.app.Application
 *  android.app.Dialog
 *  android.app.Fragment
 *  android.app.FragmentManager
 *  android.app.LoaderManager
 *  android.app.PendingIntent
 *  android.app.SharedElementCallback
 *  android.app.TaskStackBuilder
 *  android.app.VoiceInteractor
 *  android.app.assist.AssistContent
 *  android.content.BroadcastReceiver
 *  android.content.ComponentCallbacks
 *  android.content.ComponentName
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.IntentSender
 *  android.content.ServiceConnection
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.res.AssetManager
 *  android.content.res.ColorStateList
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.database.Cursor
 *  android.database.DatabaseErrorHandler
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.graphics.Bitmap
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.drawable.Drawable
 *  android.media.MediaPlayer
 *  android.media.session.MediaController
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.PersistableBundle
 *  android.os.UserHandle
 *  android.support.annotation.Nullable
 *  android.support.v4.app.Fragment
 *  android.support.v4.app.FragmentManager
 *  android.support.v4.app.LoaderManager
 *  android.support.v4.app.SharedElementCallback
 *  android.support.v4.app.SupportActivity
 *  android.support.v4.app.SupportActivity$ExtraData
 *  android.support.v4.app.TaskStackBuilder
 *  android.support.v4.media.session.MediaControllerCompat
 *  android.support.v7.app.ActionBar
 *  android.support.v7.app.ActionBarDrawerToggle
 *  android.support.v7.app.ActionBarDrawerToggle$Delegate
 *  android.support.v7.app.AppCompatActivity
 *  android.support.v7.app.AppCompatDelegate
 *  android.support.v7.view.ActionMode
 *  android.support.v7.view.ActionMode$Callback
 *  android.support.v7.widget.Toolbar
 *  android.transition.Scene
 *  android.transition.TransitionManager
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.Display
 *  android.view.DragAndDropPermissions
 *  android.view.DragEvent
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.SearchEvent
 *  android.view.View
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.webkit.WebSettings
 *  android.webkit.WebSettings$RenderPriority
 *  android.webkit.WebView
 *  android.webkit.WebViewClient
 *  android.widget.RelativeLayout
 *  android.widget.Toolbar
 *  com.android.tools.fd.runtime.IncrementalChange
 *  com.android.tools.fd.runtime.InstantReloadException
 *  np.com.dhirajsharma.coolwordclock.activity.Home$1
 *  np.com.dhirajsharma.coolwordclock.activity.Home$2
 *  np.com.dhirajsharma.coolwordclock.activity.Home$3
 *  np.com.dhirajsharma.coolwordclock.util.CommonUtils
 */
package np.com.dhirajsharma.coolwordclock.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import np.com.dhirajsharma.coolwordclock.R;
import np.com.dhirajsharma.coolwordclock.util.CommonUtils;

public class Home extends AppCompatActivity {
    private WebView webView;
    private RelativeLayout container;
    private Calendar calendar;
    private List<Boolean> colors;
    private Handler handler;
    private int lastMinute = -1;
    private Runnable r;
    private String data = "";
    private boolean doubleBackToExitPressedOnce = false;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        colors = Arrays.asList(new Boolean[CommonUtils.WORDS.size()]);
        webView = (WebView) findViewById(R.id.webView);
        container = (RelativeLayout) findViewById(R.id.container);
        setContainerPadding();
        webViewSettings();


        startCountDown();
    }

    private void webViewSettings() {
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(2, null);
        } else {
            webView.setLayerType(1, null);
        }
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        webView.setBackgroundColor(Color.parseColor("#212121"));
    }

    private void loadDataToWebview() {
        Log.d("My Log", ("Data : " + getDataToLoad()));
        webView.loadData(getDataToLoad(), "text/html", "UTF-8");
        webView.reload();
    }

    private void setContainerPadding() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        container.setPadding(width / 13, 0, width / 13, 0);
    }

    private String getDataToLoad() {

        updateColor();
        data = "<div style=\"text-align: justify; font-size: " + CommonUtils.FONT_SIZE_VALUE + "px; font-weight: bold;\">";
        for (int i = 0; i < CommonUtils.WORDS.size(); ++i) {
            data = data + "<span style=\"color:" + getColorForWord(i) + "\">" + CommonUtils.WORDS.get(i) + "</span> ";
        }
        data = data + "</div>";
        return data;
    }

    private void updateColor() {
        colors.set(0, true);
        colors.set(1, true);
        colors.set(2, minuteBound(30, 34));
        colors.set(3, minuteBound(10, 14) || minuteBound(50, 54));
        colors.set(4, minuteBound(15, 19) || minuteBound(45, 49));
        colors.set(5, minuteBound(20, 29) || minuteBound(35, 44));
        colors.set(6, minuteBound(5, 9) || minuteBound(55, 59) || minuteBound(25, 29) || minuteBound(35, 39));
        colors.set(7, !colors.get(2) && !colors.get(4) && !minuteBound(0, 4));
        colors.set(8, minuteBound(35, 59));
        colors.set(9, minuteBound(5, 34));
        colors.set(22, minuteBound(0, 4));
        colors.set(10, hourBound(1));
        colors.set(11, hourBound(3));
        colors.set(12, hourBound(2));
        colors.set(13, hourBound(4));
        colors.set(14, hourBound(5));
        colors.set(15, hourBound(6));
        colors.set(16, hourBound(7));
        colors.set(17, hourBound(8));
        colors.set(18, hourBound(9));
        colors.set(19, hourBound(10));
        colors.set(20, hourBound(11));
        colors.set(21, hourBound(12));
        Log.d("My Log", "Color Changed");
    }

    private String getColorForWord(int i) {
        if (colors.get(i) == null) {
            return CommonUtils.OFF_COLOR;
        }
        return getColor(colors.get(i));
    }

    private void startCountDown() {
        if (r != null) {
            return;
        }
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                int currentTime = getMinute() / 5;
                Log.d("MU LOG", "HERE OUTSIDE");
                if (currentTime != lastMinute) {
                    Log.d("MU LOG", "HERE INSIDE");
                    lastMinute = currentTime;
                    loadDataToWebview();
                    playDing();
                }
                handler.postDelayed(this, 10000);
            }
        };
        r.run();
    }

    private boolean hourBound(int hour) {
        return (getHour() == hour && colors.get(22)) || (getHour() == hour && colors.get(9)) || ((getHour() < 12 ? getHour() + 1 : 1) == hour && colors.get(8));
    }

    private int getMinute() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    private int getHour() {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        return (hour == 0) ? 12 : hour;
    }

    private boolean minuteBound(int min, int max) {
        return getMinute() <= max && getMinute() >= min;
    }

    private String getColor(boolean on) {
        return on ? CommonUtils.ON_COLOR : CommonUtils.OFF_COLOR;
    }

    private void playDing() {
        MediaPlayer mMediaPlayer = MediaPlayer.create(Home.this, R.raw.ding);
        mMediaPlayer.setAudioStreamType(3);
        mMediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            if (handler != null && r != null) {
                handler.removeCallbacks(r);
            }
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back key again to exit !", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
