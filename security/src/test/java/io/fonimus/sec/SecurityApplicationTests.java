package io.fonimus.sec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
abstract class SecurityApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void anonymousUser() throws Exception {
        test("anonymous", 401, 401, 401);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void user() throws Exception {
        test("user", 200, 403, 403);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void admin() throws Exception {
        test("admin", 200, 200, 403);
    }

    @Test
    @WithMockUser(username = "actuator", roles = {"ACTUATOR"})
    void actuator() throws Exception {
        test("actuator", 200, 403, 200);
    }

    @Test
    @WithMockUser(username = "all", roles = {"ADMIN", "ACTUATOR"})
    void all() throws Exception {
        test("all", 200, 200, 200);
    }

    protected void test(String userName, int authStatus, int adminStatus, int infoStatus) throws Exception {

        this.mockMvc.perform(get("/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));

        this.mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello " + userName));


        ResultActions tmp = this.mockMvc.perform(get("/auth"))
                .andDo(print())
                .andExpect(status().is(authStatus));

        if (authStatus == 200) {
            tmp.andExpect(content().string(containsString("\"authorities\"")));
        }


        tmp = this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().is(adminStatus));

        if (adminStatus == 200) {
            tmp.andExpect(content().string(containsString("\"authorities\"")));
        }


        tmp = this.mockMvc.perform(get("/actuator/info"))
                .andDo(print())
                .andExpect(status().is(infoStatus));


        if (infoStatus == 200) {
            tmp.andExpect(content().string("{}"));
        }
    }

}
