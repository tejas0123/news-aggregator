package model;

import java.time.LocalDate;
import java.util.Optional;

public record SearchParams(
	Optional<String> category,
	Optional<String> keyword,
	Optional<LocalDate> from,
	Optional<LocalDate> to
) {

}
