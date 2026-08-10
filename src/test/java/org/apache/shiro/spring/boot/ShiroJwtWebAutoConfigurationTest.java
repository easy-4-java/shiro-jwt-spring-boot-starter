package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ShiroJwtWebAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroJwtWebAutoConfiguration Tests")
class ShiroJwtWebAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Configuration
    static class TestConfig {
        @Bean
        public JwtPayloadRepository jwtPayloadRepository() {
            return new JwtPayloadRepository() {};
        }
    }

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        ShiroJwtWebAutoConfiguration configuration = new ShiroJwtWebAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration loads when 'shiro.jwt.enabled=true'")
    void testLoadsWhenEnabledPropertySet() {
        runner.withUserConfiguration(TestConfig.class, ShiroJwtWebAutoConfiguration.class)
                .withPropertyValues("shiro.jwt.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(ShiroJwtWebAutoConfiguration.class);
                    assertThat(context).hasSingleBean(JwtPayloadRepository.class);
                });
    }

    @Test
    @DisplayName("Auto-configuration is absent when property is not set")
    void testNotLoadedWhenPropertyAbsent() {
        runner.withUserConfiguration(TestConfig.class, ShiroJwtWebAutoConfiguration.class)
                .run(context -> assertThat(context).doesNotHaveBean(ShiroJwtWebAutoConfiguration.class));
    }
}
