package model;

import java.time.Instant;
import java.time.LocalDate;

public class User {
	private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String password;
    private Instant createdAt;
}
