package org.apache.shiro.spring.boot.jwt.authc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtSubjectFactory}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtSubjectFactory Tests")
class JwtSubjectFactoryTest {

    @Test
    @DisplayName("Constructor with sessionCreationEnabled=true")
    void testConstructorEnabled() {
        JwtSubjectFactory factory = new JwtSubjectFactory(true);
        assertThat(factory).isNotNull();
        assertThat(factory.isSessionCreationEnabled()).isTrue();
    }

    @Test
    @DisplayName("Constructor with sessionCreationEnabled=false")
    void testConstructorDisabled() {
        JwtSubjectFactory factory = new JwtSubjectFactory(false);
        assertThat(factory).isNotNull();
        assertThat(factory.isSessionCreationEnabled()).isFalse();
    }
}
