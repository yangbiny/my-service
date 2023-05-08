package com.reason.know.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  @Bean
  public RequestMappingHandlerAdapter requestMapping() {
    RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();

    List<HttpMessageConverter<?>> converters = adapter.getMessageConverters();

    MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter();
    List<MediaType> supportedMediaTypes = new ArrayList<>();
    MediaType xmlMedia = new MediaType(MediaType.TEXT_XML, StandardCharsets.UTF_8);
    supportedMediaTypes.add(xmlMedia);
    xmlConverter.setSupportedMediaTypes(supportedMediaTypes);
    converters.add(xmlConverter);

    adapter.setMessageConverters(converters);

    return adapter;
  }

}