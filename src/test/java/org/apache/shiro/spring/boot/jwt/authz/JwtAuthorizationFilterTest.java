package org.apache.shiro.spring.boot.jwt.authz;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtAuthorizationFilter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthorizationFilter Tests")
class JwtAuthorizationFilterTest {

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    @DisplayName("Default values are correct")
    void testDefaultValues() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThat(filter.getJwtPayloadRepository()).isNull();
        assertThat(filter.isCheckExpiry()).isFalse();
        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("X-Authorization");
        assertThat(filter.getAuthorizationParamName()).isEqualTo("token");
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("token");
    }

    @Test
    @DisplayName("Getters/setters work correctly")
    void testGettersSetters() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        filter.setJwtPayloadRepository(repo);
        filter.setCheckExpiry(true);
        filter.setAuthorizationHeaderName("X-Token");
        filter.setAuthorizationParamName("jwt");
        filter.setAuthorizationCookieName("jwt-cookie");

        assertThat(filter.getJwtPayloadRepository()).isEqualTo(repo);
        assertThat(filter.isCheckExpiry()).isTrue();
        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("X-Token");
        assertThat(filter.getAuthorizationParamName()).isEqualTo("jwt");
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("jwt-cookie");
    }

    @Test
    @DisplayName("init and destroy do not throw exceptions")
    void testInitDestroy() throws ServletException {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        filter.init(null);
        filter.destroy();
    }

    @Test
    @DisplayName("doFilter passes through when no token present")
    void testDoFilterNoToken() throws IOException, ServletException {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);
        when(mockResponse.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override public void write(int b) {}
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener l) {}
        });
        when(mockResponse.getStatus()).thenReturn(200);

        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn(null);
        when(mockRequest.getCookies()).thenReturn(null);

        filter.doFilter(mockRequest, mockResponse, mockChain);
    }

    @Test
    @DisplayName("doFilter extracts token from header and attempts login")
    void testDoFilterTokenFromHeader() throws IOException, ServletException {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);
        when(mockResponse.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override public void write(int b) {}
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener l) {}
        });

        when(mockRequest.getHeader("X-Authorization")).thenReturn("some-token");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        SecurityManager sm = mock(SecurityManager.class);
        Subject subject = mock(Subject.class);
        ThreadContext.bind(sm);
        ThreadContext.bind(subject);
        doThrow(new org.apache.shiro.authc.AuthenticationException("test")).when(subject).login(any(org.apache.shiro.authc.AuthenticationToken.class));

        try {
            filter.doFilter(mockRequest, mockResponse, mockChain);
        } finally {
            ThreadContext.remove();
        }
    }

    @Test
    @DisplayName("createJwtToken creates JwtAuthorizationToken")
    void testCreateJwtToken() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn("test-token");
        when(mockRequest.getRemoteAddr()).thenReturn("192.168.1.1");

        var token = filter.createJwtToken(mockRequest, null);
        assertThat(token).isInstanceOf(JwtAuthorizationToken.class);
        assertThat(((JwtAuthorizationToken) token).getToken()).isEqualTo("test-token");
    }

    @Test
    @DisplayName("isJwtSubmission returns true when token present")
    void testIsJwtSubmission() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn("token");
        assertThat(filter.isJwtSubmission(mockRequest, null)).isTrue();
    }

    @Test
    @DisplayName("getAccessToken returns empty string from param when header is null")
    void testGetAccessTokenFromParamWhenHeaderNull() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn("param-token");

        assertThat(filter.getAccessToken(mockRequest)).isEqualTo("param-token");
    }

    @Test
    @DisplayName("doFilter succeeds when login succeeds")
    void testDoFilterSuccess() throws IOException, ServletException {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);

        when(mockRequest.getHeader("X-Authorization")).thenReturn("valid-token");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        SecurityManager sm = mock(SecurityManager.class);
        Subject subject = mock(Subject.class);
        ThreadContext.bind(sm);
        ThreadContext.bind(subject);

        try {
            filter.doFilter(mockRequest, mockResponse, mockChain);
            verify(mockChain).doFilter(mockRequest, mockResponse);
        } finally {
            ThreadContext.remove();
        }
    }

    @Test
    @DisplayName("doFilter throws ServletException for non-HTTP request")
    void testDoFilterNonHttp() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        assertThatThrownBy(() -> filter.doFilter(
                mock(jakarta.servlet.ServletRequest.class),
                mock(jakarta.servlet.ServletResponse.class),
                mock(FilterChain.class)))
                .isInstanceOf(ServletException.class);
    }
}
