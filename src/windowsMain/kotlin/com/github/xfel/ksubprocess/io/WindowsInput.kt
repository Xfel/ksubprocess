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
package com.github.xfel.ksubprocess.io

import com.github.xfel.ksubprocess.close
import kotlinx.io.core.AbstractInput
import kotlinx.io.core.ExperimentalIoApi
import kotlinx.io.core.Input
import kotlinx.io.core.IoBuffer
import platform.windows.HANDLE

@Suppress("FunctionName")
@ExperimentalIoApi
fun Input(handle: HANDLE?): Input = WindowsInputForFileHandle(handle)

@ExperimentalIoApi
private class WindowsInputForFileHandle(val handle: HANDLE?) : AbstractInput() {
    private var closed = false
    override fun closeSource() {
        if (closed) return
        closed = true
        handle.close()
    }

    override fun fill(): IoBuffer? {
        val buffer = pool.borrow()
        buffer.reserveEndGap(IoBuffer.ReservedSize)

        val size = read(handle, buffer)
        if (size == SZERO) { // EOF
            buffer.release(pool)
            return null
        }

        return buffer
    }

}