package com.michaelflisar.composedebugdrawer.plugin.kotpreferences

import com.michaelflisar.kotpreferences.core.interfaces.StorageSetting
import java.util.regex.Pattern

fun <T> StorageSetting<T>.getDebugLabel(): String {
    return key.let {
        var trimmed = it.replaceFirstChar { it.uppercase() }
        val stringToRemove = emptyList<String>()//listOf("debug", "tmp", "temp")
        stringToRemove.forEach {
            if (trimmed.startsWith(it, true))
                trimmed = trimmed.substring(it.length)
        }
        val words = trimmed.split(Pattern.compile("(?=\\p{Lu})"))
        // einzelne nun aufgesplittete Großbuchstaben wieder zusammenfassen
        // bspw. "NY" in "debugTestNYCity"
        val words2 = mutableListOf<String>()
        var tmp = ""
        words.forEach {
            if (it.length == 1 && it == it.uppercase()) {
                tmp += it
            } else {
                if (tmp.isNotEmpty()) {
                    words2.add(tmp)
                    tmp = ""
                }
                words2.add(it)
            }
        }
        if (tmp.isNotEmpty()) {
            words2.add(tmp)
            tmp = ""
        }
        //L.d { "trimmed = $trimmed | words = ${words.size} (${words.joinToString(";")})" }
        //L.d { "trimmed = $trimmed | words2 = ${words2.size} (${words2.joinToString(";")})" }
        words2.joinToString(" ")
    }
}