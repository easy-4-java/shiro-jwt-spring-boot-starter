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
import java.util.Map;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthenticationToken;
import org.apache.shiro.spring.boot.utils.SubjectJwtUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSONObject;
/** Authentication success handler for Jwt.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */


public class JwtAuthenticationSuccessHandler implements Ordered {

	private JwtPayloadRepository jwtPayloadRepository;
	/** If Check JWT Validity. */
	private boolean checkExpiry = false;

	public JwtAuthenticationSuccessHandler() {
	}

	public JwtAuthenticationSuccessHandler(JwtPayloadRepository jwtPayloadRepository, boolean checkExpiry) {
		super();
		this.jwtPayloadRepository = jwtPayloadRepository;
		this.checkExpiry = checkExpiry;
	}

	/** Indicates whether this provider supports the given authentication class.
	 * @param token the token
	 * @return the result
	 */
	public boolean supports(AuthenticationToken token) {
		return SubjectUtils.isAssignableFrom(token.getClass(), JwtAuthenticationToken.class);
	}

	/** Called when an authentication attempt succeeds.
	 * @param token the token
	 * @param request the request
	 * @param response the response
	 * @param subject the subject
	 */
	public void onAuthenticationSuccess(AuthenticationToken token, ServletRequest request, ServletResponse response,
			Subject subject) {

		try {

			String tokenString = "";
			// 账号首次登陆标记
			if(ShiroPrincipal.class.isAssignableFrom(subject.getPrincipal().getClass())) {
				// JSON Web Token (JWT)
				tokenString = getJwtPayloadRepository().issueJwt(token, subject);
			}

			Map<String, Object> tokenMap = SubjectJwtUtils.tokenMap(subject, tokenString);

			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			JSON.writeTo(response.getOutputStream(), tokenMap);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	/** Returns the order.
	 * @return the result
	 */
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
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
