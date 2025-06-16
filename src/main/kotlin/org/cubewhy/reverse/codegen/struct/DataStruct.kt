package org.cubewhy.reverse.codegen.struct

import org.cubewhy.reverse.codegen.Type

data class DataStruct(
    val name: String,
    val fields: List<Field>
): Struct {
    data class Field(
        val name: String,
        val type: Type
    )


    override fun toTypescriptType(): String {
        return """
            interface $name {
            ${fields.joinToString(";\n") { "  ${it.name}?: ${it.type.toTsType()}" }}
            }
        """.trimIndent()
    }
}
