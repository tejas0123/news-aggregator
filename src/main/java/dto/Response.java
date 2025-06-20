package dto;

import java.util.Optional;

public record Response<T>(
		boolean isOperationSuccessful,
		String message,
		Optional<T> data
	){
}
