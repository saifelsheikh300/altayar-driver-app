package com.altayar.driver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.getcapacitor.BridgeActivity;

/**
 * تمت إضافة هذا الملف بواسطة Claude فوق قالب Capacitor الافتراضي (كان فاضي أصلاً).
 * سبلاش خفيفة جداً: فيد إن سريع للوجو، وقفة بسيطة، وفيد آوت — من غير أي حركة زيادة.
 */
public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLightSplash();
    }

    private void showLightSplash() {
        final float density = getResources().getDisplayMetrics().density;

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

        ViewGroup root = findViewById(android.R.id.content);
        root.addView(overlay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(200);

        overlay.postDelayed(() -> {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
            fadeOut.setDuration(200);
            fadeOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    root.removeView(overlay);
                }
            });
            fadeOut.start();
        }, 500);

        fadeIn.start();
    }
}
