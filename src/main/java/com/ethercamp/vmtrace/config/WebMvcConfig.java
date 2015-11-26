package com.ethercamp.vmtrace.config;

import com.ethercamp.vmtrace.rest.JsonRpc;
import com.ethercamp.vmtrace.rest.JsonRpcImpl;
import com.ethercamp.vmtrace.sevice.console.crash.plugin.js.JSLanguage;
import com.googlecode.jsonrpc4j.spring.JsonServiceExporter;
import org.crsh.spring.SpringBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//    @Autowired
//    private List<HandlerInterceptor> interceptors;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        interceptors.stream().forEach(registry::addInterceptor);
//        super.addInterceptors(registry);
//    }

    @Bean
    JsonRpcImpl myService() {
        return new JsonRpcImpl();
    }

    @Bean(name = "/")
    public JsonServiceExporter jr() {
        JsonServiceExporter ret = new JsonServiceExporter();
        ret.setService(myService());
        ret.setServiceInterface(JsonRpc.class);
        return ret;
    }

    @Bean
    public JSLanguage crashJSPlugin() {
        return new JSLanguage();
    }

//    @Bean
//    public SpringBootstrap crashBean() {
//        SpringBootstrap crashBoot = new SpringBootstrap();
//        crashBoot.setConfig();
//    }
}
