package org.apache.shiro.spring.boot.jwt.authz;

/**
 * Jwtfilter with expiry checking enabled.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class JwtWithinExpiryFilter extends JwtAuthorizationFilter {

	public JwtWithinExpiryFilter() {
		super();
		setCheckExpiry(true);
	}

}
