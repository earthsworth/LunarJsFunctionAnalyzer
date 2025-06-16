package org.cubewhy.reverse.codegen.struct

data class EnumStruct(
    val name: String,
    val entries: Set<String>
): Struct {
    override fun toTypescriptType(): String {
        val quotedEntries = entries.joinToString(" | ") { "'$it'" }
        return "export type $name = $quotedEntries;"
    }
}