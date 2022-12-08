package challenges.day7

import path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class PathTest {

    @Test
    fun `all paths test`() {
        val p = Path("/dir/dir2/h.app")

        val expected = listOf(
            Path("/"),
            Path("/dir"),
            Path("/dir/dir2"),
            Path("/dir/dir2/h.app")
        ).sortedBy { it.path.length }

        val result = p.allPaths().sortedBy { it.path.length }
        assertEquals(expected, result)
    }

    @Test
    fun `up test`() {
        val p = Path("/dir/dir2/h.app")
        val expected = Path("/dir/dir2")
        val result = p.up()

        assertEquals(expected, result)
    }
}
