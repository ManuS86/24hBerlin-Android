# --- 1. Core Config & Attributes ---
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*, RuntimeVisibleAnnotations
-dontwarn okio.**
-dontwarn javax.annotation.**

# --- 2. App-Specific Rules (Data & UI) ---
# Protects your package members for Moshi/Firebase.
-keepclassmembers class com.esutor.twentyfourhoursberlin.** {
    <init>(...);
    <fields>;
    <methods>;
}

# Protect the names of your UI and Navigation classes.
-keepnames class com.esutor.twentyfourhoursberlin.ui.** { *; }
-keepnames class com.esutor.twentyfourhoursberlin.navigation.** { *; }

# --- 3. UI, Navigation & Runtime Library Rules ---
-keepclassmembers class androidx.navigation.** { *; }
-keepclassmembers class androidx.compose.material3.** { *; }
-keepclassmembers class androidx.compose.runtime.** { *; }

# Protect Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
    @androidx.compose.runtime.ReadOnlyComposable *;
}

# Protect Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# --- 4. Moshi & Retrofit ---
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepnames interface retrofit2.** { *; }

# --- 5. Firebase & Dependencies ---
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
    @com.google.firebase.firestore.Exclude <fields>;
}

-dontwarn com.google.firebase.firestore.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn com.google.j2objc.annotations.**
-dontwarn com.google.common.**