package com.amahoro.amahoro_stadium_ticketing.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/access-denied").setViewName("access-denied");
        registry.addViewController("/").setViewName("homepage");
        registry.addViewController("/about-us").setViewName("about-us");

    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all endpoints
//                .allowedOrigins("http://localhost:5173") // Allow all origins
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Set to true if credentials (cookies, authorization headers) are needed
//    }

}