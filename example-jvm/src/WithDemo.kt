import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/*
 *  Copyright 2017 JetBrains s.r.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@Serializable
data class Data(
        val id: Int,
        @Serializable(with = IX::class) val payload: Payload,
        val date: Date
)

data class Payload(val content: String)

@Serializer(forClass = Payload::class)
object IX {}

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS")

    override fun save(output: KOutput, obj: Date) {
        output.writeStringValue(df.format(obj))
    }

    override fun load(input: KInput): Date {
        return df.parse(input.readStringValue())
    }
}

fun main(args: Array<String>) {
    val o = Data(1, Payload("lorem ipsum dolor sit amet"), Date())
    val json = JSON(indented = true,
            context = SerialContext().apply { registerSerializer(Date::class, DateSerializer) }
    )
    println(json.stringify(o))
}