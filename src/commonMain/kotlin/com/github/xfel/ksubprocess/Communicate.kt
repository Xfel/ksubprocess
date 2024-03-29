/*
 * Copyright 2019 Felix Treede
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xfel.ksubprocess

import kotlinx.io.charsets.Charset
import kotlinx.io.charsets.Charsets
import kotlinx.io.core.writeText
import kotlinx.io.errors.IOException
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


/**
 * Result tuple of Process.communicate().
 *
 * @param exitCode process exit code. Is 0 if the process terminated normally
 * @param output stdout pipe output, or empty if stdout wasn't a pipe
 * @param errors stderr pipe output, or empty if stderr wasn't a pipe
 */
data class CommunicateResult(
    val exitCode: Int,
    val output: String,
    val errors: String
) {

    /**
     * Check that the process exited normally, ie with [exitCode] 0. If not, throw [ProcessExitException].
     *
     * @throws ProcessExitException if `exitCode != 0`
     */
    fun check() {
        if (exitCode != 0) throw ProcessExitException(this)
    }
}

/**
 * Communicate with the process and wait for it's termination.
 *
 * If stdin is a pipe, input will be written to it. Afterwards, stdin will be closed to signal end-of-input.
 *
 * If stdout or stderr are pipes, their output will be collected and returned on completion. The pipes will be closed
 * on termination. The pipe collection runs in background threads to avoid buffer overflows in the pipe.
 *
 * If a timeout is set, the child will be [terminated][Process.terminate] if it doesn't finish soon enough. An extra
 * timeout for graceful termination can be set, afterwards the child will be [killed][Process.kill]. The kill timeout
 * can also be set to [Duration.ZERO] to skip the graceful termination attempt and kill the child directly.
 *
 * NOTE: The timeout functions require the experimental kotlin time API. However, this function can be perfectly used
 * without them if the timeout parameters are left alone (set to `null`). Therefore, it is not marked with
 * [ExperimentalTime].
 *
 * @param input stdin pipe input. Ignored if stdin isn't a pipe
 * @param charset charset to use for text communication. Defaults to UTF-8
 * @param timeout timeout for child process if desired
 * @param killTimeout extra timeout before the terminated child is killed. May be ZERO to kill directly
 *
 * @return result of communication
 *
 * @throws ProcessException if another process error occurs
 * @throws IOException if an IO error occurs in the pipes
 */
@UseExperimental(ExperimentalTime::class)
fun Process.communicate(
    input: String = "",
    charset: Charset = Charsets.UTF_8,
    timeout: Duration? = null,
    killTimeout: Duration? = null
): CommunicateResult {
    // start output pipe collectors
    val stdoutCollector =
        if (args.stdout == Redirect.Pipe) BackgroundPipeCollector(this, false, charset)
        else null
    val stderrCollector =
        if (args.stderr == Redirect.Pipe) BackgroundPipeCollector(this, true, charset)
        else null

    // push out the input data
    stdin?.let {
        it.writeText(input, charset = charset)
        // close input stream to notify child of input end
        it.close()
    }

    // wait with timeout if needed
    if (timeout != null && waitFor(timeout) == null) {
        // didn't exit in timeout, so terminate explicitly
        if (killTimeout == Duration.ZERO) {
            // kill directly
            kill()
        } else {
            // try gently first
            terminate()

            // wait a little more and kill if requested
            if (killTimeout != null && waitFor(killTimeout) == null) {
                kill()
            }
        }
    }

    // wait for the process to actually die
    val exitCode = waitFor()

    // wait for output collectors
    BackgroundPipeCollector.awaitAll(listOfNotNull(stdoutCollector, stderrCollector))

    // return result
    return CommunicateResult(
        exitCode,
        stdoutCollector?.result ?: "",
        stderrCollector?.result ?: ""
    )
}

/** Reads the given stream's text in the background, used by communicate. */
internal expect class BackgroundPipeCollector(
    process: Process,
    isStderr: Boolean,
    charset: Charset
) {
    // wait for the stream to reach EOF
    fun await()

    // get result. result of calling before EOF is unspecified
    val result: String

    companion object {
        // wait for all streams in list
        fun awaitAll(readers: List<BackgroundPipeCollector>)
    }
}

