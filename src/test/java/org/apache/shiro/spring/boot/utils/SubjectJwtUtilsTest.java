package org.apache.shiro.spring.boot.utils;

import java.util.Map;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SubjectJwtUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("SubjectJwtUtils Tests")
class SubjectJwtUtilsTest {

    @Test
    @DisplayName("tokenMap returns map with ShiroPrincipal")
    void testTokenMapWithShiroPrincipal() {
        Subject subject = mock(Subject.class);
        ShiroPrincipal principal = new ShiroPrincipal();
        principal.setUserid("123");
        principal.setUsername("testuser");
        principal.setUserkey("key");
        principal.setUsercode("code");
        principal.setNickname("Test User");
        principal.setRoleid("role1");
        principal.setRole("admin");
        when(subject.getPrincipal()).thenReturn(principal);

        Map<String, Object> tokenMap = SubjectJwtUtils.tokenMap(subject, "jwt-token");
        assertThat(tokenMap).containsKey("code");
        assertThat(tokenMap).containsKey("token");
        assertThat(tokenMap.get("token")).isEqualTo("jwt-token");
        assertThat(tokenMap.get("userid")).isEqualTo("123");
        assertThat(tokenMap.get("username")).isEqualTo("testuser");
    }

    @Test
    @DisplayName("tokenMap returns map with non-ShiroPrincipal")
    void testTokenMapWithNonShiroPrincipal() {
        Subject subject = mock(Subject.class);
        when(subject.getPrincipal()).thenReturn("anonymous");

        Map<String, Object> tokenMap = SubjectJwtUtils.tokenMap(subject, "jwt-token");
        assertThat(tokenMap).containsKey("code");
        assertThat(tokenMap.get("token")).isEqualTo("null");
        assertThat(tokenMap.get("username")).isEqualTo("null");
    }
}
