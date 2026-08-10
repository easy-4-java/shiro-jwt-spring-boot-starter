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
package org.apache.shiro.spring.boot.jwt.authz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.authc.AuthcResponseCode;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.ShiroJwtMessageSource;
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticationFailureHandler;
import org.apache.shiro.spring.boot.jwt.exception.ExpiredJwtException;
import org.apache.shiro.spring.boot.jwt.exception.IncorrectJwtException;
import org.apache.shiro.spring.boot.jwt.exception.InvalidJwtToken;
import org.apache.shiro.spring.boot.jwt.exception.NotObtainedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSONObject;

/**
 * Authorization failure handler for JWT.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtAuthorizationFailureHandler implements Ordered {

	protected MessageSourceAccessor messages = ShiroJwtMessageSource.getAccessor();
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

	/** Indicates whether this provider supports the given authentication class.
	 * @param ex the ex
	 * @return the result
	 */
	public boolean supports(AuthenticationException ex) {
		return SubjectUtils.isAssignableFrom(ex.getClass(), ExpiredJwtException.class,
				IncorrectJwtException.class, InvalidJwtToken.class, NotObtainedJwtException.class);
	}

	public boolean onAuthorizationFailure(Object mappedValue, AuthenticationException ex, ServletRequest request,
			ServletResponse response) throws IOException {

		if(LOG.isDebugEnabled()) {
			LOG.debug(ExceptionUtils.getRootCauseMessage(ex));
		}

		try {

			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			// Jwt过期
			if (ex instanceof ExpiredJwtException) {
				JSON.writeTo(response.getOutputStream(), AuthcResponse.error(AuthcResponseCode.SC_AUTHZ_TOKEN_EXPIRED.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHZ_TOKEN_EXPIRED.getMsgKey(), ex.getMessage())));
			}
			// Jwt错误
			else if (ex instanceof IncorrectJwtException) {
				JSON.writeTo(response.getOutputStream(), AuthcResponse.error(AuthcResponseCode.SC_AUTHZ_TOKEN_INCORRECT.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHZ_TOKEN_INCORRECT.getMsgKey(), ex.getMessage())));
			}
			// Jwt无效
			else if (ex instanceof InvalidJwtToken) {
				JSON.writeTo(response.getOutputStream(), AuthcResponse.error(AuthcResponseCode.SC_AUTHZ_TOKEN_INVALID.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHZ_TOKEN_INVALID.getMsgKey(), ex.getMessage())));
			}
			// Jwt缺失
			else if (ex instanceof NotObtainedJwtException) {
				JSON.writeTo(response.getOutputStream(), AuthcResponse.error(AuthcResponseCode.SC_AUTHZ_TOKEN_REQUIRED.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHZ_TOKEN_REQUIRED.getMsgKey(), ex.getMessage())));
			} else {
				JSON.writeTo(response.getOutputStream(), AuthcResponse.error(AuthcResponseCode.SC_AUTHC_FAIL.getCode(),
						messages.getMessage(AuthcResponseCode.SC_AUTHC_FAIL.getMsgKey())));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			JSON.writeTo(response.getOutputStream(), AuthcResponse.error("Unauthentication."));
		}

		return false;
	}

	@Override
	/** Returns the order.
	 * @return the result
	 */
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
	}

}
