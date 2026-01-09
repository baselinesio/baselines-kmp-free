-keepnames class * extends io.baselines.sample.domain.api.NavRoute
-keep,includedescriptorclasses class io.baselines.sample.**$$serializer { *; }
-keepclassmembers class * extends io.baselines.sample.domain.api.NavRoute {
    *** Companion;
}
-keepclasseswithmembers class  * extends io.baselines.sample.domain.api.NavRoute {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class kotlin.Metadata
