package ru.sibintek.vme.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePagination() {
        return p -> p.setOneIndexedParameters(false);
    }

    @Bean
    SortHandlerMethodArgumentResolverCustomizer customizeSort() {
        return s -> s.setFallbackSort(Sort.by(Sort.Direction.DESC, "id"));
    }
}
