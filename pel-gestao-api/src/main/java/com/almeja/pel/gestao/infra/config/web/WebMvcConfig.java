package com.almeja.pel.gestao.infra.config.web;

import com.almeja.pel.gestao.core.util.enums.EnumDateFormat;
import com.almeja.pel.gestao.infra.interceptor.AuthInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(EnumDateFormat.YYYYMMDDTHHMMSS.getFormat());
        mapper.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        final MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(mapper);
        converters.clear();
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(jsonConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/v3/api-docs/**",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-ui/**"
                );
    }

}