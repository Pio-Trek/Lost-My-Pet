package graded.unit.lostmypetwebapp.config;

import graded.unit.lostmypetwebapp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This class configure Authentication and Authorization for the Spring MVC application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    /**
     * Authentication of each URL using user role.
     *
     * @param http This configure web based security for specific http requests
     * @throws Exception When request access is not valid.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/").permitAll()
                .antMatchers("/account").hasRole("USER")
                .antMatchers("/account/messages/**").hasRole("USER")
                .antMatchers("/account/update").authenticated()
                .antMatchers("/account/login").anonymous()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/view/post").authenticated()
                .antMatchers("/post/**").hasRole("USER")
                .antMatchers("/view/lost/delete").hasRole("ADMIN")
                .antMatchers("/view/found/delete").hasRole("ADMIN")
                // log in
                .and()
                .formLogin()
                .loginPage("/account/login")
                .loginProcessingUrl("/account/login")
                .failureUrl("/account/login?error")
                .permitAll()
                .and()
                // logout
                .logout()
                .logoutUrl("/account/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/account/access-denied");

    }

    /**
     * This method pass an argument with {@link AuthenticationManagerBuilder} to
     * create a user with password and role.
     *
     * @param auth This is authentication provider.
     * @throws Exception When authentication cannot be build and/or is not valid.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}