package org.cubewhy.reverse.codegen

data class MethodDescriptor(
    val parameterTypes: List<Type>,
    val returnType: Type
)