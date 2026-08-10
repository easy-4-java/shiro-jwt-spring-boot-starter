package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.JwtPrincipalRepository;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticatingFilter;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationSuccessHandler;
import org.apache.shiro.spring.boot.jwt.authz.JwtAuthorizationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
/** Configuration for Shiro Jwt Web authentication filter chain.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = ShiroJwtProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroJwtProperties.class })
public class ShiroJwtWebFilterConfiguration {

	@Bean("jwtAuthcFilter")
	@ConditionalOnMissingBean(name = "jwtAuthcFilter")
	public FilterRegistrationBean<JwtAuthenticatingFilter> jwtAuthcFilterRegistrationBean(
			JwtPayloadRepository jwtPayloadRepository,
			JwtAuthenticationSuccessHandler successHandler,
			JwtAuthenticationFailureHandler failureHandler,
			ShiroJwtProperties jwtProperties) {

		JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
		filter.setJwtPayloadRepository(jwtPayloadRepository);
		filter.setSuccessHandler(successHandler);
		filter.setFailureHandler(failureHandler);
		filter.setCheckExpiry(jwtProperties.isCheckExpiry());

		FilterRegistrationBean<JwtAuthenticatingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.setOrder(Integer.MAX_VALUE - 1);
		return registration;
	}

	@Bean("jwtAuthzFilter")
	@ConditionalOnMissingBean(name = "jwtAuthzFilter")
	public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthzFilterRegistrationBean(
			JwtPayloadRepository jwtPayloadRepository,
			ShiroJwtProperties jwtProperties) {

		JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
		filter.setJwtPayloadRepository(jwtPayloadRepository);
		filter.setCheckExpiry(jwtProperties.isCheckExpiry());

		FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.setOrder(Integer.MAX_VALUE);
		return registration;
	}

}
