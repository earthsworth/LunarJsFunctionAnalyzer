package org.cubewhy.reverse.codegen
//
//fun generateNamespaceTs(functions: List<JsProxiedFunction>): String {
//    data class Node(
//        val children: MutableMap<String, Node> = mutableMapOf(),
//        val functions: MutableList<JsProxiedFunction> = mutableListOf()
//    )
//
//    val root = Node()
//
//    for (fn in functions) {
//        val path = buildList {
//            add("lunar")
//            if (fn.namespace != null) addAll(fn.namespace.split('.'))
//        }
//        var current = root
//        for (segment in path) {
//            current = current.children.getOrPut(segment) { Node() }
//        }
//        current.functions.add(fn)
//    }
//
//    fun render(node: Node, indent: String = ""): String {
//        val sb = StringBuilder()
//        for ((name, child) in node.children) {
//            sb.append(indent + "export namespace $name {\n")
//            sb.append(render(child, "$indent    "))
//            sb.append("$indent}\n")
//            sb.append("\n")
//        }
//        for (fn in node.functions) {
//            sb.append(indent + fn.toTsFunctionDeclaration() + "\n")
//        }
//        return sb.toString()
//    }
//
//    return render(root)
//}

fun generateNamespaceTs(functions: List<JsProxiedFunction>): String {
    data class Node(
        val children: MutableMap<String, Node> = mutableMapOf(),
        val functions: MutableList<JsProxiedFunction> = mutableListOf()
    )

    val root = Node()

    for (fn in functions) {
        val path = fn.namespace?.split('.') ?: emptyList()
        var current = root
        for (segment in path) {
            current = current.children.getOrPut(segment) { Node() }
        }
        current.functions.add(fn)
    }

    fun renderInterface(node: Node, interfaceName: String = "LunarNamespace", indent: String = "    "): String {
        val sb = StringBuilder()
        sb.append("interface $interfaceName {\n")

        for ((name, child) in node.children) {
            sb.append("$indent$name?: {\n")
            sb.append(renderInterface(child, "", "$indent    ").removePrefix("interface  {\n").removeSuffix("}\n"))
            sb.append("$indent};\n")
        }

        for (fn in node.functions) {
            sb.append("$indent${fn.toTsFunctionDeclaration()}\n")
        }

        sb.append("}\n")
        return sb.toString()
    }

    val interfaceCode = renderInterface(root)

    return """
$interfaceCode

declare global {
    interface Window {
        lunar: LunarNamespace;
    }
}
    """.trimIndent()
}
