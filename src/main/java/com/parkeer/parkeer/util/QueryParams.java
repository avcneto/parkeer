package com.parkeer.parkeer.util;

import com.parkeer.parkeer.exception.BadRequestException;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static com.parkeer.parkeer.util.Validators.isNullOrBlank;
import static java.lang.String.format;

public record QueryParams(
        MultiValueMap<String, String> params
) {

    private static final String EMPTY_OR_BLANK = "%s and %s and %s is empty or blank";
    private static final String CPF = "cpf";
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String PLATE = "plate";
    private static final String INVALID_ID = "invalid id";
    private static final String ID_IS_NOT_A_NUMBER = "id is not a number";

    public String getCpf() {
        return params.getFirst(CPF);
    }

    public String getEmail() {
        return params.getFirst(EMAIL);
    }

    public Long getUserId() {
        var userId = Optional.ofNullable(params.getFirst(USER_ID)).orElse("");

        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public String getPlate() {
        return params.getFirst(PLATE);
    }

    public Long getIdOrNull() {
        var id = Optional.ofNullable(params.getFirst(ID)).orElse("");

        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Long getIdOrBadRequestException() {
        var id = Optional.ofNullable(params.getFirst(ID))
                .orElseThrow(() -> new BadRequestException(INVALID_ID));

        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new BadRequestException(ID_IS_NOT_A_NUMBER);
        }
    }

    public void validateIdAndUserIdAndPlate() {
        if (getIdOrNull() == null && getUserId() == null && isNullOrBlank(getPlate())) {
            throw new BadRequestException(format(EMPTY_OR_BLANK, CPF, EMAIL, ID));
        }
    }

    public void validateIdAndCpfAndEmail() {
        if (getIdOrNull() == null && isNullOrBlank(getCpf()) && isNullOrBlank(getEmail())) {
            throw new BadRequestException(format(EMPTY_OR_BLANK, CPF, EMAIL, ID));
        }
    }

}
