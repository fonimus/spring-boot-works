package io.fonimus.kotlin

import org.springframework.stereotype.Service

@Service
class KotlinService {

    fun testMethod(): String {
        return "real-implementation"
    }

    fun testMethodWithArg(arg: String): String {
        return arg
    }
}
