package org.apache.shiro.spring.boot.jwt.realm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.spring.boot.jwt.JwtPayloadPrincipal;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.easy4j.jwt.JwtPayload;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtStatelessAuthorizingRealm}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtStatelessAuthorizingRealm Tests")
class JwtStatelessAuthorizingRealmTest {

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();
        assertThat(realm).isNotNull();
    }

    @Test
    @DisplayName("getAuthenticationTokenClass returns JwtAuthorizationToken")
    void testGetAuthenticationTokenClass() {
        JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();
        assertThat(realm.getAuthenticationTokenClass()).isEqualTo(JwtAuthorizationToken.class);
    }

    @Test
    @DisplayName("doGetAuthorizationInfo returns authorization info with roles and permissions")
    void testDoGetAuthorizationInfo() {
        JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();

        JwtPayload payload = new JwtPayload();
        payload.setSubject("user123");

        JwtPayloadPrincipal principal = new JwtPayloadPrincipal(payload);
        Set<String> perms = new HashSet<>();
        perms.add("read");
        perms.add("write");
        principal.setPerms(perms);

        List<JwtPayload.RolePair> roles = new ArrayList<>();
        JwtPayload.RolePair rolePair = new JwtPayload.RolePair();
        rolePair.setKey("admin");
        roles.add(rolePair);
        principal.setRoles(roles);

        PrincipalCollection principals = new SimplePrincipalCollection(principal, "realm");
        AuthorizationInfo info = realm.doGetAuthorizationInfo(principals);
        assertThat(info).isNotNull();
        assertThat(info.getRoles()).contains("admin");
        assertThat(info.getStringPermissions()).contains("read", "write");
    }
}
