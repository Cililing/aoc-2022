package extractor

import kotlin.test.Test
import kotlin.test.assertEquals

class ExtractorTest {

    @Test
    fun `test multiple nextInt`() {
        val resource = "move 4 from 5 to 7"
        val extractor = Extractor(resource)

        val n1 = extractor.nextInt()
        val n2 = extractor.nextInt()
        val n3 = extractor.nextInt()

        assertEquals(4, n1)
        assertEquals(5, n2)
        assertEquals(7, n3)
    }

    @Test
    fun `test different methods`() {
        val resource = "Operation: new = old * 19"
        val extractor = Extractor(resource)

        val n1 = extractor.nextChar(setOf('*'))
        val n2 = extractor.nextInt()

        assertEquals('*', n1)
        assertEquals(19, n2)
    }

    @Test
    fun `test get list of ints`() {
        val resource = "  Starting items: 54, 82, 90, 88, 86, 54"
        val extractor = Extractor(resource)

        val n = extractor.nextInts()

        assertEquals(listOf(54, 82, 90, 88, 86, 54), n)
    }
}
