/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot.jwt.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.StringUtils2;
import org.apache.shiro.biz.web.filter.authc.PostLoginRequest;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jwtauthentication (authentication)filter
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtAuthenticatingFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticatingFilter.class);

	/**
     * HTTP Authorization header, equal to <code>X-Authorization</code>
     */
    protected static final String AUTHORIZATION_HEADER = "X-Authorization";
	protected static final String AUTHORIZATION_PARAM = "token";

    private String authorizationHeaderName = AUTHORIZATION_HEADER;
    private String authorizationParamName = AUTHORIZATION_PARAM;
	private String authorizationCookieName = AUTHORIZATION_PARAM;
	private JwtPayloadRepository jwtPayloadRepository;
	private JwtAuthenticationFailureHandler failureHandler;
	private JwtAuthenticationSuccessHandler successHandler;
	/** If Check JWT Validity. */
	private boolean checkExpiry = false;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// no-op
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("just supports HTTP requests");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// Check if this is a JWT submission (has token in header/param/cookie)
		if (isJwtSubmission(request, response)) {
			AuthenticationToken token = createJwtToken(request, response);
			try {
				Subject subject = SecurityUtils.getSubject();
				subject.login(token);
				if (checkExpiry) {
					boolean accessAllowed = getJwtPayloadRepository().verify(token, subject, isCheckExpiry());
					if (!accessAllowed) {
						throw new InvalidJwtToken("Invalid JWT value.");
					}
				}
				// Authentication success
				if (successHandler != null) {
					successHandler.onAuthenticationSuccess(token, request, response, subject);
				}
				filterChain.doFilter(request, response);
				return;
			} catch (AuthenticationException e) {
				// Authentication failure
				if (failureHandler != null) {
					failureHandler.onAuthenticationFailure(token, request, response, e);
				}
				return;
			}
		}

		// Not a JWT submission, pass through
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// no-op
	}

	protected AuthenticationToken createJwtToken(ServletRequest request, ServletResponse response) {
		String host = getRemoteAddr(request);
		String jwtToken = getAccessToken(request);
		return new JwtAuthorizationToken(host, jwtToken, false);
	}

	protected String getRemoteAddr(ServletRequest request) {
		if (request instanceof HttpServletRequest) {
			return ((HttpServletRequest) request).getRemoteAddr();
		}
		return "unknown";
	}

    /** Returns whether the jwt submission is enabled.
     * @param request the request
     * @param response the response
     * @return the result
     */
    protected boolean isJwtSubmission(ServletRequest request, ServletResponse response) {
    	String authzHeader = getAccessToken(request);
		return (request instanceof HttpServletRequest) && authzHeader != null;
	}

    /** Returns the access token.
     * @param request the request
     * @return the result
     */
    protected String getAccessToken(ServletRequest request) {

    	HttpServletRequest httpRequest = (HttpServletRequest) request;
        //从header中获取token
        String token = httpRequest.getHeader(getAuthorizationHeaderName());
        //如果header中不存在token，则从参数中获取token
        if (StringUtils2.isEmpty(token)) {
            return httpRequest.getParameter(getAuthorizationParamName());
        }
        if (StringUtils2.isEmpty(token)) {
            // 从 cookie 获取 token
            Cookie[] cookies = httpRequest.getCookies();
            if (null == cookies || cookies.length == 0) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(getAuthorizationCookieName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

	/** Returns the authorization header name.
	 * @return the result
	 */
	public String getAuthorizationHeaderName() {
		return authorizationHeaderName;
	}

	/** Sets the authorization header name.
	 * @param authorizationHeaderName the authorizationHeaderName
	 */
	public void setAuthorizationHeaderName(String authorizationHeaderName) {
		this.authorizationHeaderName = authorizationHeaderName;
	}

	/** Returns the authorization param name.
	 * @return the result
	 */
	public String getAuthorizationParamName() {
		return authorizationParamName;
	}

	/** Sets the authorization param name.
	 * @param authorizationParamName the authorizationParamName
	 */
	public void setAuthorizationParamName(String authorizationParamName) {
		this.authorizationParamName = authorizationParamName;
	}

	/** Returns the authorization cookie name.
	 * @return the result
	 */
	public String getAuthorizationCookieName() {
		return authorizationCookieName;
	}

	/** Sets the authorization cookie name.
	 * @param authorizationCookieName the authorizationCookieName
	 */
	public void setAuthorizationCookieName(String authorizationCookieName) {
		this.authorizationCookieName = authorizationCookieName;
	}

	/** Returns the jwt payload repository.
	 * @return the result
	 */
	public JwtPayloadRepository getJwtPayloadRepository() {
		return jwtPayloadRepository;
	}

	/** Sets the jwt payload repository.
	 * @param jwtPayloadRepository the jwtPayloadRepository
	 */
	public void setJwtPayloadRepository(JwtPayloadRepository jwtPayloadRepository) {
		this.jwtPayloadRepository = jwtPayloadRepository;
	}

	/** Returns the failure handler.
	 * @return the result
	 */
	public JwtAuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}

	/** Sets the failure handler.
	 * @param failureHandler the failureHandler
	 */
	public void setFailureHandler(JwtAuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	/** Returns the success handler.
	 * @return the result
	 */
	public JwtAuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	/** Sets the success handler.
	 * @param successHandler the successHandler
	 */
	public void setSuccessHandler(JwtAuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}

	/** Returns whether the check expiry is enabled.
	 * @return the result
	 */
	public boolean isCheckExpiry() {
		return checkExpiry;
	}

	/** Sets the check expiry.
	 * @param checkExpiry the checkExpiry
	 */
	public void setCheckExpiry(boolean checkExpiry) {
		this.checkExpiry = checkExpiry;
	}

}
