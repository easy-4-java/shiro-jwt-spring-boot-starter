package org.apache.shiro.spring.boot.jwt.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtAuthenticationToken}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthenticationToken Tests")
class JwtAuthenticationTokenTest {

    @Test
    @DisplayName("Default constructor creates instance")
    void testDefaultConstructor() {
        JwtAuthenticationToken token = new JwtAuthenticationToken();
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Constructor with username, password, rememberMe")
    void testConstructorBasic() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", true);
        assertThat(token.getPrincipal()).isEqualTo("user");
        assertThat(token.isRememberMe()).isTrue();
    }

    @Test
    @DisplayName("Constructor with username, password, rememberMe, host")
    void testConstructorWithHost() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", false, "192.168.1.1");
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
        assertThat(token.isRememberMe()).isFalse();
    }

    @Test
    @DisplayName("Constructor with captcha")
    void testConstructorWithCaptcha() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", "captcha", "host");
        assertThat(token.getCaptcha()).isEqualTo("captcha");
    }

    @Test
    @DisplayName("Constructor with captcha and rememberMe")
    void testConstructorWithCaptchaRememberMe() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", "pass", "captcha", true, "host");
        assertThat(token.getCaptcha()).isEqualTo("captcha");
        assertThat(token.isRememberMe()).isTrue();
    }

    @Test
    @DisplayName("Constructor with char[] password and rememberMe")
    void testConstructorCharArrayBasic() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", new char[]{'p', 'a', 's', 's'}, true);
        assertThat(token.getPrincipal()).isEqualTo("user");
        assertThat(token.isRememberMe()).isTrue();
    }

    @Test
    @DisplayName("Constructor with char[] password, rememberMe, host")
    void testConstructorCharArrayWithHost() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", new char[]{'p'}, false, "host");
        assertThat(token.getHost()).isEqualTo("host");
    }

    @Test
    @DisplayName("Constructor with char[] password and captcha")
    void testConstructorCharArrayWithCaptcha() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", new char[]{'p'}, "captcha", "host");
        assertThat(token.getCaptcha()).isEqualTo("captcha");
    }

    @Test
    @DisplayName("Constructor with char[] password, captcha, rememberMe")
    void testConstructorCharArrayWithCaptchaRememberMe() {
        JwtAuthenticationToken token = new JwtAuthenticationToken("user", new char[]{'p'}, "captcha", true, "host");
        assertThat(token.getCaptcha()).isEqualTo("captcha");
        assertThat(token.isRememberMe()).isTrue();
    }
}
