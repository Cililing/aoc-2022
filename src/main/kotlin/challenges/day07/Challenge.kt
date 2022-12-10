package challenges.day07

import path.Path
import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(7)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<String> {
        return input
    }

    @Task("ex1")
    fun ex1(input: List<String>): Int {
        return directoryTotalSum(getAllFiles(input))
            .values
            .filter { it < 100000 }
            .sum()
    }

    @Task("ex2")
    fun ex2(input: List<String>): Int {
        val result = directoryTotalSum(getAllFiles(input))
        val spaceTaken = result[Path("/")]!!
        val freeSpace = 70_000_000 - spaceTaken
        val requiredSpace = 30_000_000 - freeSpace

        return result
            .values
            .filter { it >= requiredSpace }
            .minOf { it }
    }

    private fun getAllFiles(input: List<String>): Map<Path, Int> {
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
