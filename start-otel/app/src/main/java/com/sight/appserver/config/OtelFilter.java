package com.sight.appserver.config;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.contrib.sampler.RuleBasedRoutingSampler;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.semconv.UrlAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelFilter {

    /**
     * Otel에서 actuator 경로는 제외하는 필터를 추가한다.
     */
    @Bean
    public AutoConfigurationCustomizerProvider otelCustomizer() {
        return p ->
                p.addSamplerCustomizer((fallback, config) ->
                                RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
                                        .drop(UrlAttributes.URL_PATH, "^/actuator")
                                        .build());
    }
}
