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

private data class EnvEntry(override val key: String, override val value: String) : Map.Entry<String, String>

actual object Environment : AbstractMap<String, String>(), Map<String, String> {
    override val entries: Set<Map.Entry<String, String>> by lazy {
        object : AbstractSet<Map.Entry<String, String>>() {
            override val size: Int
                get() = sumBy { 1 }

            override fun iterator(): Iterator<Map.Entry<String, String>> {
                return js("Object.keys(process.env)").iterator().asSequence().map { key: String ->
                    EnvEntry(key, get(key)!!)
                }.iterator()
            }
        }
    }


    override fun get(key: String): String? {
        return js("process.env[key]") as? String
    }

    override fun containsKey(key: String): Boolean {
        return js("process.env[key] != null") as Boolean
    }

    actual val caseInsensitive: Boolean
        get() = false // TODO
}