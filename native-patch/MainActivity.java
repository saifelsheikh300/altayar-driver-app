package com.altayar.driver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.getcapacitor.BridgeActivity;

/**
 * تمت إضافة هذا الملف بواسطة Claude فوق قالب Capacitor الافتراضي (كان فاضي أصلاً).
 * السبلاش دلوقتي: شاشة برتقالي واللوجو في النص، وفاضلة طول ما موقع الأوردرات لسه بيحمّل
 * (مش مدة ثابتة)، وبتختفي أول ما المحتوى يخلص تحميل.
 * ملحوظة: مفيش أي لمس لـ WebViewClient بتاع كباسيتور، فبريدج الإشعارات والبلجنز شغالين عادي.
 */
public class MainActivity extends BridgeActivity {

    private static final int MIN_DISPLAY_MS = 400;   // أقل مدة ظهور حتى لو الموقع حمّل فوراً
    private static final int MAX_DISPLAY_MS = 7000;   // أقصى مدة أمان لو النت بطيء أو مقطوع
    private static final int POLL_INTERVAL_MS = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSplashUntilLoaded();
    }

    private void showSplashUntilLoaded() {
        final float density = getResources().getDisplayMetrics().density;
        final long startTime = System.currentTimeMillis();

        final FrameLayout overlay = new FrameLayout(this);
        overlay.setBackgroundColor(Color.parseColor("#FF4500")); // نفس لون البراند

        final ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.altayar_splash_logo);
        FrameLayout.LayoutParams logoParams = new FrameLayout.LayoutParams(
                (int) (170 * density), (int) (170 * density));
        logoParams.gravity = Gravity.CENTER;
        logo.setLayoutParams(logoParams);
        logo.setAlpha(0f);
        overlay.addView(logo);

        final ViewGroup root = findViewById(android.R.id.content);
        root.addView(overlay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(200);
        fadeIn.start();

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] checker = new Runnable[1];
        checker[0] = () -> {
            WebView webView = getBridge() != null ? getBridge().getWebView() : null;
            boolean loaded = webView != null && webView.getProgress() >= 100;
            long elapsed = System.currentTimeMillis() - startTime;

            if ((loaded && elapsed >= MIN_DISPLAY_MS) || elapsed >= MAX_DISPLAY_MS) {
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
                fadeOut.setDuration(300);
                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        root.removeView(overlay);
                    }
                });
                fadeOut.start();
            } else {
                handler.postDelayed(checker[0], POLL_INTERVAL_MS);
            }
        };
        handler.postDelayed(checker[0], POLL_INTERVAL_MS);
    }
}
