package pro.bukhman.validation;

import pro.bukhman.exception.InvalidUUIDException;

import java.util.UUID;

public class UuidValidator {
    public void validate(String uuid) {
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            throw new InvalidUUIDException(uuid, e);
        }
    }
}
