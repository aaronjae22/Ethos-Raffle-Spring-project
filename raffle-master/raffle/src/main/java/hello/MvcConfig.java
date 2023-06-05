package main.java.hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        
        registry.addViewController("/error").setViewName("error");
        
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/logged-out").setViewName("login");
    }
    

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
	registry.addResourceHandler("/resources/js/app/**")
    .addResourceLocations("/resources/js/app/") .setCachePeriod(0);
	
    registry.addResourceHandler("/resources/**")
            .addResourceLocations("/resources/") .setCachePeriod(900000)
            .resourceChain(false)
            .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
}

}
