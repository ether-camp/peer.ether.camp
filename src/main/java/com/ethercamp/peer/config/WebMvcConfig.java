package com.ethercamp.peer.config;

import com.ethercamp.peer.rest.JsonRpcBak;
import com.ethercamp.peer.rest.JsonRpcServiceImpl;
import com.ethercamp.peer.sevice.console.crash.plugin.js.JSLanguage;
import com.googlecode.jsonrpc4j.spring.JsonServiceExporter;
import org.ethereum.jsonrpc.JsonRpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
    JsonRpcServiceImpl myService() {
        return new JsonRpcServiceImpl();
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
