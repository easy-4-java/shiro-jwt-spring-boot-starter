package org.apache.shiro.spring.boot.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;
/** The Invalid Jwt Token.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class InvalidJwtToken extends AuthenticationException {
	
	public InvalidJwtToken() {
		super();
	}

	public InvalidJwtToken(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidJwtToken(String message) {
		super(message);
	}

	public InvalidJwtToken(Throwable cause) {
		super(cause);
	}
	
}
