/**
 * Reads from stdin and writes to stdout
 */
fun main() {
    while (true) {
        val line = readLine() ?: break
        println(line)
    }
}