package org.apache.shiro.spring.boot.jwt.authz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import com.alibaba.fastjson2.JSON;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.StringUtils;
import org.apache.shiro.biz.utils.WebUtils;
import org.apache.shiro.biz.web.filter.authz.AbstracAuthorizationFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
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
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtAuthorizationFilter extends AbstracAuthorizationFilter {

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
	/** Returns whether the access allowed is enabled.
	 * @param request the request
	 * @param response the response
	 * @param mappedValue the mappedValue
	 * @return the result
	 */
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 判断是否认证请求
		if (isJwtSubmission(request, response)) {
			// Step 1、生成无状态Token
			AuthenticationToken token = createJwtToken(request, response);
			try {
				//Step 2、委托给Realm进行登录
				Subject subject = getSubject(request, response);
				subject.login(token);
				if(checkExpiry) {
					// Step 3、委托给JwtPayloadRepository进行Token验证
					boolean accessAllowed = getJwtPayloadRepository().verify(token, subject, isCheckExpiry());
					if (!accessAllowed) {
						throw new InvalidJwtToken("Invalid JWT value.");
					}
				}
				//Step 3、执行授权成功后的函数
				return onAccessSuccess(mappedValue, subject, request, response);
			} catch (AuthenticationException e) {
				//Step 4、执行授权失败后的函数
				return onAccessFailure(mappedValue, e, request, response);
			}
		}

		String mString = String.format("Attempting to access a path which requires authentication.  %s = Authorization Header or %s = Authorization Param or %s = Authorization Cookie  is not present in the request",
				getAuthorizationHeaderName(), getAuthorizationParamName(), getAuthorizationCookieName());
		if (LOG.isTraceEnabled()) {
			LOG.trace(mString);
		}
		WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		JSON.writeTo(response.getOutputStream(), AuthcResponse.fail(HttpStatus.SC_UNAUTHORIZED, mString));

		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		return false;
	}

	protected AuthenticationToken createJwtToken(ServletRequest request, ServletResponse response) {
		String host = WebUtils.getRemoteAddr(request);
		String jwtToken = getAccessToken(request);
		return new JwtAuthorizationToken(host, jwtToken, false);
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

    	HttpServletRequest httpRequest = WebUtils.toHttp(request);
        //从header中获取token
        String token = httpRequest.getHeader(getAuthorizationHeaderName());
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isEmpty(token)) {
            return httpRequest.getParameter(getAuthorizationParamName());
        }
        if (StringUtils.isEmpty(token)) {
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
