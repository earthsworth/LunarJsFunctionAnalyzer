package org.cubewhy.reverse.codegen.struct

data class EnumStruct(
    val name: String,
    val entries: Set<String>
): Struct {
    override fun toTypescriptType(): String {
        return """
enum $name {
${entries.joinToString(",\n") { "  $it" }}
}
        """.trimIndent()
    }
}