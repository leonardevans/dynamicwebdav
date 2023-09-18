package com.dynamicwebdav.dynamicwebdav;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.seanox.webdav.WebDavFilter;

import lombok.Data;

@ComponentScan({"com.dynamicwebdav.dynamicwebdav.api"})
@SpringBootApplication
public class DynamicwebdavApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		final SpringApplication springApplication = new SpringApplication(DynamicwebdavApplication.class);
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.run(args);
	}

	@Bean
    public FilterRegistrationBean<WebDavFilter> webDavFilterRegistration() {
        final FilterRegistrationBean<WebDavFilter> registration = new FilterRegistrationBean<WebDavFilter>();
        registration.setFilter(new WebDavFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix="example")
    @EnableConfigurationProperties
    public static class ApplicationConfiguration {
        // Example of the configuration as an inner class in combination with lombok
    }
}
