package org.cubewhy.reverse

import org.cubewhy.reverse.analyzer.JsProxiedFunctionAnalyzer
import org.cubewhy.reverse.analyzer.StructAnalyzer
import org.cubewhy.reverse.codegen.generateNamespaceTs
import java.io.File

fun main() {
    val lunarJarFile = File("lunar.jar")
    // init FileAnalyzer
    val jsProxiedFunctionAnalyzer = JsProxiedFunctionAnalyzer(lunarJarFile)
    // analysis proxied functions
    val jsProxiedFunctions = jsProxiedFunctionAnalyzer.analysis()

    // analysis data classes
    val structAnalyzer = StructAnalyzer(lunarJarFile)
    val types = structAnalyzer.analysis(jsProxiedFunctions)
    println("// entities.ts")
    println(types.joinToString("\n\n") { it.toTypescriptType() })

    println("// lunar.d.ts")
    println(generateNamespaceTs(jsProxiedFunctions))
}