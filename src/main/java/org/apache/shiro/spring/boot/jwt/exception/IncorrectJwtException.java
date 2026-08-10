package org.apache.shiro.spring.boot.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;
/** Exception thrown when a Incorrect Jwt error occurs.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class IncorrectJwtException extends AuthenticationException {
	
	public IncorrectJwtException() {
		super();
	}

	public IncorrectJwtException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectJwtException(String message) {
		super(message);
	}

	public IncorrectJwtException(Throwable cause) {
		super(cause);
	}
	
}
