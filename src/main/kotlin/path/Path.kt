package path

data class Path(val path: String = "/") {

    fun append(p: String) = when {
        path.endsWith('/') -> Path("$path$p")
        else -> Path("$path/$p")
    }

    fun up(): Path {
        return path.foldRight(path) { char, acc ->
            if (char != '/') {
                return@foldRight acc.dropLast(1)
            } else {
                return Path(acc.dropLast(1))
            }
        }.let { Path(it) }
    }

    fun allPaths(): List<Path> {
        return this.path.split("/")
            .filter { it.isNotBlank() }
            .runningFold(Path("/")) { acc, v ->
                acc.append(v)
            }
    }
}
