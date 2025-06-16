package org.cubewhy.reverse.codegen.struct

class EnumStructBuilder private constructor(val name: String) {
    companion object {
        fun newBuilder(name: String): EnumStructBuilder {
            return EnumStructBuilder(name)
        }
    }

    private val entries = mutableSetOf<String>("UNSPECIFIED")

    fun entry(name: String) {
        entries.add(name)
    }

    fun build(): EnumStruct {
        return EnumStruct(name, entries)
    }
}