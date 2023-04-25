package com.mobiquity.validation;


/**
 *
 * Stores a result after validating a given input, and says if the Validation was successful (isValid equals to TRUE)
 *
 */
public  class ValidationResult {
    /* True, if the validation returns a valid entry set*/
    private final boolean isValid;

    /* The errorMsg with the details of this Validation */
    private final String errorMsg;

    protected ValidationResult(boolean isValid, String errorMsg) {
        this.isValid = isValid;
        this.errorMsg = errorMsg;
    }

    /**
     * Creates a valid return for a Validation
     * @return   The ValidationReturn instance.
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    /**
     * Creates an invalid return for a Validation
     * @return   The ValidationReturn instance.
     */
    public static ValidationResult invalid(String errorMsg) {
        return new ValidationResult(false, errorMsg);
    }


    public boolean notValid() {
        return !isValid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
