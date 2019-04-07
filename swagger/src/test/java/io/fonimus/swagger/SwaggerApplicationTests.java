package io.fonimus.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class SwaggerApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation)
                        .snippets().withTemplateFormat(TemplateFormats.markdown()))
                .build();
    }

    @Test
    void contextLoads() throws Exception {

        this.mvc.perform(get("/api/test/{path}", "test"))
                .andDo(document("getEx", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        this.mvc.perform(put("/api/test/{path}", "test")
                .content(new ObjectMapper().writeValueAsBytes(new SwaggerBean("test", 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("putEx", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        String spec = this.mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        try (InputStream is = new ByteArrayInputStream(spec.getBytes()); OutputStream os = new FileOutputStream("target/spec.json")) {
            IOUtils.copyLarge(is, os);
        }

        spec = new RestTemplate().getForObject("http://localhost:" + port + "/services/api/swagger.json", String.class);
        assertNotNull(spec);

        try (InputStream is = new ByteArrayInputStream(spec.getBytes()); OutputStream os = new FileOutputStream("target/spec-cxf.json")) {
            IOUtils.copyLarge(is, os);
        }
    }

}
