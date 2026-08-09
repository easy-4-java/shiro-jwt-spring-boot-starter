package org.apache.shiro.spring.boot.jwt.authz;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;

/**
 * Jwtfilter
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class JwtWithinExpiryFilter extends JwtAuthorizationFilter {
	
	@Override
	protected boolean onAccessSuccess(Object mappedValue, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return true;
	}
	
}
