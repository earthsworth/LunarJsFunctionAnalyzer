import org.cubewhy.reverse.analyzer.StringCursor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringCursorTests {
    @Test
    fun `parse desc with cursor`() {
        val cursor = StringCursor("(Ljava/lang/Integer;Ljava/lang/String;I)Ljava/lang/String;")
        val leftBracket = cursor.move(1)
        assertEquals("(", leftBracket)

        // params
        val paramTypes = cursor.moveUntil(")")
        val cursor2 = StringCursor(paramTypes)

        // Ljava/lang/Integer;
        val firstType = cursor2.parseNextDescriptor()
        assertEquals("Ljava/lang/Integer;", firstType)
        // Ljava/lang/String;
        val secondType = cursor2.parseNextDescriptor()
        assertEquals("Ljava/lang/String;", secondType)
        // I
        val thirdType = cursor2.parseNextDescriptor()
        assertEquals("I", thirdType)
    }
}