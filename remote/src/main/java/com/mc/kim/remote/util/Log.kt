package com.mc.kim.remote.util

import android.util.Log
import com.mc.kim.remote.BuildConfig

class Log {
    companion object{
        val INCLUDE: Boolean = BuildConfig.DEBUG

        fun v(tag: String?, message: String?) {
            if (INCLUDE) {
                val tags = getTags(tag)
                for (str in tags!!) {
                    Log.v(str, message!!)
                }
            }
        }

        fun i(tag: String?, message: String?) {
            if (INCLUDE) {
                val tags = getTags(tag)
                for (str in tags!!) {
                    Log.i(str, message!!)
                }
            }
        }

        fun d(tag: String?, message: String?) {
            if (INCLUDE) {
                val tags = getTags(tag)
                for (str in tags!!) {
                    Log.d(str, message!!)
                }
            }
        }

        fun w(tag: String?, message: String?) {
            if (INCLUDE) {
                val tags = getTags(tag)
                for (str in tags!!) {
                    Log.w(str, message!!)
                }
            }
        }

        fun e(tag: String?, message: String?) {
            if (INCLUDE) {
                val tags = getTags(tag)
                for (str in tags!!) {
                    Log.e(str, message!!)
                }
            }
        }

        private fun getTags(tag: String?): Array<String>? {
            return if (tag != null) {
                if (tag.indexOf("|") > 0) {
                    tag.split("\\|").toTypedArray()
                } else {
                    arrayOf(tag)
                }
            } else null
        }
    }

}