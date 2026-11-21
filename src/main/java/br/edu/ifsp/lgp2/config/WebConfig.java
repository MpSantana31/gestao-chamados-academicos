package br.edu.ifsp.lgp2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Configura a aplicação web e exclui os recursos públicos da verificação de login. */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SessaoInterceptor interceptor;
    public WebConfig(SessaoInterceptor interceptor){this.interceptor=interceptor;}
    @Override public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns("/login","/logout","/css/**","/js/**","/images/**","/error","/favicon.ico");
    }
}
