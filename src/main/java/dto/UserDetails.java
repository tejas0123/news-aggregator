package dto;

import java.time.LocalDate;

import model.Gender;

public record UserDetails(
		String firstName,
	    String lastName,
	    String email,
	    Gender gender,
	    LocalDate dateOfBirth,
	    String phone,
	    String password
) {

}
