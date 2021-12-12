package com.mc.kim.remote.util

import com.mc.kim.remote.annotation.JsonValue
import org.json.JSONArray
import org.json.JSONObject
import java.lang.RuntimeException
import java.lang.StringBuilder
import kotlin.reflect.*
import kotlin.reflect.full.memberProperties

/**
 * Json Parser
 */
class JsonParser {
    private val TAG = JsonParser::class.simpleName

    /**
     * parser for Object to String.
     */
    fun <T : Any> fromJson(data: T): String {
        val kClass = data::class
        val members = kClass.memberProperties
        val stringBuilder = StringBuilder()
        for ((index, member) in members.withIndex()) {
            val name = member.name
            val value = member.getter.call(data) ?: continue
            when (value) {
                is String -> {
                    stringBuilder.append("\"${name}\":\"${value}\"")
                }
                is Int, Long, Double, Float, Boolean -> {
                    stringBuilder.append("\"${name}\":${value}")
                }
                else -> {
                    stringBuilder.append("\"${name}\":${fromJson(value)}")
                }
            }
            if(index != members.size -1){
                stringBuilder.append(",")
            }
        }
        val result = "{${stringBuilder}}"
        return result
    }

    /**
     * parser for String to Object.
     * @param data: input string
     * @param className: class type for make object
     * @throws RuntimeException, occurred during the parsing
     */
    @Throws(RuntimeException::class)
    fun <T : Any> toJson(data: String, className: KClass<T>): T {
        val jsonObject = JSONObject(data)
        val instance = className.constructors
        val typeParameters = instance.first().parameters
        var constructorFiled: HashMap<KParameter, Any?> = HashMap()
        for (parameter in typeParameters) {
            val name = parameter.name
            val key = className.members.getJsonValueKey(name!!)
            val returnType = parameter.type

            if (jsonObject.isNull(key)) {
                when (returnType.toString()) {
                    "kotlin.String" -> {
                        constructorFiled[parameter] = ""
                    }
                    "kotlin.Int", "kotlin.Long", "kotlin.Float", "kotlin.Double" -> {
                        constructorFiled[parameter] = 0
                    }
                    "kotlin.Boolean" -> {
                        constructorFiled[parameter] = false
                    }
                    else -> {
                        constructorFiled[parameter] = null
                    }
                }
            } else {
                val filed = jsonObject.opt(key)
                when (filed) {
                    is String -> {
                        constructorFiled[parameter] = filed
                    }
                    is Int, Long, Double, Float, Boolean -> {
                        constructorFiled[parameter] = filed
                    }
                    is JSONArray -> {
                        var arguments = returnType.arguments
                        var argumentType = arguments[0].type

                        val size = filed.length()
                        var list = arrayListOf<Any>()
                        for (index in 0 until size) {
                            list.add(
                                toJson(
                                    filed[index].toString(),
                                    argumentType!!.classifier as KClass<*>
                                )
                            )

                        }
                        constructorFiled[parameter] = list
                    }
                    is JSONObject -> {

                        constructorFiled[parameter] = toJson(
                            filed.toString(),
                            returnType.classifier as KClass<*>
                        )
                    }
                }
            }
        }
        return instance!!.first().callBy(constructorFiled)
    }

    private fun Collection<KCallable<*>>.getJsonValueKey(name: String): String {
        for (member in this) {
            if (member.name != name) {
                continue
            }
            val annotations = member.annotations
            for (annotation in annotations) {
                if (annotation is JsonValue) {
                    return annotation.filedName
                }
            }
        }

        return name
    }
}