package org.apache.shiro.spring.boot.jwt.authc.credential;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtCredentialsMatcher}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtCredentialsMatcher Tests")
class JwtCredentialsMatcherTest {

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtCredentialsMatcher matcher = new JwtCredentialsMatcher();
        assertThat(matcher).isNotNull();
    }

    @Test
    @DisplayName("doCredentialsMatch always returns true")
    void testDoCredentialsMatch() {
        JwtCredentialsMatcher matcher = new JwtCredentialsMatcher();
        AuthenticationToken token = new UsernamePasswordToken("user", "pass");
        AuthenticationInfo info = new SimpleAuthenticationInfo("user", "jwt-token", "realm");
        assertThat(matcher.doCredentialsMatch(token, info)).isTrue();
    }
}
