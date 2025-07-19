package com.itt.newsaggregatorclient.dto;

import java.time.LocalDate;
import java.util.Optional;

public record ArticleFilterParams(
        Optional<LocalDate> from,
        Optional<LocalDate> to,
        Optional<String> category,
        Optional<String> keyword
) {
}
