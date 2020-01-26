package com.easybank.test;

import com.easybank.Application;
import com.easybank.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@ActiveProfiles("mvc")
public class ApplicationTest {

    @Autowired
    private WebApplicationContext wac;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private JacksonJsonParser jsonParser = new JacksonJsonParser();

    private static final String CLIENT_ID = "easy-bank";
    private static final String CLIENT_SECRET = "easy-bank";
    private static final String MANAGER_USERNAME = "manager";
    private static final String MANAGER_PASSWORD = "manager";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
    }

    public String obtainAccessToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void getUnauthorized() throws Exception {
        mockMvc.perform(get("/bank"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAccess() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        mockMvc.perform(get("/bank")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void getBanks() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        mockMvc.perform(get("/bank")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void postBank() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        String bankString = mapper.writeValueAsString(new Bank("001", "Banco do Brasil S.A"));
        mockMvc.perform(post("/bank")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(bankString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void getAgencies() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        mockMvc.perform(get("/agency")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void postAgency() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        ResultActions bankResult = mockMvc.perform(get("/bank/1")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        String id = jsonParser.parseMap(resultString).get("id").toString();
        Bank bank = new Bank(Long.valueOf(id));

        String agencyString = mapper.writeValueAsString(new Agency(bank, "1234", "6"));
        mockMvc.perform(post("/agency")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(agencyString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void getClients() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        mockMvc.perform(get("/client")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void getAccounts() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        mockMvc.perform(get("/account")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void postAccount() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        String bankString = mapper.writeValueAsString(new Bank("341", "Banco Itaú S.A"));
        ResultActions bankResult = mockMvc.perform(post("/bank")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(bankString)
                .accept(CONTENT_TYPE));

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        String id = jsonParser.parseMap(resultString).get("id").toString();
        Bank bank = new Bank(Long.valueOf(id));

        String agencyString = mapper.writeValueAsString(new Agency(bank, "1234", "6"));
        ResultActions agencyResult = mockMvc.perform(post("/agency")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(agencyString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        resultString = agencyResult.andReturn().getResponse().getContentAsString();
        id = jsonParser.parseMap(resultString).get("id").toString();

        Agency agency = new Agency(Long.valueOf(id));

        User user = new User("John", "john", "123456");
        Client client = new Client(user, "John Smith", "654.951.655-78");
        client.setUser(user);

        String accountString = mapper.writeValueAsString(new Account(agency, client, "65421", "0", new BigDecimal(100)));
        mockMvc.perform(post("/account")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(accountString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

}
