package uk.gov.justice.laa.crime.application.tracking.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServicesConfigurationTest {
    @Autowired
    ServicesConfiguration myClass;
    @Test
    void testVariables() {
        String expectedUrl = "http://test.com";
        String expectedRegID = "201920";
        // Call a public method that interacts with the private variable
        ServicesConfiguration.MaatApi maatApi = new ServicesConfiguration.MaatApi(expectedUrl, expectedRegID);
        myClass.setMaatApi(maatApi);

        // Assert the expected behavior
        assertEquals(expectedUrl, maatApi.getBaseUrl());
        assertEquals(expectedRegID, maatApi.getRegistrationId());
    }

}