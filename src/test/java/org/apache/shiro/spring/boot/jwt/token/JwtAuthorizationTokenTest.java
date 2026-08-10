package org.apache.shiro.spring.boot.jwt.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtAuthorizationToken}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthorizationToken Tests")
class JwtAuthorizationTokenTest {

    @Test
    @DisplayName("Constructor sets all fields correctly")
    void testConstructor() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("192.168.1.1", "jwt-value", true);
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
        assertThat(token.getToken()).isEqualTo("jwt-value");
        assertThat(token.isRememberMe()).isTrue();
        assertThat(token.getPrincipal()).isEqualTo("jwt-value");
        assertThat(token.getCredentials()).isEqualTo("jwt-value");
    }

    @Test
    @DisplayName("Constructor with rememberMe false")
    void testConstructorNotRememberMe() {
        JwtAuthorizationToken token = new JwtAuthorizationToken("host", "token", false);
        assertThat(token.isRememberMe()).isFalse();
    }
}
