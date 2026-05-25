package pro.bukhman.validation;

public class NewPlayerValidator {
    public void validate(String firstName, String lastName) {
        validateName(firstName, "FirstName");
        validateName(lastName, "LastName");
    }

    private void validateName(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }

        String trimmed = value.trim();

        if (trimmed.length() > 55) {
            throw new IllegalArgumentException(fieldName + " is too long");
        }

        if (!trimmed.matches("[\\p{L} '-]+")) {
            throw new IllegalArgumentException(fieldName + " contains invalid characters");
        }
    }
}
