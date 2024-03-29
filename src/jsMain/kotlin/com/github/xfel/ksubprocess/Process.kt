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

import kotlinx.io.core.Input
import kotlinx.io.core.Output
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

actual class Process actual constructor(actual val args: ProcessArguments) {

    init {

    }

    actual val isAlive: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val exitCode: Int?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    actual fun waitFor(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @ExperimentalTime
    actual fun waitFor(timeout: Duration): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual val stdin: Output?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val stdout: Input?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val stderr: Input?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    actual fun terminate() {
    }

    actual fun kill() {
    }


}