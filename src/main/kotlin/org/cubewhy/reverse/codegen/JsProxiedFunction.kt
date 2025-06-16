package org.cubewhy.reverse.codegen

data class JsProxiedFunction(
    val functionName: String,
    val descriptor: MethodDescriptor,

    val namespace: String?
) /* {
    fun toTsFunctionDeclaration(): String {
        val args = descriptor.parameterTypes.withIndex().joinToString(", ") { (i, type) ->
            "arg$i: ${type.toTsType()}"
        }
        val returnType = descriptor.returnType.toTsType()
        return "$functionName($args): $returnType;"
    }
} */
