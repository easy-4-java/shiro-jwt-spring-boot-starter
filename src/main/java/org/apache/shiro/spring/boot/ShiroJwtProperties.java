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
package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
/** Configuration properties for Shiro Jwt.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */

@ConfigurationProperties(ShiroJwtProperties.PREFIX)
public class ShiroJwtProperties {

	public static final String PREFIX = "shiro.jwt";

	// 默认HMAC签名有效期：1分钟=60000毫秒(ms)
	protected static final Integer DEFAULT_HMAC_PERIOD = 60000;
	// 默认HASH加密算法
	protected static final String DEFAULT_HASH_ALGORITHM_NAME = "MD5";
	// 默认HASH加密盐
	protected static final String DEFAULT_HASH_SALT = "A1B2C3D4efg.5679g8e7d6c5b4a_-=_)(8.";
	// 默认HASH加密迭代次数
	protected static final Integer DEFAULT_HASH_ITERATIONS = 2;

	// 默认JWT加密算法
	protected static final String DEFAULT_HMAC_ALGORITHM_NAME = "HmacMD5";
	// HASH加密算法
	public static final String HASH_ALGORITHM_NAME_MD5 = "MD5";
	public static final String HASH_ALGORITHM_NAME_SHA1 = "SHA-1";
	public static final String HASH_ALGORITHM_NAME_SHA256 = "SHA-256";
	public static final String HASH_ALGORITHM_NAME_SHA512 = "SHA-512";
	// HMACA签名算法
	public static final String HMAC_ALGORITHM_NAME_MD5 = "HmacMD5";// 128位
	public static final String HMAC_ALGORITHM_NAME_SHA1 = "HmacSHA1";// 126
	public static final String HMAC_ALGORITHM_NAME_SHA256 = "HmacSHA256";// 256
	public static final String HMAC_ALGORITHM_NAME_SHA512 = "HmacSHA512";// 512

	/**
	 * Enable Shiro JWT.
	 */
	private boolean enabled = false;

	/**
	 * If Check JWT Validity.
	 */
	private boolean checkExpiry;

	/**
	 * {@link JwtAuthorizationToken} will expire after this time.
	 */
	private Long tokenExpirationTime;

	/**
	 * Token issuer.
	 */
	private String tokenIssuer;

	/**
	 * Key is used to sign {@link JwtAuthorizationToken}.
	 */
	private String tokenSigningKey;

	/**
	 * {@link JwtAuthorizationToken} can be refreshed during this timeframe.
	 */
	private Integer refreshTokenExpTime;

	private Long access_token_expiration;

	private Long refresh_token_expiration;

	/** Returns whether the enabled is enabled.
	 * @return the result
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/** Sets the enabled.
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	/** Returns the token expiration time.
	 * @return the result
	 */
	public Long getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	/** Sets the token expiration time.
	 * @param tokenExpirationTime the tokenExpirationTime
	 */
	public void setTokenExpirationTime(Long tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

	/** Returns the token issuer.
	 * @return the result
	 */
	public String getTokenIssuer() {
		return tokenIssuer;
	}

	/** Sets the token issuer.
	 * @param tokenIssuer the tokenIssuer
	 */
	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	/** Returns the token signing key.
	 * @return the result
	 */
	public String getTokenSigningKey() {
		return tokenSigningKey;
	}

	/** Sets the token signing key.
	 * @param tokenSigningKey the tokenSigningKey
	 */
	public void setTokenSigningKey(String tokenSigningKey) {
		this.tokenSigningKey = tokenSigningKey;
	}

	/** Returns the refresh token exp time.
	 * @return the result
	 */
	public Integer getRefreshTokenExpTime() {
		return refreshTokenExpTime;
	}

	/** Sets the refresh token exp time.
	 * @param refreshTokenExpTime the refreshTokenExpTime
	 */
	public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
		this.refreshTokenExpTime = refreshTokenExpTime;
	}

	/** Returns the access_token_expiration.
	 * @return the result
	 */
	public Long getAccess_token_expiration() {
		return access_token_expiration;
	}

	/** Sets the access_token_expiration.
	 * @param access_token_expiration the access_token_expiration
	 */
	public void setAccess_token_expiration(Long access_token_expiration) {
		this.access_token_expiration = access_token_expiration;
	}

	/** Returns the refresh_token_expiration.
	 * @return the result
	 */
	public Long getRefresh_token_expiration() {
		return refresh_token_expiration;
	}

	/** Sets the refresh_token_expiration.
	 * @param refresh_token_expiration the refresh_token_expiration
	 */
	public void setRefresh_token_expiration(Long refresh_token_expiration) {
		this.refresh_token_expiration = refresh_token_expiration;
	}

}
