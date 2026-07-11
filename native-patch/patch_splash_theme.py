#!/usr/bin/env python3
"""
كباسيتور بيولّد ستايل السبلاش باستخدام android:background بس، وده بيشتغل قبل
أندرويد 12، لكن أندرويد 12+ ليه نظام SplashScreen خاص بيه بيتجاهل ده ويعرض
خلفية سودة افتراضية + أيقونة التطبيق مربعة. السكريبت ده بيضيف الخاصيتين
الصح (windowSplashScreenBackground / windowSplashScreenAnimatedIcon) عشان
أندرويد 12+ يعرض نفس الشاشة البرتقالي زي باقي الإصدارات، من غير ما يمسح
أو يغيّر أي حاجة تانية في الملف.
"""
import re
import sys

path = "android/app/src/main/res/values/styles.xml"

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

target = '<style name="AppTheme.NoActionBarLaunch" parent="Theme.SplashScreen">'
if target not in content:
    print("لم يتم إيجاد الستايل المطلوب، لم يتم تعديل أي شيء.")
    sys.exit(1)

new_items = (
    '\n        <item name="windowSplashScreenBackground">#FD5003</item>'
    '\n        <item name="windowSplashScreenAnimatedIcon">@mipmap/ic_launcher</item>'
    '\n        <item name="windowSplashScreenAnimationDuration">200</item>'
)

content = content.replace(target, target + new_items, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("تم تعديل ستايل السبلاش بنجاح.")
