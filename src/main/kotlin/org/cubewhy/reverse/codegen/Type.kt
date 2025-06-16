package org.cubewhy.reverse.codegen

import org.cubewhy.reverse.utils.jsName

data class Type(
    val name: String,
    val isBaseType: Boolean,
    val isArray: Boolean = false,
) {
    val isKnownType: Boolean
        get() = isBaseType || name.startsWith("java/")

    fun toTsType(): String {
        val base = when (name) {
            "void" -> "void"
            "java/lang/String" -> "string"
            "java/lang/Boolean" -> "boolean"
            "java/lang/Integer", "int" -> "number"
            "java/lang/Long", "long" -> "number"
            "java/lang/Double", "double" -> "number"
            "java/lang/Float", "float" -> "number"
            "java/util/UUID" -> "string"
            else -> jsName(name)
        }
        return if (isArray) "$base[]" else base
    }
}