package org.cubewhy.reverse.analyzer

class StringCursor(private val string: String) {
    private var cursor = 0

    fun move(size: Int): String {
        if (cursor + size > string.length) {
            throw IndexOutOfBoundsException("Cannot move beyond end of string")
        }
        // get data
        val data = string.substring(cursor, cursor + size)
        // move the pointer
        cursor += size
        return data
    }

    fun moveUntil(query: String): String {
        // index the next character
        val data = string.substring(cursor)
        val size = data.indexOf(query) + 1
        return move(size)
    }

    fun parseNextDescriptor(): String? {
        val start = currentOffset()
        val ch = peek(1).firstOrNull() ?: return null

        return when (ch) {
            'B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 'V' -> {
                move(1) // primitive type
            }

            '[' -> {
                move(1)
                parseNextDescriptor() // recursively parse the element type
                string.substring(start, currentOffset())
            }

            'L' -> {
                move(1)
                val semi = string.indexOf(';', currentOffset())
                if (semi == -1) throw Exception("Unterminated class descriptor")
                val size = semi - currentOffset() + 1
                move(size)
                string.substring(start, currentOffset())
            }

            else -> throw Exception("Unknown descriptor prefix: '$ch'")
        }
    }

    fun moveToEnd(): String {
        return move(string.length - cursor)
    }

    fun peek(size: Int): String {
        if (cursor + size > string.length) return string.substring(cursor)
        return string.substring(cursor, cursor + size)
    }

    fun hasNext(): Boolean {
        return cursor < string.length
    }

    fun currentOffset(): Int = cursor

    override fun toString(): String {
        return "StringCursor(string='$string', cursor=$cursor)"
    }

}

fun String.toCursor() = StringCursor(this)