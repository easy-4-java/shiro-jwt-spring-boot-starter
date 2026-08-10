package org.apache.shiro.spring.boot.jwt.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.spring.boot.jwt.token.JwtAuthorizationToken;

/**
 * JSON Web Token (JWT) Stateful AuthorizingRealm
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtStatefulAuthorizingRealm extends AbstractAuthorizingRealm {

	@Override
	/** Returns the authentication token class.
	 * @return the result
	 */
	public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
		return JwtAuthorizationToken.class;// 此Realm只支持JwtToken
	}

}
