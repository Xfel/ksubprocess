/**
 * For every argument, dumps the named environment variable. Missing vars will print null.
 */
fun main(args: Array<String>) {
    for (arg in args) {
        println(System.getenv(arg) ?: "<NOT-SET>")
    }
}