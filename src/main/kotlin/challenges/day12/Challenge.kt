package challenges.day12

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(12)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<Vertex> {
        return input.mapIndexed { x, line ->
            line.mapIndexed { y, ch -> Vertex(x, y, ch) }
        }.flatten()
    }

    @Task("ex1")
    fun ex1(input: List<Vertex>): Double {
        val graph = createGraph(input)

        val start = input.find { it.c == 'S' } ?: throw IllegalStateException("start vertex not found")
        val end = input.find { it.c == 'E' } ?: throw IllegalStateException("end vertex not found")

        val dijkstra = DijkstraShortestPath(graph)
        val path = dijkstra.getPath(start.id, end.id)

        return path.weight
    }

    @Task("ex2")
    fun ex2(input: List<Vertex>): Double {
        val graph = createGraph(input)

        val starts = input.filter { it.c in setOf('S', 'a') }
        val end = input.find { it.c == 'E' } ?: throw IllegalStateException("end vertex not found")

        val dijkstra = DijkstraShortestPath(graph)
        val weights = starts.map {
            dijkstra.getPath(it.id, end.id)?.weight ?: Double.POSITIVE_INFINITY
        }

        return weights.min()
    }

    private fun createGraph(input: List<Vertex>): SimpleDirectedGraph<String, DefaultEdge> {
        val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)

        input.forEach { graph.addVertex(it.id) }

        input.forEach { source ->
            source.possibleMoves().forEach { targetPos ->
                val target = input.find { it.x == targetPos.first && it.y == targetPos.second }
                    ?: return@forEach

                if (source.canGo(target)) {
                    graph.addEdge(source.id, target.id)
                }
            }
        }

        return graph
    }
}
