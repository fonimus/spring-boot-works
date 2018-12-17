package io.fonimus.kotlin

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.templates.TemplateFormats
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration


@WebMvcTest(KotlinController::class)
@AutoConfigureRestDocs
class KotlinRestDocAutoConfigureTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun testApiConfig() {
        this.mvc.perform(get("/api/config").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andDo(document("config/auto"))
    }

    @TestConfiguration
    internal class CustomAdditionalConfiguration : RestDocsMockMvcConfigurationCustomizer {

        override fun customize(configurer: MockMvcRestDocumentationConfigurer) {
            configurer.snippets().withTemplateFormat(TemplateFormats.markdown())
                    .and().operationPreprocessors()
                    .withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint())
        }

    }
}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
class KotlinRestDocManualConfigureTest {

    private var mvc: MockMvc? = null

    @BeforeEach
    fun setup(context: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(
                        documentationConfiguration(restDocumentation)

                                .snippets().withTemplateFormat(TemplateFormats.markdown()).and()

                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint()))
                .build()
    }

    @Test
    fun testApiConfig() {
        this.mvc!!.perform(get("/api/config").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andDo(document("config/manual"))
    }
}