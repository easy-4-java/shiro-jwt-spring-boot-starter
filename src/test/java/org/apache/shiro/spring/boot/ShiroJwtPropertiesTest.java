package org.apache.shiro.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ShiroJwtProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroJwtProperties Tests")
class ShiroJwtPropertiesTest {

    @Test
    @DisplayName("PREFIX constant has expected value")
    void testPREFIXConstant() {
        assertThat(ShiroJwtProperties.PREFIX).isEqualTo("shiro.jwt");
    }

    @Test
    @DisplayName("Default values are correct")
    void testDefaultValues() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.isCheckExpiry()).isFalse();
        assertThat(props.getTokenExpirationTime()).isNull();
        assertThat(props.getTokenIssuer()).isNull();
        assertThat(props.getTokenSigningKey()).isNull();
        assertThat(props.getRefreshTokenExpTime()).isNull();
        assertThat(props.getAccess_token_expiration()).isNull();
        assertThat(props.getRefresh_token_expiration()).isNull();
    }

    @Test
    @DisplayName("enabled getter/setter works correctly")
    void testEnabled() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("checkExpiry getter/setter works correctly")
    void testCheckExpiry() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setCheckExpiry(true);
        assertThat(props.isCheckExpiry()).isTrue();
        props.setCheckExpiry(false);
        assertThat(props.isCheckExpiry()).isFalse();
    }

    @Test
    @DisplayName("tokenExpirationTime getter/setter works correctly")
    void testTokenExpirationTime() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setTokenExpirationTime(3600000L);
        assertThat(props.getTokenExpirationTime()).isEqualTo(3600000L);
    }

    @Test
    @DisplayName("tokenIssuer getter/setter works correctly")
    void testTokenIssuer() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setTokenIssuer("my-app");
        assertThat(props.getTokenIssuer()).isEqualTo("my-app");
    }

    @Test
    @DisplayName("tokenSigningKey getter/setter works correctly")
    void testTokenSigningKey() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setTokenSigningKey("secret-key");
        assertThat(props.getTokenSigningKey()).isEqualTo("secret-key");
    }

    @Test
    @DisplayName("refreshTokenExpTime getter/setter works correctly")
    void testRefreshTokenExpTime() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setRefreshTokenExpTime(7200);
        assertThat(props.getRefreshTokenExpTime()).isEqualTo(7200);
    }

    @Test
    @DisplayName("access_token_expiration getter/setter works correctly")
    void testAccess_token_expiration() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setAccess_token_expiration(1800000L);
        assertThat(props.getAccess_token_expiration()).isEqualTo(1800000L);
    }

    @Test
    @DisplayName("refresh_token_expiration getter/setter works correctly")
    void testRefresh_token_expiration() {
        ShiroJwtProperties props = new ShiroJwtProperties();
        props.setRefresh_token_expiration(3600000L);
        assertThat(props.getRefresh_token_expiration()).isEqualTo(3600000L);
    }

    @Test
    @DisplayName("Hash algorithm constants have expected values")
    void testHashConstants() {
        assertThat(ShiroJwtProperties.HASH_ALGORITHM_NAME_MD5).isEqualTo("MD5");
        assertThat(ShiroJwtProperties.HASH_ALGORITHM_NAME_SHA1).isEqualTo("SHA-1");
        assertThat(ShiroJwtProperties.HASH_ALGORITHM_NAME_SHA256).isEqualTo("SHA-256");
        assertThat(ShiroJwtProperties.HASH_ALGORITHM_NAME_SHA512).isEqualTo("SHA-512");
    }

    @Test
    @DisplayName("HMAC algorithm constants have expected values")
    void testHmacConstants() {
        assertThat(ShiroJwtProperties.HMAC_ALGORITHM_NAME_MD5).isEqualTo("HmacMD5");
        assertThat(ShiroJwtProperties.HMAC_ALGORITHM_NAME_SHA1).isEqualTo("HmacSHA1");
        assertThat(ShiroJwtProperties.HMAC_ALGORITHM_NAME_SHA256).isEqualTo("HmacSHA256");
        assertThat(ShiroJwtProperties.HMAC_ALGORITHM_NAME_SHA512).isEqualTo("HmacSHA512");
    }
}
