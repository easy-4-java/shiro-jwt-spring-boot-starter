package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtAuthenticatingFilter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtAuthenticatingFilter Tests")
class JwtAuthenticatingFilterTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    @DisplayName("Default values are correct")
    void testDefaultValues() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        assertThat(filter.getJwtPayloadRepository()).isNull();
        assertThat(filter.getFailureHandler()).isNull();
        assertThat(filter.getSuccessHandler()).isNull();
        assertThat(filter.isCheckExpiry()).isFalse();
        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("X-Authorization");
        assertThat(filter.getAuthorizationParamName()).isEqualTo("token");
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("token");
    }

    @Test
    @DisplayName("jwtPayloadRepository getter/setter works correctly")
    void testJwtPayloadRepositoryGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        JwtPayloadRepository repo = mock(JwtPayloadRepository.class);
        filter.setJwtPayloadRepository(repo);
        assertThat(filter.getJwtPayloadRepository()).isEqualTo(repo);
    }

    @Test
    @DisplayName("failureHandler getter/setter works correctly")
    void testFailureHandlerGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
        filter.setFailureHandler(handler);
        assertThat(filter.getFailureHandler()).isEqualTo(handler);
    }

    @Test
    @DisplayName("successHandler getter/setter works correctly")
    void testSuccessHandlerGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        JwtAuthenticationSuccessHandler handler = new JwtAuthenticationSuccessHandler();
        filter.setSuccessHandler(handler);
        assertThat(filter.getSuccessHandler()).isEqualTo(handler);
    }

    @Test
    @DisplayName("checkExpiry getter/setter works correctly")
    void testCheckExpiryGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.setCheckExpiry(true);
        assertThat(filter.isCheckExpiry()).isTrue();
        filter.setCheckExpiry(false);
        assertThat(filter.isCheckExpiry()).isFalse();
    }

    @Test
    @DisplayName("authorizationHeaderName getter/setter works correctly")
    void testAuthorizationHeaderNameGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.setAuthorizationHeaderName("X-Token");
        assertThat(filter.getAuthorizationHeaderName()).isEqualTo("X-Token");
    }

    @Test
    @DisplayName("authorizationParamName getter/setter works correctly")
    void testAuthorizationParamNameGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.setAuthorizationParamName("jwt");
        assertThat(filter.getAuthorizationParamName()).isEqualTo("jwt");
    }

    @Test
    @DisplayName("authorizationCookieName getter/setter works correctly")
    void testAuthorizationCookieNameGetterSetter() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.setAuthorizationCookieName("jwt-cookie");
        assertThat(filter.getAuthorizationCookieName()).isEqualTo("jwt-cookie");
    }

    @Test
    @DisplayName("init does not throw exception")
    void testInit() throws ServletException {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.init(null);
    }

    @Test
    @DisplayName("destroy does not throw exception")
    void testDestroy() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.destroy();
    }

    @Test
    @DisplayName("doFilter passes through when no JWT token present")
    void testDoFilterNoToken() throws IOException, ServletException {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);

        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn(null);
        when(mockRequest.getCookies()).thenReturn(null);

        filter.doFilter(mockRequest, mockResponse, mockChain);
        verify(mockChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("doFilter extracts token from header and attempts login")
    void testDoFilterTokenFromHeader() throws IOException, ServletException {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        filter.setFailureHandler(new JwtAuthenticationFailureHandler());
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);
        when(mockResponse.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override public void write(int b) {}
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener l) {}
        });

        when(mockRequest.getHeader("X-Authorization")).thenReturn("some-jwt-token");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        // Set up a mock SecurityManager and Subject
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
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn("test-token");
        when(mockRequest.getRemoteAddr()).thenReturn("192.168.1.1");

        var token = filter.createJwtToken(mockRequest, null);
        assertThat(token).isInstanceOf(JwtAuthorizationToken.class);
        JwtAuthorizationToken jwtToken = (JwtAuthorizationToken) token;
        assertThat(jwtToken.getToken()).isEqualTo("test-token");
        assertThat(jwtToken.getHost()).isEqualTo("192.168.1.1");
    }

    @Test
    @DisplayName("isJwtSubmission returns true when token is present")
    void testIsJwtSubmissionTrue() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn("token");

        assertThat(filter.isJwtSubmission(mockRequest, null)).isTrue();
    }

    @Test
    @DisplayName("isJwtSubmission returns false when no token")
    void testIsJwtSubmissionFalse() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn(null);
        when(mockRequest.getCookies()).thenReturn(null);

        assertThat(filter.isJwtSubmission(mockRequest, null)).isFalse();
    }

    @Test
    @DisplayName("getAccessToken returns token from header")
    void testGetAccessTokenFromHeader() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn("header-token");

        assertThat(filter.getAccessToken(mockRequest)).isEqualTo("header-token");
    }

    @Test
    @DisplayName("getAccessToken returns token from parameter when header is empty")
    void testGetAccessTokenFromParam() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn("param-token");

        assertThat(filter.getAccessToken(mockRequest)).isEqualTo("param-token");
    }

    @Test
    @DisplayName("getAccessToken returns null when no token found")
    void testGetAccessTokenNull() {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Authorization")).thenReturn(null);
        when(mockRequest.getParameter("token")).thenReturn(null);
        when(mockRequest.getCookies()).thenReturn(null);

        assertThat(filter.getAccessToken(mockRequest)).isNull();
    }

    @Test
    @DisplayName("doFilter succeeds when login succeeds")
    void testDoFilterSuccess() throws IOException, ServletException {
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        JwtAuthenticationSuccessHandler successHandler = new JwtAuthenticationSuccessHandler();
        successHandler.setJwtPayloadRepository(mock(JwtPayloadRepository.class));
        filter.setSuccessHandler(successHandler);
        filter.setCheckExpiry(false);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);

        when(mockRequest.getHeader("X-Authorization")).thenReturn("valid-token");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(mockResponse.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            private final OutputStream delegate = new java.io.ByteArrayOutputStream();
            @Override public void write(int b) throws IOException { delegate.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener l) {}
        });

        SecurityManager sm = mock(SecurityManager.class);
        Subject subject = mock(Subject.class);
        when(subject.getPrincipal()).thenReturn(new org.apache.shiro.biz.authz.principal.ShiroPrincipal());
        ThreadContext.bind(sm);
        ThreadContext.bind(subject);
        // login succeeds (no exception)

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
        JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
        jakarta.servlet.ServletRequest nonHttpReq = mock(jakarta.servlet.ServletRequest.class);
        jakarta.servlet.ServletResponse nonHttpResp = mock(jakarta.servlet.ServletResponse.class);
        FilterChain mockChain = mock(FilterChain.class);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> filter.doFilter(nonHttpReq, nonHttpResp, mockChain))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("just supports HTTP requests");
    }
}
