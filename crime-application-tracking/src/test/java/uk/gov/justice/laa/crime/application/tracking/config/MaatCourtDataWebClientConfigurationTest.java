package uk.gov.justice.laa.crime.application.tracking.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.ConnectionProvider;

import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MaatCourtDataWebClientConfigurationTest {
    @Autowired
    private ServicesConfiguration servicesConfiguration;
    @Autowired
    private ClientRegistrationRepository clientRegistrations;
    @Autowired
    private OAuth2AuthorizedClientRepository authorizedClients;
    @Autowired
    private WebClient webClient;

    private MaatCourtDataWebClientConfiguration webClientConfig;

    @BeforeEach
    void setUp() {
        webClientConfig = new MaatCourtDataWebClientConfiguration();
    }

    @Test
    void shouldConfigureWebClient(){
        WebClient webClient = webClientConfig.maatCourtDataWebClient(servicesConfiguration, clientRegistrations, authorizedClients);
        assertNotNull(webClient);
    }

    @Test
    void shouldConfigureMaatCourtDataApiClient(){
        MaatCourtDataApiClient maatCourtDataApiClient = webClientConfig.maatCourtDataApiClient(webClient);
        assertNotNull(maatCourtDataApiClient);
    }
}