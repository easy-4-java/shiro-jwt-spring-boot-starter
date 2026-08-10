package org.apache.shiro.spring.boot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JSONResult}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("JSONResult Tests")
class JSONResultTest {

    @Test
    @DisplayName("fillResultString returns valid JSON")
    void testFillResultString() {
        String result = JSONResult.fillResultString(200, "success", "data");
        assertThat(result).contains("\"status\":200");
        assertThat(result).contains("\"message\":\"success\"");
        assertThat(result).contains("\"result\":\"data\"");
    }

    @Test
    @DisplayName("fillResultString handles null result")
    void testFillResultStringNullResult() {
        String result = JSONResult.fillResultString(500, "error", null);
        assertThat(result).contains("\"status\":500");
        assertThat(result).contains("\"message\":\"error\"");
    }
}
