# Kotlin multiplatform child process library

![Bintray](https://img.shields.io/bintray/v/xfelde/ksubprocess/ksubprocess)
![badge][badge-jvm]
![badge][badge-windows]
![badge][badge-linux]

Allows to launch child processes, monitor their state and capture their output. The interface is inspired by python's
`subprocess` module.

```kotlin
val result = exec {
    // some command line
    arg("aprogram")
    arg("-flag")
    arg("/path/to/file")
    // redirect streams
    stdin = Redirect.Pipe
    stdout = Redirect.Pipe
    stderr = Redirect.Write("/log/file")
    // add input
    input {
        append("Bla")
        append("Blub")
    }
    // check for errors
    check = true
}

// use result
println(result.output)
```

## Supported platforms

The ksubprocess library supports the following platforms:

- JVM via `java.lang.Process`
- Native/Linux via `fork`/`exec`
- Native/Windows via `CreateProcess`

Node.JS support is WIP, the environment interface works, but the process interface is hindered by the fact that it is 
synchronous, while the respective Node.JS api is asynchronous.
Mac support is possible, and can probably reuse a lot of the linux code. I don't own a mac to develop on, but 
contributions would be welcome.

## Stability
The library itself is well tested and has a relatively stable interface. Additional unit tests are welcome. However, in 
order to provide a platform-independent stream interface, the library depends on the 
[kotlinx-io](https://github.com/Kotlin/kotlinx-io) library, which is still in early development. Unfortunately, there
are no good alternatives short of implementing our own io library. Because of that, this library is is still incubating.


[badge-native]: http://img.shields.io/badge/platform-native-lightgrey.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-yellow.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/platform-jvm-orange.svg?style=flat
[badge-linux]: http://img.shields.io/badge/platform-linux-important.svg?style=flat 
[badge-windows]: http://img.shields.io/badge/platform-windows-informational.svg?style=flat
[badge-mac]: http://img.shields.io/badge/platform-macos-lightgrey.svg?style=flat
