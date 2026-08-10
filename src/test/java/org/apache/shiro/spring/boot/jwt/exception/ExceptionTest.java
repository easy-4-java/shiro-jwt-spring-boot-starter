package org.apache.shiro.spring.boot.jwt.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for JWT exception classes.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JWT Exception Tests")
class ExceptionTest {

    @Test
    @DisplayName("ExpiredJwtException default constructor")
    void testExpiredJwtDefault() {
        ExpiredJwtException ex = new ExpiredJwtException();
        assertThat(ex).isNotNull();
    }

    @Test
    @DisplayName("ExpiredJwtException with message")
    void testExpiredJwtMessage() {
        ExpiredJwtException ex = new ExpiredJwtException("expired");
        assertThat(ex).hasMessage("expired");
    }

    @Test
    @DisplayName("ExpiredJwtException with message and cause")
    void testExpiredJwtMessageCause() {
        RuntimeException cause = new RuntimeException("root");
        ExpiredJwtException ex = new ExpiredJwtException("expired", cause);
        assertThat(ex).hasMessage("expired");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("ExpiredJwtException with cause")
    void testExpiredJwtCause() {
        RuntimeException cause = new RuntimeException("root");
        ExpiredJwtException ex = new ExpiredJwtException(cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("IncorrectJwtException default constructor")
    void testIncorrectJwtDefault() {
        IncorrectJwtException ex = new IncorrectJwtException();
        assertThat(ex).isNotNull();
    }

    @Test
    @DisplayName("IncorrectJwtException with message")
    void testIncorrectJwtMessage() {
        IncorrectJwtException ex = new IncorrectJwtException("incorrect");
        assertThat(ex).hasMessage("incorrect");
    }

    @Test
    @DisplayName("IncorrectJwtException with message and cause")
    void testIncorrectJwtMessageCause() {
        RuntimeException cause = new RuntimeException("root");
        IncorrectJwtException ex = new IncorrectJwtException("incorrect", cause);
        assertThat(ex).hasMessage("incorrect");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("IncorrectJwtException with cause")
    void testIncorrectJwtCause() {
        RuntimeException cause = new RuntimeException("root");
        IncorrectJwtException ex = new IncorrectJwtException(cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("InvalidJwtToken default constructor")
    void testInvalidJwtDefault() {
        InvalidJwtToken ex = new InvalidJwtToken();
        assertThat(ex).isNotNull();
    }

    @Test
    @DisplayName("InvalidJwtToken with message")
    void testInvalidJwtMessage() {
        InvalidJwtToken ex = new InvalidJwtToken("invalid");
        assertThat(ex).hasMessage("invalid");
    }

    @Test
    @DisplayName("InvalidJwtToken with message and cause")
    void testInvalidJwtMessageCause() {
        RuntimeException cause = new RuntimeException("root");
        InvalidJwtToken ex = new InvalidJwtToken("invalid", cause);
        assertThat(ex).hasMessage("invalid");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("InvalidJwtToken with cause")
    void testInvalidJwtCause() {
        RuntimeException cause = new RuntimeException("root");
        InvalidJwtToken ex = new InvalidJwtToken(cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("NotObtainedJwtException default constructor")
    void testNotObtainedJwtDefault() {
        NotObtainedJwtException ex = new NotObtainedJwtException();
        assertThat(ex).isNotNull();
    }

    @Test
    @DisplayName("NotObtainedJwtException with message")
    void testNotObtainedJwtMessage() {
        NotObtainedJwtException ex = new NotObtainedJwtException("not obtained");
        assertThat(ex).hasMessage("not obtained");
    }

    @Test
    @DisplayName("NotObtainedJwtException with message and cause")
    void testNotObtainedJwtMessageCause() {
        RuntimeException cause = new RuntimeException("root");
        NotObtainedJwtException ex = new NotObtainedJwtException("not obtained", cause);
        assertThat(ex).hasMessage("not obtained");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("NotObtainedJwtException with cause")
    void testNotObtainedJwtCause() {
        RuntimeException cause = new RuntimeException("root");
        NotObtainedJwtException ex = new NotObtainedJwtException(cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
