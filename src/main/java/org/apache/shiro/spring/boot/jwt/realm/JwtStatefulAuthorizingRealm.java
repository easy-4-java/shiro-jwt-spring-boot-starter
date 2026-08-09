package org.apache.shiro.spring.boot.jwt.realm;

import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;

/**
 * JSON Web Token (JWT) Stateful AuthorizingRealm
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtStatefulAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	/** Returns the authentication token class.
	 * @return the result
	 */
	public Class<?> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;// 此Realm只支持JwtToken
	}

}
