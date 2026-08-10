package org.apache.shiro.spring.boot.jwt.authz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtWithinExpiryFilter}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtWithinExpiryFilter Tests")
class JwtWithinExpiryFilterTest {

    @Test
    @DisplayName("Instance can be created")
    void testInstantiation() {
        JwtWithinExpiryFilter filter = new JwtWithinExpiryFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    @DisplayName("Default checkExpiry is true")
    void testDefaultCheckExpiry() {
        JwtWithinExpiryFilter filter = new JwtWithinExpiryFilter();
        assertThat(filter.isCheckExpiry()).isTrue();
    }
}
