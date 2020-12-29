package com.turing.b2c.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("shoplogin");
        registry.addViewController("/admin/index.html").setViewName("admin/index");
        registry.addViewController("/admin/home.html").setViewName("admin/home");
        registry.addViewController("/admin/goodsEdit.html").setViewName("admin/goodsEdit");
        registry.addViewController("/admin/goods.html").setViewName("admin/goods");
    }
}
