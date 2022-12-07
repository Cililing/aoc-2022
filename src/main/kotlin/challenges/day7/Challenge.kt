package challenges.day7

import runner.Challenge
import runner.Mapper
import runner.Task

data class Path(val path: String = "/") {
    fun append(p: String) = if (path.endsWith('/')) Path("$path$p") else Path("$path/$p")
    fun up(): Path {
        val possible = path.dropLastWhile { it != '/' }
        if (possible.endsWith('/') && possible.length > 1) {
            return Path(possible.dropLast(1))
        }
        return Path(possible)
    }

    fun canUp() = path != "/"

    fun allPaths(): List<Path> {
        val all = mutableListOf<Path>()
        var current = this
        while (current.canUp()) {
            all.add(current)
            current = current.up()
        }
        all.add(Path()) // root
        return all
    }
}

@Challenge(7)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Map<Path, Int> {
        // map of all files on the disk
        // no directories included
        // fold over fullFilePath to its size
        return input.drop(1).fold(Pair(Path(), mapOf<Path, Int>())) { acc, cmd ->
            when {
                cmd.startsWith("$ ls") -> {
                    return@fold acc.first to acc.second
                }

                cmd.startsWith("$ cd ") -> {
                    val dir = cmd.drop(5) // drop "$ cd "

                    if (dir == "..") {
                        return@fold acc.first.up() to acc.second
                    } else {
                        return@fold acc.first.append(dir) to acc.second
                    }
                }

                else -> {
                    // skip dirs path, get only filepaths
                    if (!cmd.startsWith("dir")) {
                        val cmdParse = cmd.split(" ")
                        return@fold acc.first to acc.second.toMutableMap().apply {
                            this[acc.first.append(cmdParse[1])] = cmdParse[0].trim().toInt()
                        }
                    }
                }
            }

            acc.first to acc.second
        }.second
    }

    @Task("ex1")
    fun ex1(input: Map<Path, Int>): Int {
        return directoryTotalSum(input).filterValues { it < 100000 }.values.sum()
    }

    @Task("ex2")
    fun ex2(input: Map<Path, Int>): Int {
        val result = directoryTotalSum(input)
        val spaceTaken = result[Path("/")]!!
        val freeSpace = 70_000_000 - spaceTaken
        val requiredSpace = 30_000_000 - freeSpace

        return result
            .filter { it.value >= requiredSpace }
            .values
            .minOf { it }
    }

    private fun directoryTotalSum(input: Map<Path, Int>): Map<Path, Int> {
        return input.toList().fold(mutableMapOf()) { acc, file ->
            acc.apply {
                val allPaths = file.first.allPaths().sortedBy { it.path.length }.dropLast(1) // drop file path
                allPaths.forEach {
                    val currentSize = this.getOrDefault(it, 0)
                    this[it] = currentSize + file.second
                }
            }
        }
    }
}
