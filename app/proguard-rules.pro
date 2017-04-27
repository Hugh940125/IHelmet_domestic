# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files\android_sdk_win64/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes InnerClasses
-dontoptimize

#-keep class com.junkchen.blelib.** { *; }
#-keep class cn.aigestudio.datepicker.** { *; }
#-keep class com.shizhefei.** { *; }
#-keep class com.uuzuche.lib_zxing.** { *; }
#-keep class cn.carbswang.android.numberpickerview.library.** { *; }

#信鸽推送
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

#引用了v4或者v7包
-dontwarn android.support.**

############混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keep class HttpUtils;
-keep class jacksonUtils;
-keep class NetUtils;
-keep class ObjToMap;
-keep class ResultData;