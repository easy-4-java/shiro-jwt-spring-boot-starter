package org.apache.shiro.spring.boot.jwt.authz;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtAuthorizationFailureHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthorizationFailureHandler Tests")
class JwtAuthorizationFailureHandlerTest {

    private ServletOutputStream createOutputStream() {
        return new ServletOutputStream() {
            private final OutputStream delegate = new java.io.ByteArrayOutputStream();
            @Override public void write(int b) throws IOException { delegate.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(WriteListener l) {}
        };
    }

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    @DisplayName("getOrder returns MAX_VALUE - 1")
    void testGetOrder() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    @DisplayName("supports returns true for ExpiredJwtException")
    void testSupportsExpiredJwt() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new ExpiredJwtException("expired"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for IncorrectJwtException")
    void testSupportsIncorrectJwt() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new IncorrectJwtException("incorrect"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for InvalidJwtToken")
    void testSupportsInvalidJwt() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new InvalidJwtToken("invalid"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for NotObtainedJwtException")
    void testSupportsNotObtainedJwt() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new NotObtainedJwtException("not obtained"))).isTrue();
    }

    @Test
    @DisplayName("supports returns false for generic AuthenticationException")
    void testSupportsGenericException() {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        assertThat(handler.supports(new AuthenticationException("generic"))).isFalse();
    }

    @Test
    @DisplayName("onAuthorizationFailure handles ExpiredJwtException")
    void testOnFailureExpiredJwt() throws IOException {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        boolean result = handler.onAuthorizationFailure(null, new ExpiredJwtException("expired"), request, response);
        assertThat(result).isFalse();
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("onAuthorizationFailure handles IncorrectJwtException")
    void testOnFailureIncorrectJwt() throws IOException {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        boolean result = handler.onAuthorizationFailure(null, new IncorrectJwtException("incorrect"), mock(HttpServletRequest.class), response);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("onAuthorizationFailure handles InvalidJwtToken")
    void testOnFailureInvalidJwt() throws IOException {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        boolean result = handler.onAuthorizationFailure(null, new InvalidJwtToken("invalid"), mock(HttpServletRequest.class), response);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("onAuthorizationFailure handles NotObtainedJwtException")
    void testOnFailureNotObtainedJwt() throws IOException {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        boolean result = handler.onAuthorizationFailure(null, new NotObtainedJwtException("not obtained"), mock(HttpServletRequest.class), response);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("onAuthorizationFailure handles generic AuthenticationException")
    void testOnFailureGeneric() throws IOException {
        JwtAuthorizationFailureHandler handler = new JwtAuthorizationFailureHandler();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        boolean result = handler.onAuthorizationFailure(null, new AuthenticationException("generic"), mock(HttpServletRequest.class), response);
        assertThat(result).isFalse();
    }
}
