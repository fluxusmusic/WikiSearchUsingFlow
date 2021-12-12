package com.mc.kim.remote.annotation

/**
 * Annotation for check key
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonValue(val filedName:String)