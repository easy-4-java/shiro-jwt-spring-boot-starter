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
package org.apache.shiro.spring.boot.jwt;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;

import io.github.easy4j.jwt.JwtPayload;

/**
 * TODO
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class JwtPayloadPrincipal extends ShiroPrincipal {

	private final JwtPayload payload;
	
	public JwtPayloadPrincipal(JwtPayload payload) {
		this.payload = payload;
	}

	/** Returns the payload.
	 * @return the result
	 */
	public JwtPayload getPayload() {
		return payload;
	}
	
}
