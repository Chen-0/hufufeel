package me.rubick.transport.app.config;

import me.rubick.transport.app.interceptor.AccessInterceptor;
import me.rubick.transport.app.interceptor.InjectInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Resource
    private InjectInterceptor injectInterceptor;

    @Resource
    private AccessInterceptor accessInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("auth/login");
        registry.addViewController("/register").setViewName("auth/register");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);
        return converter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(injectInterceptor);
        registry.addInterceptor(accessInterceptor);
    }
}
