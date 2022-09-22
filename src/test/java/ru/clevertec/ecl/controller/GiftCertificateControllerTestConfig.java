package ru.clevertec.ecl.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.clevertec.ecl.config.AppWebMvcConfigurer;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.service.CrudService;

@Configuration
@EnableWebMvc
@Import({AppWebMvcConfigurer.class, GiftCertificateController.class})
public class GiftCertificateControllerTestConfig {

    @Bean
    public CrudService<GiftCertificateDto, GiftCertificateCreateDto, Long> crudGiftCertificateService() {
        return Mockito.mock(CrudService.class);
    }
}
