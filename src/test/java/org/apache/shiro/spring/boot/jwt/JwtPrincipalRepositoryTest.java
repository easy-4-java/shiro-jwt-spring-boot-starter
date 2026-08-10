package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.easy4j.jwt.JwtPayload;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JwtPrincipalRepository}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtPrincipalRepository Tests")
class JwtPrincipalRepositoryTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository instance = new JwtPrincipalRepository(repo);
        assertThat(instance).isNotNull();
        assertThat(instance.getJwtPayloadRepository()).isSameAs(repo);
    }

    @Test
    @DisplayName("checkExpiry getter/setter works correctly")
    void testCheckExpiryGetterSetter() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtPrincipalRepository instance = new JwtPrincipalRepository(repo);
        assertThat(instance.isCheckExpiry()).isFalse();
        instance.setCheckExpiry(true);
        assertThat(instance.isCheckExpiry()).isTrue();
    }

    @Test
    @DisplayName("getAuthenticationInfo returns AuthenticationInfo with JwtPayload")
    void testGetAuthenticationInfo() {
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        JwtPayload payload = new JwtPayload();
        payload.setSubject("user123");
        payload.setRkey("admin");
        payload.setUkey("key");
        payload.setUcode("code");
        payload.setRid("role1");
        when(repo.getPayload(any(JwtAuthorizationToken.class), anyBoolean())).thenReturn(payload);

        JwtPrincipalRepository instance = new JwtPrincipalRepository(repo);
        JwtAuthorizationToken token = new JwtAuthorizationToken("host", "jwt-token", false);
        AuthenticationInfo info = instance.getAuthenticationInfo(token);
        assertThat(info).isNotNull();
        assertThat(info.getCredentials()).isEqualTo("jwt-token");
    }
}
