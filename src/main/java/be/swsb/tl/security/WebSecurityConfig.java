package be.swsb.tl.security;

import be.swsb.tl.security.jwt.JwtCsrfValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CsrfTokenRepository jwtCsrfTokenRepository;
    @Autowired
    private JwtCsrfValidatorFilter jwtCsrfValidatorFilter;

    public static final String[] ignoreCsrfAntMatchers = {
            "/login"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(jwtCsrfValidatorFilter, CsrfFilter.class)
                .csrf()
                .csrfTokenRepository(jwtCsrfTokenRepository)
                .ignoringAntMatchers(ignoreCsrfAntMatchers)
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER").and().formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN", "USER");
    }
}
