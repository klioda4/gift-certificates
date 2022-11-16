package ru.clevertec.ecl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.cluster.interceptor.CommitLogFilteringInterceptor;
import ru.clevertec.ecl.cluster.interceptor.EntityInterceptor;
import ru.clevertec.ecl.cluster.interceptor.OrderInterceptor;

@RequiredArgsConstructor
@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    private static final String ORDERS_PATH_PATTERN = "/v1/orders/**";
    private static final String USERS_PATH_PATTERN = "/v1/users/**";
    private static final String TAGS_PATH_PATTERN = "/v1/tags/**";
    private static final String CERTIFICATES_PATH_PATTERN = "/v1/gift-certificates/**";
    private static final String SEQUENCE_PATH_PATTERN = "/v1/*/sequence/*";
    private static final String COMMIT_LOG_CREATION_PATH = "/commit-log";

    private final OrderInterceptor orderInterceptor;
    private final EntityInterceptor entityInterceptor;
    private final CommitLogFilteringInterceptor commitLogFilteringInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(orderInterceptor)
            .addPathPatterns(ORDERS_PATH_PATTERN)
            .excludePathPatterns(SEQUENCE_PATH_PATTERN);

        registry.addInterceptor(entityInterceptor)
            .addPathPatterns(TAGS_PATH_PATTERN,
                             CERTIFICATES_PATH_PATTERN,
                             USERS_PATH_PATTERN)
            .excludePathPatterns(SEQUENCE_PATH_PATTERN);

        registry.addInterceptor(commitLogFilteringInterceptor)
            .addPathPatterns(COMMIT_LOG_CREATION_PATH);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
            .addViewController("/swagger-ui/")
            .setViewName("forward:/swagger-ui/index.html");
    }
}
