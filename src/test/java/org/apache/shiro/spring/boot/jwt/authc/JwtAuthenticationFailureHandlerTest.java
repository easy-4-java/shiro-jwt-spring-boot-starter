package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtAuthenticationFailureHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthenticationFailureHandler Tests")
class JwtAuthenticationFailureHandlerTest {

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
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    @DisplayName("getOrder returns MAX_VALUE - 1")
    void testGetOrder() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    @DisplayName("supports returns true for ExpiredJwtException")
    void testSupportsExpiredJwt() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new ExpiredJwtException("expired"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for IncorrectJwtException")
    void testSupportsIncorrectJwt() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new IncorrectJwtException("incorrect"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for InvalidJwtToken")
    void testSupportsInvalidJwt() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new InvalidJwtToken("invalid"))).isTrue();
    }

    @Test
    @DisplayName("supports returns true for NotObtainedJwtException")
    void testSupportsNotObtainedJwt() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new NotObtainedJwtException("not obtained"))).isTrue();
    }

    @Test
    @DisplayName("supports returns false for generic AuthenticationException")
    void testSupportsGenericException() {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        assertThat(handler.supports(new AuthenticationException("generic"))).isFalse();
    }

    @Test
    @DisplayName("onAuthenticationFailure handles ExpiredJwtException")
    void testOnFailureExpiredJwt() throws IOException {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        handler.onAuthenticationFailure(new UsernamePasswordToken("u", "p"), request, response, new ExpiredJwtException("expired"));
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("onAuthenticationFailure handles IncorrectJwtException")
    void testOnFailureIncorrectJwt() throws IOException {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        handler.onAuthenticationFailure(new UsernamePasswordToken("u", "p"), request, response, new IncorrectJwtException("incorrect"));
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("onAuthenticationFailure handles InvalidJwtToken")
    void testOnFailureInvalidJwt() throws IOException {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        handler.onAuthenticationFailure(new UsernamePasswordToken("u", "p"), request, response, new InvalidJwtToken("invalid"));
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("onAuthenticationFailure handles NotObtainedJwtException")
    void testOnFailureNotObtainedJwt() throws IOException {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        handler.onAuthenticationFailure(new UsernamePasswordToken("u", "p"), request, response, new NotObtainedJwtException("not obtained"));
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("onAuthenticationFailure handles generic AuthenticationException")
    void testOnFailureGeneric() throws IOException {
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(createOutputStream());
        handler.onAuthenticationFailure(new UsernamePasswordToken("u", "p"), request, response, new AuthenticationException("generic"));
        verify(response).setStatus(200);
    }
}
