package org.apache.shiro.spring.boot.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;
/** Exception thrown when a Not Obtained Jwt error occurs.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class NotObtainedJwtException extends AuthenticationException {
	
	public NotObtainedJwtException() {
		super();
	}

	public NotObtainedJwtException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotObtainedJwtException(String message) {
		super(message);
	}

	public NotObtainedJwtException(Throwable cause) {
		super(cause);
	}
	
}
