# -------------------------------------------------------------------------
# GSON & RETROFIT RULES
# -------------------------------------------------------------------------
# Keep generic signature information and annotations
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Keep Gson's internal classes safe
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe
-keep class com.google.gson.stream.** { *; }

# Prevent ProGuard from renaming our Network Data Transfer Objects (DTOs)
-keep class dev.yash.dynamicweatherapp.data.remote.dto.** { *; }

# -------------------------------------------------------------------------
# ROOM DATABASE RULES
# -------------------------------------------------------------------------
# Protect your local database entities from being obfuscated
-keep class dev.yash.dynamicweatherapp.data.local.entity.** { *; }

# -------------------------------------------------------------------------
# JETPACK GLANCE (WIDGET) RULES
# -------------------------------------------------------------------------
# Keep the Widget Receiver so Android can find it on the Home Screen
-keep class dev.yash.dynamicweatherapp.presentation.widget.WeatherWidgetReceiver { *; }