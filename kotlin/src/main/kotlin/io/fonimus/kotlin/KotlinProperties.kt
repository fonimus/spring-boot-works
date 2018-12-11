package io.fonimus.kotlin

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.core.io.Resource
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * Kotlin properties
 */
@ConfigurationProperties(prefix = "kotlin")
@Validated
class KotlinProperties {

    /**
     * From outer
     */
    var fromOuter: String? = null

    @Pattern(regexp = "[abc]*")
    var withDefault = "acbcbca"

    var inner = KotlinInnerBean()

    @Min(200)
    var integ = 300

    @NotNull
    var notNull: String? = null

    @NestedConfigurationProperty
    var outer = KotlinOuterBean()

    var list = mutableListOf<KotlinInnerBean>()

    var map = mutableMapOf<String, KotlinInnerBean>()

    @NotEmpty
    var notEmpty = mutableListOf<String>()

    @JsonIgnore
    var resources = mutableListOf<Resource>()

    class KotlinInnerBean {
        var fromInner: String? = null
    }
}