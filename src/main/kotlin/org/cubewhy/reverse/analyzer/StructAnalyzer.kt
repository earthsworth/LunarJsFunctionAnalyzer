package org.cubewhy.reverse.analyzer

import org.cubewhy.reverse.codegen.JsProxiedFunction
import org.cubewhy.reverse.codegen.struct.EnumStruct
import org.cubewhy.reverse.codegen.struct.Struct
import org.cubewhy.reverse.utils.jsName
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.jar.JarFile

class StructAnalyzer(file: File) : AutoCloseable {
    private val jarFile = JarFile(file)

    fun analysis(functions: List<JsProxiedFunction>): List<Struct> {
        val classNodes = readToClassNodes(functions)
        val structs = mutableListOf<Struct>()

        classNodes.forEach { classNode ->
            if (classNode.superName == "java/lang/Enum") {
                // enum class
                val struct = parseEnum(classNode)
                structs.add(struct)
            }
            // TODO parse data classes
        }
        return structs
    }

    private fun parseEnum(classNode: ClassNode): EnumStruct {
        val entries = classNode.fields.filter {
            it.desc.toCursor().parseNextDescriptor() == "L${classNode.name};"
        }.map { fieldNode ->
            fieldNode.name
        }
        return EnumStruct(jsName(classNode.name), entries.toSet())
    }

    private fun readToClassNodes(functions: List<JsProxiedFunction>): List<ClassNode> =
        functions
            .flatMap { it.descriptor.parameterTypes }
            .filter { !it.isKnownType }
            .mapNotNull { jarFile.getJarEntry(it.name + ".class") }
            .map { ClassNode().apply { ClassReader(jarFile.getInputStream(it)).accept(this, ClassReader.SKIP_FRAMES) } }

    override fun close() {
        jarFile.close()
    }
}