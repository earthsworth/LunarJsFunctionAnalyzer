package org.cubewhy.reverse.codegen

data class MethodDescriptor(
    val types: List<Type>,
    val returnType: Type
)