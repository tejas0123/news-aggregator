package model;

import java.time.Instant;
import java.time.LocalDate;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String password;
    private Instant createdAt;
}
