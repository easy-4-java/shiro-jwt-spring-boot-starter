package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JwtPayloadRepository}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtPayloadRepository Tests")
class JwtPayloadRepositoryTest {

    @Test
    @DisplayName("Interface can be loaded")
    void testInterfaceExists() {
        assertThat(JwtPayloadRepository.class).isInterface();
    }

    @Test
    @DisplayName("Default issueJwt with token and subject returns empty string")
    void testDefaultIssueJwt() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        Subject subject = mock(Subject.class);
        when(subject.getPrincipal()).thenReturn("anonymous");
        AuthenticationToken token = new UsernamePasswordToken("user", "pass");
        assertThat(repo.issueJwt(token, subject)).isEmpty();
    }

    @Test
    @DisplayName("Default issueJwt with ShiroPrincipal returns empty string")
    void testDefaultIssueJwtWithPrincipal() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        ShiroPrincipal principal = new ShiroPrincipal();
        principal.setUserid("123");
        assertThat(repo.issueJwt(principal)).isEmpty();
    }

    @Test
    @DisplayName("Default issueJwt with userId and profile returns empty string")
    void testDefaultIssueJwtWithUserId() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.issueJwt("user123", null)).isEmpty();
    }

    @Test
    @DisplayName("Default verify with token and subject returns false")
    void testDefaultVerify() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        AuthenticationToken token = new UsernamePasswordToken("user", "pass");
        Subject subject = mock(Subject.class);
        assertThat(repo.verify(token, subject, false)).isFalse();
    }

    @Test
    @DisplayName("Default verify with string token returns false")
    void testDefaultVerifyString() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.verify("token", false)).isFalse();
    }

    @Test
    @DisplayName("Default getPayload with JwtAuthorizationToken returns null")
    void testDefaultGetPayload() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        JwtAuthorizationToken token = new JwtAuthorizationToken("host", "jwt", false);
        assertThat(repo.getPayload(token, false)).isNull();
    }

    @Test
    @DisplayName("Default getPayload with string token returns null")
    void testDefaultGetPayloadString() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        assertThat(repo.getPayload("token", false)).isNull();
    }
}
