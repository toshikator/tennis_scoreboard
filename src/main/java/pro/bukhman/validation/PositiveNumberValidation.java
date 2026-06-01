package pro.bukhman.validation;

public class PositiveNumberValidation {
    public void validate(String value) {
        try {
            long l = Long.parseLong(value);
            if (l < 0) {
                throw new IllegalArgumentException("Value must be positive");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Value must be a number");
        }
    }
}
