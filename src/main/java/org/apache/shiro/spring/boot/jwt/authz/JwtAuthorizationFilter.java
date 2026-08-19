package org.apache.shiro.spring.boot.jwt.authz;

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
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.ShiroJwtMessageSource;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSONObject;

/**
 * Jwtauthorization (authorization)filter
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class JwtAuthorizationFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	protected MessageSourceAccessor messages = ShiroJwtMessageSource.getAccessor();

	/**
     * HTTP Authorization header, equal to <code>X-Authorization</code>
     */
    protected static final String AUTHORIZATION_HEADER = "X-Authorization";
    protected static final String AUTHORIZATION_PARAM = "token";

    private String authorizationHeaderName = AUTHORIZATION_HEADER;
    private String authorizationParamName = AUTHORIZATION_PARAM;
	private String authorizationCookieName = AUTHORIZATION_PARAM;
	private JwtPayloadRepository jwtPayloadRepository;
	/** If Check JWT Validity. */
	private boolean checkExpiry = false;

	@Override
	/**
	 * init.
	 *
	 * @param filterConfig the filter config
	 * @throws ServletException if an error occurs
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// no-op
	}

	@Override
	/**
	 * do Filter.
	 *
	 * @param request the request
	 * @param response the response
	 * @param filterChain the filter chain
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("just supports HTTP requests");
		}

		// Check if this is a JWT submission
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
				// Authorization success, continue
				filterChain.doFilter(request, response);
				return;
			} catch (AuthenticationException e) {
				// Authorization failure
				LOG.debug("JWT authorization failure: {}", e.getMessage());

				String mString = "JWT authorization failed: " + e.getMessage();
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(HttpStatus.SC_OK);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding(StandardCharsets.UTF_8.name());
				JSON.writeTo(response.getOutputStream(), AuthcResponse.fail(HttpStatus.SC_UNAUTHORIZED, mString));
				return;
			}
		}

		// No JWT token provided
		String mString = String.format("Attempting to access a path which requires authentication.  %s = Authorization Header or %s = Authorization Param or %s = Authorization Cookie  is not present in the request",
				getAuthorizationHeaderName(), getAuthorizationParamName(), getAuthorizationCookieName());
		if (LOG.isTraceEnabled()) {
			LOG.trace(mString);
		}

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpStatus.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		JSON.writeTo(response.getOutputStream(), AuthcResponse.fail(HttpStatus.SC_UNAUTHORIZED, mString));
	}

	@Override
	/**
	 * destroy.
	 *
	 */
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
