package org.cubewhy.reverse.analyzer

import org.cubewhy.reverse.codegen.JsProxiedFunction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.jar.JarFile

class JsProxiedFunctionAnalyzer(file: File) {
    private val jarFile = JarFile(file)

    /**
     * Run analysis on a file
     * */
    fun analysis(): List<JsProxiedFunction> {
        val functions = mutableListOf<JsProxiedFunction>()
        jarFile.use {
            for (entry in it.entries()) {
                if (!entry.name.endsWith(".class")) {
                    // skip non java class
                    continue
                }
                val cn = ClassNode()
                // read bytecode
                ClassReader(it.getInputStream(entry)).accept(cn, ClassReader.SKIP_FRAMES)
                // initialize ClassAnalyzer
                val classAnalyzer = ClassAnalyzer(cn)
                functions.addAll(classAnalyzer.analysis())
            }
        }
        return functions
    }
}