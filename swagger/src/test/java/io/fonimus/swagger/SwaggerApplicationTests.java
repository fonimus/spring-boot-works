package io.fonimus.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.templates.TemplateFormat;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerApplicationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                        .snippets().withTemplateFormat(TemplateFormats.markdown()))
                .build();
    }

    @Test
    public void contextLoads() throws Exception {

        String spec = this.mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        try (InputStream is = new ByteArrayInputStream(spec.getBytes()); OutputStream os = new FileOutputStream("target/spec.json")) {
            IOUtils.copyLarge(is, os);
        }

        this.mvc.perform(get("/api/test/{path}", "test"))
                .andDo(document("getEx", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        this.mvc.perform(put("/api/test/{path}", "test")
                .content(new ObjectMapper().writeValueAsBytes(new SwaggerBean("test", 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("putEx", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());
    }

}
