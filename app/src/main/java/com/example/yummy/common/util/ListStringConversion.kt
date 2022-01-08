package com.example.yummy.common.util

import java.lang.StringBuilder

object ListStringConversion {

    fun listToString(list: List<String>): String {
        val finalString = StringBuilder()

        for (item in list) {
            finalString.append("$item,")
        }

        return finalString.toString()
    }

    fun stringToList(string: String?): List<String> {
        val list:ArrayList<String> = ArrayList()

        string?.let {
            for (item in it.split(",")) {
                list.add(item)
            }
        }

        return list
    }
}