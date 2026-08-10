package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.JwtPrincipalRepository;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//https://blog.csdn.net/weixin_42058600/article/details/81837056
@Configuration
/** Auto-configuration for Shiro Jwt Web.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = ShiroJwtProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroJwtProperties.class })
public class ShiroJwtWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	protected JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler(
			JwtPayloadRepository jwtPayloadRepository,
			ShiroJwtProperties jwtProperties) {
		return new JwtAuthenticationSuccessHandler(jwtPayloadRepository, jwtProperties.isCheckExpiry());
	}

	@Bean
	@ConditionalOnMissingBean
	protected JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler() {
		return new JwtAuthenticationFailureHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtPrincipalRepository jwtRepository(
			JwtPayloadRepository jwtPayloadRepository,
			ShiroJwtProperties properties) {
		JwtPrincipalRepository jwtRepository = new JwtPrincipalRepository(jwtPayloadRepository);
		jwtRepository.setCheckExpiry(properties.isCheckExpiry());
		return jwtRepository;
	}

}
