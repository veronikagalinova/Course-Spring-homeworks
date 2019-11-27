package bg.sofia.uni.fmi.Blogger.Rest.API.config;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.UsersService;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.NonexisitngEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import bg.sofia.uni.fmi.Blogger.Rest.API.security.RestAuthenticationEntryPoint;
import bg.sofia.uni.fmi.Blogger.Rest.API.security.RestSavedRequestAwareAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;



@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestSavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/info").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger*/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                .antMatchers(HttpMethod.POST, "api/**").hasAnyRole("BLOGGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT).hasAnyRole("BLOGGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.DELETE).hasAnyRole("BLOGGER", "ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ADMINISTRATOR")
                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .permitAll()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/api/health");
    }

    @Bean
    public UserDetailsService userDetailsService(UsersService users) {
        return username -> {
            try {
                User found = users.getUserByEmail(username);
                log.debug(">>> User authenticated for email: {} is: {}", username, found);
                return found;
            } catch (NonexisitngEntityException ex) {
                throw new UsernameNotFoundException(ex.getMessage(), ex);
            }
        };
    }

}
