package models;

import play.data.validation.Constraints;

public class Patient {
    @Constraints.Required
    public String firstName;
    @Constraints.Required
    public String lastName;
    @Constraints.Required
    public String id;
    @Constraints.Required
    @Constraints.Pattern(message="Possible values are male or female", value="M|F")
    public String sex;

    public Patient() {
    }

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public Patient(String firstName, String lastName, String id, String sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.sex = sex;
    }
}
