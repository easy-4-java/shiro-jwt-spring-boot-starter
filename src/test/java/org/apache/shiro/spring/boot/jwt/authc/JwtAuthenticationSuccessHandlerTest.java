package org.apache.shiro.spring.boot.jwt.authc;

import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthenticationToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link JwtAuthenticationSuccessHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthenticationSuccessHandler Tests")
class JwtAuthenticationSuccessHandlerTest {

    @Test
    @DisplayName("Default constructor creates instance")
    void testDefaultConstructor() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        assertThat(handler).isNotNull();
        assertThat(handler.getJwtPayloadRepository()).isNull();
        assertThat(handler.isCheckExpiry()).isFalse();
    }

    @Test
    @DisplayName("Parameterized constructor sets fields")
    void testParameterizedConstructor() {
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler(repo, true);
        assertThat(handler.getJwtPayloadRepository()).isEqualTo(repo);
        assertThat(handler.isCheckExpiry()).isTrue();
    }

    @Test
    @DisplayName("getOrder returns MAX_VALUE - 1")
    void testGetOrder() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    @DisplayName("supports returns true for JwtAuthenticationToken")
    void testSupportsJwtAuthenticationToken() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        assertThat(handler.supports(new JwtAuthenticationToken())).isTrue();
    }

    @Test
    @DisplayName("supports returns false for non-JwtAuthenticationToken")
    void testSupportsNonJwtToken() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        assertThat(handler.supports(new JwtAuthorizationToken("host", "token", false))).isFalse();
    }

    @Test
    @DisplayName("jwtPayloadRepository getter/setter works correctly")
    void testJwtPayloadRepositoryGetterSetter() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        handler.setJwtPayloadRepository(repo);
        assertThat(handler.getJwtPayloadRepository()).isEqualTo(repo);
    }

    @Test
    @DisplayName("checkExpiry getter/setter works correctly")
    void testCheckExpiryGetterSetter() {
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        handler.setCheckExpiry(true);
        assertThat(handler.isCheckExpiry()).isTrue();
        handler.setCheckExpiry(false);
        assertThat(handler.isCheckExpiry()).isFalse();
    }
}
