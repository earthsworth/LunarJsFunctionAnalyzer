package org.cubewhy.reverse.analyzer

import org.cubewhy.reverse.codegen.MethodDescriptor
import org.cubewhy.reverse.codegen.Type


class MethodDescriptorParser(private val descriptor: String) {
    companion object {
        private val baseTypeMap = mapOf<String, String>(
            "B" to "byte",
            "C" to "char",
            "D" to "double",
            "F" to "float",
            "I" to "int",
            "J" to "long",
            "S" to "short",
            "Z" to "boolean",
            "V" to "void",
        )
    }

    fun parse(): MethodDescriptor {
        val cursor = descriptor.toCursor()
        cursor.move(1) // (
        val paramTypeCursor = cursor.moveUntil(")").removeSuffix(")").toCursor()

        val types = if (paramTypeCursor.hasNext()) {
            val descriptors = generateSequence {
                paramTypeCursor.parseNextDescriptor()
            }

            descriptors.map {
                convertType(it)
            }.toList()
        } else {
            emptyList()
        }

        // parse return type
        val returnType = convertType(cursor.moveToEnd())
        return MethodDescriptor(types, returnType)
    }

    private fun convertType(rawType: String, isArray: Boolean = false): Type {
        val type = rawType.removeSuffix(";");

        if (type.startsWith("[")) {
            // parse array
            return convertType(type.substring(1), isArray = true)
        }

        if (!type.startsWith("L")) {
            // try to find in the baseType map
            return Type(baseTypeMap[type]!!, true, isArray)
        }
        // parse custom type
        val typeClass = type.removePrefix("L");
        return Type(typeClass, false, isArray)
    }
}