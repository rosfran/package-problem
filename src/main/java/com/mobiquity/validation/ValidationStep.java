package com.mobiquity.validation;

/**
 * Each Validation can be a sequence of Steps - each step promotes a different type of constraint over the Class on <T>
 * @param <T>       The Classe which is used to be validated
 */
public abstract class ValidationStep<T> {

    private ValidationStep<T> next;

    public ValidationStep<T> linkWith(ValidationStep<T> next) {
        if (this.next == null) {
            this.next = next;
            return this;
        }
        ValidationStep<T> lastStep = this.next;
        while (lastStep.next != null) {
            lastStep = lastStep.next;
        }
        lastStep.next = next;
        return this;
    }

    public abstract ValidationResult validate(T toValidate);

    protected ValidationResult checkNext(T toValidate) {
        if (next == null) {
            return ValidationResult.valid();
        }

        return next.validate(toValidate);
    }
}