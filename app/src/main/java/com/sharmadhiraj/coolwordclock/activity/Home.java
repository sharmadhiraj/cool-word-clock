package com.sharmadhiraj.coolwordclock.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sharmadhiraj.coolwordclock.R;
import com.sharmadhiraj.coolwordclock.util.CommonUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.View.LAYER_TYPE_HARDWARE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.webkit.WebSettings.LOAD_NO_CACHE;
import static android.webkit.WebSettings.RenderPriority.HIGH;
import static com.sharmadhiraj.coolwordclock.util.CommonUtils.FONT_SIZE_VALUE;
import static com.sharmadhiraj.coolwordclock.util.CommonUtils.WORDS;

public class Home extends AppCompatActivity {
    private WebView webView;
    private RelativeLayout container;
    private Calendar calendar;
    private List<Boolean> colors;
    private Handler handler;
    private int lastMinute = -1;
    private Runnable r;
    private boolean doubleBackToExitPressedOnce = false;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        colors = Arrays.asList(new Boolean[WORDS.size()]);
        webView = findViewById(R.id.webView);
        container = findViewById(R.id.container);
        setContainerPadding();
        webViewSettings();

        startCountDown();
    }

    private void webViewSettings() {
        webView.setOnLongClickListener(view -> false);
        webView.getSettings().setRenderPriority(HIGH);
        webView.getSettings().setCacheMode(LOAD_NO_CACHE);
        webView.setLayerType(SDK_INT >= 19 ? LAYER_TYPE_HARDWARE : LAYER_TYPE_SOFTWARE, null);
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        webView.setBackgroundColor(Color.parseColor("#212121"));
    }

    private void loadDataToWebView() {
        webView.loadData(getDataToLoad(), "text/html", "UTF-8");
        webView.reload();
    }

    private void setContainerPadding() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        container.setPadding(width / 13, 0, width / 13, 0);
    }

    private String getDataToLoad() {
        updateColor();
        StringBuilder data = new StringBuilder("<div style=\"text-align: justify; font-size: " + FONT_SIZE_VALUE + "px; font-weight: bold;\">");
        for (int i = 0; i < WORDS.size(); ++i) {
            data.append("<span style=\"color:")
                    .append(getColorForWord(i))
                    .append("\">")
                    .append(WORDS.get(i))
                    .append("</span> ");
        }
        data.append("</div>");
        return data.toString();
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
                if (currentTime != lastMinute) {
                    lastMinute = currentTime;
                    loadDataToWebView();
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

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
