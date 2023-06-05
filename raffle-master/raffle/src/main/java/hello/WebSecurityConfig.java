package main.java.hello;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(securedEnabled = false, prePostEnabled=false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	
        http
        .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/home","/resources/**","/error").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll()
            .logoutSuccessUrl("/logged-out").permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        
    	/*
    	 auth
    	 
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
        */
        auth.jdbcAuthentication().passwordEncoder(passwordEncoder).dataSource(dataSource)
		.usersByUsernameQuery("select userName,password, active as enabled from user where userName=?")
		.authoritiesByUsernameQuery(
				"select userName as username, if(administrator = 1, 'Administrator','Regular')  role   from user u where u.username=?");
        
        
    }
}
