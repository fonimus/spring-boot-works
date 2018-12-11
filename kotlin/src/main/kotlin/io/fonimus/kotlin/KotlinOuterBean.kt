package io.fonimus.kotlin

import javax.validation.constraints.Max
import javax.validation.constraints.Min

class KotlinOuterBean {

    @Min(10)
    @Max(1000)
    var nested = 101
}