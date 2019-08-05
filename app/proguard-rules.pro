-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
-verbose


# Basic elements
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.google.gson.Gson

# Androidx
-keep class androidx.core.app.CoreComponentFactory { *; }

# for views
-keep public class * extends View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
}

# Enumerations
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


# DEBUG MODE: set line numbers and also stacktrace
-printmapping outputfile.txt

-dontwarn com.fasterxml.jackson.databind.**
-dontwarn retrofit.**
-dontwarn retrofit2.**
-dontwarn com.squareup.okhttp.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn org.slf4j.**
-dontwarn fm.**
-dontwarn org.bouncycastle.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.google.**
-dontwarn avp8.**
-dontwarn aopus.**
-dontwarn aaudioprocessing.**


-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit.http.* <methods>;
}

-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep class com.viewpagerindicator.** { *; }
-keep class okio.** { *; }
-keep class okhttp3.** { *; }
-keep class de.idnow.sdk.** { *; }
-keep class com.opentok.** { *; }
-keep class org.webrtc.** { *; }
-keep class com.ning.http.** { *; }
-keep class fm.** { *; }
-keep class org.bouncycastle.** { *; }
-keep class com.google.** { *; }
-keep class aopus.** { *; }
-keep class aaudioprocessing.** { *; }


-keepattributes *Annotation*
-keepattributes SourceFile, LineNumberTable

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-dontwarn com.google.errorprone.annotations.**

-dontwarn com.google.android.gms.**

# Kotlin Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Gipg library
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
-keepclasseswithmembernames class * {
    native <methods>;
}


-keep public class com.currencytrackingapp.data.models.** { *; }

