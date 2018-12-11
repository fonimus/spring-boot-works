package io.fonimus.kotlin

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import java.nio.charset.Charset
import javax.annotation.PostConstruct

@RestController
class KotlinController(val properties: KotlinProperties) {

    @PostConstruct
    fun init() {
        val test = properties.list.withIndex().associateBy { it.index to it.value.fromInner }
        for (entry in test.entries) {
            println("${entry.key} -> ${entry.value}")
        }
        val test2 = properties.map.map {
            it.key to it.value.fromInner
        }.toMap()
        for (entry in test2.entries) {
            println("${entry.key} -> ${entry.value}")
        }
        println("from java: ${JavaClass.test()}")
    }

    @GetMapping("/api/config")
    fun config(): KotlinProperties {
        return properties
    }

    @GetMapping("/api/resources")
    fun resources(): ResponseEntity<Resource> {
        var toReturn = "\n"
        for ((i, resource) in properties.resources.withIndex()) {
            toReturn += "- $i -----------------------------\n\n"
            toReturn += resource.inputStream.readTextAndClose()
        }
        return ResponseEntity.ok(ByteArrayResource(toReturn.toByteArray()))
    }

}

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}