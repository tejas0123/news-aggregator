package dto;

import java.util.Optional;

public record Response<T>(
		boolean isSuccessful,
		String message,
		Optional<T> data
	){
}
