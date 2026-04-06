package com.codefactory.appstripe;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AppstripeApplicationTests {

    @MockBean
    private IApiCredentialRepositoryPort credentialRepository;

    @MockBean
    private ICommerceRepositoryPort commerceRepository;

    @MockBean
    private IApiKeyGeneratorPort keyGenerator;

    @Test
    void contextLoads() {
    }

}
