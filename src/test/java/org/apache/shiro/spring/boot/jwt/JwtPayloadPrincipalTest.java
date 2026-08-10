package org.apache.shiro.spring.boot.jwt;

import io.github.easy4j.jwt.JwtPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtPayloadPrincipal}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JwtPayloadPrincipal Tests")
class JwtPayloadPrincipalTest {

    @Test
    @DisplayName("Instance can be created with null payload")
    void testInstantiationNull() {
        JwtPayloadPrincipal instance = new JwtPayloadPrincipal(null);
        assertThat(instance).isNotNull();
        assertThat(instance.getPayload()).isNull();
    }

    @Test
    @DisplayName("Instance can be created with payload")
    void testInstantiationWithPayload() {
        JwtPayload payload = new JwtPayload();
        payload.setSubject("user123");
        JwtPayloadPrincipal instance = new JwtPayloadPrincipal(payload);
        assertThat(instance).isNotNull();
        assertThat(instance.getPayload()).isEqualTo(payload);
        assertThat(instance.getPayload().getSubject()).isEqualTo("user123");
    }

    @Test
    @DisplayName("getPayload returns the payload set in constructor")
    void testGetPayload() {
        JwtPayload payload = new JwtPayload();
        JwtPayloadPrincipal instance = new JwtPayloadPrincipal(payload);
        assertThat(instance.getPayload()).isSameAs(payload);
    }
}
