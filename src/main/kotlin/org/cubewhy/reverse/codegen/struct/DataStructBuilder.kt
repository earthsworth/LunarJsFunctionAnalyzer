package org.cubewhy.reverse.codegen.struct

import org.cubewhy.reverse.codegen.Type

class DataStructBuilder private constructor(val className: String) {
    companion object {
        fun newBuilder(className: String): DataStructBuilder {
            return DataStructBuilder(className)
        }
    }

    private val fields = mutableListOf<DataStruct.Field>()

    fun addField(name: String, type: Type) {
        fields.add(DataStruct.Field(name, type))
    }

    fun build(): DataStruct {
        return DataStruct(className, fields)
    }
}