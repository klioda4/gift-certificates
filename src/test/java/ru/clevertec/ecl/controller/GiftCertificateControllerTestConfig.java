package ru.clevertec.ecl.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.clevertec.ecl.config.AppWebMvcConfigurer;
import ru.clevertec.ecl.service.GiftCertificateService;

@Configuration
@EnableWebMvc
@Import({AppWebMvcConfigurer.class, GiftCertificateController.class})
public class GiftCertificateControllerTestConfig {

    @Bean
    public GiftCertificateService crudGiftCertificateService() {
        return Mockito.mock(GiftCertificateService.class);
    }
}
