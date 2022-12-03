package runner

import java.io.File
import java.net.URL

// based on: https://www.infoworld.com/article/2077477/java-tip-113--identify-subclasses-at-runtime.html
fun getAllChallenges(clazz: Any, packageName: String = ""): List<String> {
    // Translate the package name into an absolute path
    var name = packageName
    if (!name.startsWith("/")) {
        name = "/$name"
    }
    name = name.replace('.', '/')

    // Get a File object for the package
    val url: URL = clazz::class.java.getResource(name) as URL
    val directory = File(url.file)

    val result = mutableListOf<String>()

    if (directory.exists()) {
        directory.walk()
            .filter { f -> f.isFile && !f.name.contains('$') && f.name.endsWith(".class") }
            .forEach {
                val fullyQualifiedClassName = packageName + it.canonicalPath.removePrefix(directory.canonicalPath)
                    .dropLast(6) // remove .class
                    .replace('/', '.')
                    .removePrefix(".") // handle root
                try {
                    val classForName = Class.forName(fullyQualifiedClassName)
                    if (classForName.annotations.any { ann -> ann is Challenge }) {
                        result.add(fullyQualifiedClassName)
                    }
                } catch (cnfex: ClassNotFoundException) {
                    System.err.println(cnfex)
                } catch (iex: InstantiationException) {
                    // We try to instantiate an interface
                    // or an object that does not have a
                    // default constructor
                } catch (iaex: IllegalAccessException) {
                    // The class is not public
                }
            }
    }

    return result
}
