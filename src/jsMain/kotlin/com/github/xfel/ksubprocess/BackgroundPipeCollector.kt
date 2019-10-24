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

/** Reads the given stream's text in the background, used by communicate. */
internal actual class BackgroundPipeCollector actual constructor(
    process: Process,
    isStderr: Boolean,
    charset: Charset
) {
    actual fun await() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual val result: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    actual companion object {
        actual fun awaitAll(readers: List<BackgroundPipeCollector>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}