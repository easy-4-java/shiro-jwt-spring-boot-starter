package org.apache.shiro.spring.boot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StringUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("StringUtils Tests")
class StringUtilsTest {

    @Test
    @DisplayName("isEmpty returns true for null")
    void testIsEmptyNull() {
        assertThat(StringUtils.isEmpty(null)).isTrue();
    }

    @Test
    @DisplayName("isEmpty returns true for empty string")
    void testIsEmptyEmpty() {
        assertThat(StringUtils.isEmpty("")).isTrue();
    }

    @Test
    @DisplayName("isEmpty returns false for non-empty string")
    void testIsEmptyNonEmpty() {
        assertThat(StringUtils.isEmpty("hello")).isFalse();
    }

    @Test
    @DisplayName("isEmpty returns true for non-string null")
    void testIsEmptyNullObject() {
        assertThat(StringUtils.isEmpty((Object) null)).isTrue();
    }

    @Test
    @DisplayName("tokenizeToStringArray splits by default delimiters")
    void testTokenizeToStringArray() {
        String[] result = StringUtils.tokenizeToStringArray("a,b;c d");
        assertThat(result).containsExactly("a", "b", "c", "d");
    }

    @Test
    @DisplayName("tokenizeToStringArray returns empty array for null")
    void testTokenizeToStringArrayNull() {
        String[] result = StringUtils.tokenizeToStringArray(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("tokenizeToStringArray with custom delimiters")
    void testTokenizeToStringArrayCustomDelimiters() {
        String[] result = StringUtils.tokenizeToStringArray("a|b|c", "|");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    @DisplayName("CONFIG_LOCATION_DELIMITERS has expected value")
    void testConfigLocationDelimiters() {
        assertThat(StringUtils.CONFIG_LOCATION_DELIMITERS).isEqualTo(",; \t\n");
    }
}
