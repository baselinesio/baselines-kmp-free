-keepnames class * extends io.baselines.sample.ui.navigation.NavRoute
-keep,includedescriptorclasses class io.baselines.sample.**$$serializer { *; }
-keepclassmembers class * extends io.baselines.sample.ui.navigation.NavRoute {
    *** Companion;
}
-keepclasseswithmembers class  * extends io.baselines.sample.ui.navigation.NavRoute {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class kotlin.Metadata
