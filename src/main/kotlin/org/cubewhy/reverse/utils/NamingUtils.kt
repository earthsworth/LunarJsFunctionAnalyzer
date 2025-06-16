package org.cubewhy.reverse.utils

fun String.toCamelCase(): String {
    return this.split('_').mapIndexed { index, part ->
        if (index == 0) part.lowercase()
        else part.replaceFirstChar { it.uppercaseChar() }
    }.joinToString("")
}

fun jsName(name: String): String =
    name.replace("/", "_").replace("$", "")