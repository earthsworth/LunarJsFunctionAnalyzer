package org.cubewhy.reverse.codegen

data class JsProxiedFunction(
    val functionName: String,
    val descriptor: MethodDescriptor,

    val namespace: String?
) {
    fun toTsFunctionDeclaration(): String {
        val args = descriptor.types.withIndex().joinToString(", ") { (i, type) ->
            "arg$i: ${type.toTsType()}"
        }
        val returnType = descriptor.returnType.toTsType()
        return "function $functionName($args): $returnType;"
    }
}
