package org.apache.shiro.spring.boot.jwt.realm;

import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtStatefulAuthorizingRealm}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtStatefulAuthorizingRealm Tests")
class JwtStatefulAuthorizingRealmTest {

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtStatefulAuthorizingRealm realm = new JwtStatefulAuthorizingRealm();
        assertThat(realm).isNotNull();
    }

    @Test
    @DisplayName("getAuthenticationTokenClass returns JwtAuthorizationToken")
    void testGetAuthenticationTokenClass() {
        JwtStatefulAuthorizingRealm realm = new JwtStatefulAuthorizingRealm();
        assertThat(realm.getAuthenticationTokenClass()).isEqualTo(JwtAuthorizationToken.class);
    }
}
