package com.easybank.test;

import com.easybank.Application;
import com.easybank.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        ResultActions bankResult = mockMvc.perform(get("/bank")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        List<Bank> banks = mapper.readValue(resultString, new TypeReference<List<Bank>>() {
        });

        Bank bank = new Bank();

        if (!banks.isEmpty()) {
            bank = banks.get(0);
        } else {
            String bankString = mapper.writeValueAsString(new Bank("001", "Banco do Brasil S.A"));
            bankResult = mockMvc.perform(post("/bank")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(bankString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isOk());
            bankString = bankResult.andReturn().getResponse().getContentAsString();
            bank.setId(Long.parseLong(jsonParser.parseMap(bankString).get("id").toString()));
        }

        String agencyString = mapper.writeValueAsString(new Agency(bank, "12345", "6"));
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

        user = new User("Martha", "martha", "123456");
        client = new Client(user, "Martha Smith", "951.654.678-55");
        client.setUser(user);

        accountString = mapper.writeValueAsString(new Account(agency, client, "65422", "1", new BigDecimal(50)));
        mockMvc.perform(post("/account")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(accountString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void deposit() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        ResultActions bankResult = mockMvc.perform(get("/account")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        List<Account> accounts = mapper.readValue(resultString, new TypeReference<List<Account>>() {
        });

        Account account;

        if (!accounts.isEmpty()) {
            account = accounts.get(0);
            Deposit deposit = new Deposit(account, new BigDecimal(50));

            String depositString = mapper.writeValueAsString(deposit);
            mockMvc.perform(put("/account/deposit")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(depositString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void withdraw() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        ResultActions bankResult = mockMvc.perform(get("/account")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        List<Account> accounts = mapper.readValue(resultString, new TypeReference<List<Account>>() {
        });

        Account account;

        if (!accounts.isEmpty()) {
            account = accounts.get(0);

            BigDecimal amount = account.getBalance().add(new BigDecimal(1));

            Withdraw withdraw = new Withdraw(account, amount);

            String withdrawString = mapper.writeValueAsString(withdraw);
            mockMvc.perform(put("/account/withdraw")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(withdrawString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isServiceUnavailable());

            withdraw = new Withdraw(account, account.getBalance());
            withdrawString = mapper.writeValueAsString(withdraw);
            mockMvc.perform(put("/account/withdraw")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(withdrawString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void extract() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        ResultActions bankResult = mockMvc.perform(get("/account")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        List<Account> accounts = mapper.readValue(resultString, new TypeReference<List<Account>>() {
        });

        Account account;

        if (!accounts.isEmpty()) {
            account = accounts.get(0);
            mockMvc.perform(get("/account/extract?id=" + account.getId())
                    .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void transfer() throws Exception {
        final String accessToken = obtainAccessToken(MANAGER_USERNAME, MANAGER_PASSWORD);
        ResultActions bankResult = mockMvc.perform(get("/account")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        String resultString = bankResult.andReturn().getResponse().getContentAsString();

        List<Account> accounts = mapper.readValue(resultString, new TypeReference<List<Account>>() {
        });

        Account origin;
        Account destination;

        if (!accounts.isEmpty() && accounts.size() > 1) {
            origin = accounts.get(0);
            destination = accounts.get(1);

            BigDecimal amount = origin.getBalance().add(new BigDecimal(1));

            Transfer transfer = new Transfer(origin, destination, amount);

            String transferString = mapper.writeValueAsString(transfer);
            mockMvc.perform(put("/account/transfer")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(transferString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isServiceUnavailable());

            transfer = new Transfer(origin, destination, origin.getBalance());
            transferString = mapper.writeValueAsString(transfer);
            mockMvc.perform(put("/account/transfer")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(CONTENT_TYPE)
                    .content(transferString)
                    .accept(CONTENT_TYPE))
                    .andExpect(status().isOk());
        }
    }

}
