package org.cubewhy.reverse.analyzer

import org.cubewhy.reverse.codegen.JsProxiedFunction
import org.cubewhy.reverse.codegen.NamespaceMapper
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

class ClassAnalyzer(private val cn: ClassNode) {
    fun analysis(): List<JsProxiedFunction> {
        val jsFunctions = filterJsFunction(cn.methods);
        return jsFunctions.map { declaration ->
            // parse desc
            val methodDescriptorParser = MethodDescriptorParser(declaration.node.desc)
            JsProxiedFunction(declaration.functionName, methodDescriptorParser.parse(), NamespaceMapper.mapNamespace(cn.name))
        }
    }

    private fun filterJsFunction(methods: List<MethodNode>): List<JsFunctionDeclaration> =
        methods.mapNotNull { method ->
            method.visibleAnnotations
                ?.lastOrNull { it.desc == "Lcom/moonsworth/webosr/javascript/JsFunction;" }
                ?.let { annotation ->
                    JsFunctionDeclaration(method, annotation.values[1] as String)
                }
        }

    private data class JsFunctionDeclaration(
        val node: MethodNode,
        val functionName: String,
    )
}