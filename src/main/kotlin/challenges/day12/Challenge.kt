package challenges.day12

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import runner.Challenge
import runner.Mapper
import runner.Task

data class Vertex(val x: Int, val y: Int, val c: Char) {
    val id = "$x:$y"
    override fun toString(): String {
        return "$x:$y:$c"
    }

    fun possibleMoves(): Set<Pair<Int, Int>> {
        return listOf(
            Pair(x - 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y),
        ).filter { it.first >= 0 && it.second >= 0 }.toSet()
    }
}

@Challenge(12)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<List<Vertex>> {
        return input.mapIndexed { x, line ->
            line.mapIndexed { y, ch -> Vertex(x, y, ch) }
        }
    }

    @Task("ex1")
    fun ex1(input: List<List<Vertex>>): Double {
        val graph = createGraph(input)

        val start = input.flatten().find { it.c == 'S' } ?: throw IllegalStateException("start vertex not found")
        val end = input.flatten().find { it.c == 'E' } ?: throw IllegalStateException("end vertex not found")

        val dijkstra = DijkstraShortestPath(graph)
        val path = dijkstra.getPath(start.id, end.id)

        return path.weight
    }

    @Task("ex2")
    fun ex2(input: List<List<Vertex>>): Double {
        val graph = createGraph(input)

        val starts = input.flatten().filter { it.c in setOf('S', 'a') }
        val end = input.flatten().find { it.c == 'E' } ?: throw IllegalStateException("end vertex not found")

        val dijkstra = DijkstraShortestPath(graph)
        val weights = starts.map {
            dijkstra.getPath(it.id, end.id)?.weight ?: Double.POSITIVE_INFINITY
        }

        return weights.min()
    }

    private fun createGraph(input: List<List<Vertex>>): SimpleDirectedGraph<String, DefaultEdge> {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)

        input.flatten().forEach { graph.addVertex(it.id) }

        input.flatten().forEach { source ->
            val adj = source.possibleMoves()
            adj.forEach adj@{ targetPos ->
                val target = input.getOrNull(targetPos.first)?.getOrNull(targetPos.second) ?: return@adj

                if (source.c == 'S' && target.c == 'a') { // start, can go to a
                    graph.addEdge(source.id, target.id)
                    return@adj
                }

                if (source.c == 'z' && target.c == 'E') { // finish, can go here from z
                    graph.addEdge(source.id, target.id)
                    return@adj
                }

                if (target.c.code - source.c.code in setOf(0, 1)) { // climb (up to 1)
                    graph.addEdge(source.id, target.id)
                }

                if (target.c.code <= source.c.code) { // go down, any height
                    graph.addEdge(source.id, target.id)
                }
            }
        }

        return graph
    }
}
