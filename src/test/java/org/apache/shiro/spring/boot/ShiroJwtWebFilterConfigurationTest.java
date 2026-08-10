package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticatingFilter;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationSuccessHandler;
import org.apache.shiro.spring.boot.jwt.authz.JwtAuthorizationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ShiroJwtWebFilterConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroJwtWebFilterConfiguration Tests")
class ShiroJwtWebFilterConfigurationTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        ShiroJwtWebFilterConfiguration instance = new ShiroJwtWebFilterConfiguration();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("jwtAuthcFilterRegistrationBean creates filter registration")
    void testJwtAuthcFilterRegistrationBean() {
        ShiroJwtWebFilterConfiguration config = new ShiroJwtWebFilterConfiguration();
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        JwtAuthenticationSuccessHandler successHandler = new JwtAuthenticationSuccessHandler(repo, false);
        JwtAuthenticationFailureHandler failureHandler = new JwtAuthenticationFailureHandler();
        ShiroJwtProperties properties = new ShiroJwtProperties();
        properties.setCheckExpiry(true);

        var registration = config.jwtAuthcFilterRegistrationBean(repo, successHandler, failureHandler, properties);
        assertThat(registration).isNotNull();
        assertThat(registration.getFilter()).isInstanceOf(JwtAuthenticatingFilter.class);
    }

    @Test
    @DisplayName("jwtAuthzFilterRegistrationBean creates filter registration")
    void testJwtAuthzFilterRegistrationBean() {
        ShiroJwtWebFilterConfiguration config = new ShiroJwtWebFilterConfiguration();
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        ShiroJwtProperties properties = new ShiroJwtProperties();

        var registration = config.jwtAuthzFilterRegistrationBean(repo, properties);
        assertThat(registration).isNotNull();
        assertThat(registration.getFilter()).isInstanceOf(JwtAuthorizationFilter.class);
    }
}
