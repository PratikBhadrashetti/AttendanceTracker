/*package com.attendance.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import org.apache.velocity.app.VelocityEngine;
//import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter {

// @Autowired
//    private NFVAuthenticationEntryPoint authenticationEntryPoint;
//    
// @Autowired
//    private AuthSuccessHandler authSuccessHandler;
//    
// @Autowired
//    private AuthFailureHandler authFailureHandler;
//    
// @Autowired
//    private NFVLogoutSuccessHandler logoutSuccessHandler;
//    
// @Autowired
//    private NFVAuthenticationProvider authenticationProvider;
// 
// @Bean
// ProviderManager getProviderManager() {
//  List<AuthenticationProvider> list = new ArrayList<>();
//  list.add(authenticationProvider);
//  ProviderManager pm = new ProviderManager(list);
//  pm.setEraseCredentialsAfterAuthentication(false);
//  return pm;
// }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider);
//    }
// 
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//     return getProviderManager();
//    }
// 
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authenticationProvider(authenticationProvider)
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .formLogin()
//                .permitAll()
//                .loginProcessingUrl("/login")
//                .usernameParameter("user")
//                .passwordParameter("password")
//                .successHandler(authSuccessHandler)
//                .failureHandler(authFailureHandler)
//                .and()
//                .logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/login", "DELETE"))
//                .logoutSuccessHandler(logoutSuccessHandler);
////                .and()
////                .sessionManagement()
////                .maximumSessions(1);
// 
//        http.authorizeRequests().
////         antMatchers("/catalog").hasAnyAuthority("EndUser","Admin").
//         anyRequest().
//         authenticated();
//    }
//    
// @Override
// public void configure(WebSecurity web) throws Exception {
//  web.ignoring().antMatchers("/index.html");
//  web.ignoring().antMatchers("/register");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/css/bootstrap.min.css");
//  web.ignoring().antMatchers("/assets/plugins/jquery/dist/jquery.min.js");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/js/bootstrap.min.js");
//  web.ignoring().antMatchers("/styles.*.bundle.css");
//  web.ignoring().antMatchers("/inline.*.bundle.js");
//  web.ignoring().antMatchers("/vendor.*.bundle.js");
//  web.ignoring().antMatchers("/main.*.bundle.js");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/js/bootstrap.min.js");
//  web.ignoring().antMatchers("/favicon.ico");
//  web.ignoring().antMatchers("/assets/images/logo.png");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/fonts/glyphicons-halflings-regular.woff2");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/fonts/glyphicons-halflings-regular.woff");
//  web.ignoring().antMatchers("/assets/plugins/bootstrap/dist/fonts/glyphicons-halflings-regular.ttf");
//  web.ignoring().antMatchers("/assets/images/loading.gif");
//  web.ignoring().antMatchers("/registerenduser");
//  web.ignoring().antMatchers("/registertenantadmin");
//  web.ignoring().antMatchers("/setnewpassword/*");
//  web.ignoring().antMatchers("/setforgotpassword/*");
//  web.ignoring().antMatchers("/setpasswordforadduser/*");
//  web.ignoring().antMatchers("/redirecttosetpassword/*");
//  web.ignoring().antMatchers("/redirecttoregister/*");
//  web.ignoring().antMatchers("/redirecttoforgotpassword/*");
//  web.ignoring().antMatchers("/redirecttoaddusersetpassword/*");
//  web.ignoring().antMatchers("/getalltenants");
//  web.ignoring().antMatchers("/tenantmanagement");
//  web.ignoring().antMatchers("/forgotpassword");
//  web.ignoring().antMatchers("/assets/dash/metisMenu.min.js");
//  web.ignoring().antMatchers("/assets/dash/raphael.min.js");
//  web.ignoring().antMatchers("/assets/dash/morris-data.js");
//  web.ignoring().antMatchers("/assets/dash/sb-admin-2.js");
//  web.ignoring().antMatchers("/assets/dash/metisMenu.min.css");
//  web.ignoring().antMatchers("/assets/dash/sb-admin-2.css");
//  web.ignoring().antMatchers("/assets/dash/font-awesome/css/font-awesome.min.css");
//  
// }
// 
// @Bean
// public VelocityEngine velocityEngine() throws VelocityException,
//   IOException {
//  VelocityEngine factory = new VelocityEngine();
//  Properties props = new Properties();
//  props.put("resource.loader", "class");
//  props.put("class.resource.loader.class",
//    "org.apache.velocity.runtime.resource.loader."
//      + "ClasspathResourceLoader");
//  factory.addProperty("resource.loader", "class");
//  factory.addProperty("class.resource.loader.class",
//    "org.apache.velocity.runtime.resource.loader."
//      + "ClasspathResourceLoader");
//  return factory;
// }
// 
// // Set maxPostSize of embedded tomcat server to 10 megabytes (default is 2 MB, not large enough to support file uploads > 1.5 MB)
// /*@Bean
// EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
//     return (ConfigurableEmbeddedServletContainer container) -> {
//         if (container instanceof TomcatEmbeddedServletContainerFactory) {
//             TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
//             tomcat.addConnectorCustomizers(
//                 (connector) -> {
//                     connector.setMaxPostSize(999999999); // 999 MB
//                 }
//             );
//         }
//     };
// }

} */