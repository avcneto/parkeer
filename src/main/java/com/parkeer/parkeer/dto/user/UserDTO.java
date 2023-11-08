package com.parkeer.parkeer.dto.user;

import jakarta.validation.constraints.*;

public record UserDTO(
        @Pattern(regexp = NO_NUMERIC_REGEX, message = NAME_NON_NUMERIC_MESSAGE)
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String name,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String address,
        @NotNull(message = FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE)
        @PositiveOrZero(message = FIELD_ONLY_NUMBER_MESSAGE)
        String addressNumber,
        @NotNull(message = FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE)
        @Pattern(regexp = ZIP_CODE_REGEX, message = ZIP_CODE_INVALID_MESSAGE)
        String zipCode,
        @NotNull(message = FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE)
        @Pattern(regexp = PHONE_NUMBER_REGEX)
        String phone,
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        @Pattern(regexp = NUMBER_REGEX, message = FIELD_ONLY_NUMBER_MESSAGE)
        @PositiveOrZero(message = FIELD_ONLY_NUMBER_MESSAGE)
        //@CPF(message = CPF_INVALID_MESSAGE) - IMPLEMENTAR HIBERNATE
        String cpf,
        @Email(message = EMAIL_MESSAGE)
        @NotBlank(message = FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE)
        String email,
        @Size(min = PASSWORD_MIN_SIZE, message = PASSWORD_SIZE_MESSAGE)
        @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
        String password
) {
    private static final String NO_NUMERIC_REGEX = "[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ\\s]+$";
    private static final String NAME_NON_NUMERIC_MESSAGE = "Name must not contain number and cannot be empty";
    private static final String FIELD_CANNOT_BE_NULL_EMPTY_BLANK_MESSAGE = "Field cannot be null, empty or blank";
    private static final String FIELD_CANNOT_BE_NULL_OR_EMPTY_MESSAGE = "Field cannot be null or empty";
    private static final String FIELD_ONLY_NUMBER_MESSAGE = "Invalid field, enter only positive numbers";
    private static final String ZIP_CODE_REGEX = "\\d{5}(-\\d{3})?";
    private static final String ZIP_CODE_INVALID_MESSAGE = "Zipcode is invalid, example: 00000-000";
    private static final String PHONE_NUMBER_REGEX = "(^$|[0-9]{10})";
    private static final String NUMBER_REGEX = "^[0-9]*$";
    private static final String EMAIL_MESSAGE = "Email is invalid, Please use a valid email";
    private static final int PASSWORD_MIN_SIZE = 6;
    private static final String PASSWORD_SIZE_MESSAGE = "The password must be in the minimum 6 characters";
    private static final String PASSWORD_REGEX = "[A-Za-z0-9]+";
    private static final String PASSWORD_MESSAGE = "Password must contain letters and numbers";
}
