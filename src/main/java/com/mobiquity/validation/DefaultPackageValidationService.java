package com.mobiquity.validation;

import com.mobiquity.packer.domain.Package;

/**
 * Our actual implementation for the 3 Validation rules.
 *
 * This class is an implementation for a design pattern called Chain of Responsibility
 * Also known as: CoR, Chain of Command.
 * Chain of Responsibility is a behavioral design pattern that lets you pass requests along a chain of handlers.
 * Upon receiving a request, each handler decides either to process the request or to pass it to the next handler in the chain.
 *
 * Each chain is a Validation as described below:
 *
 * 1. Max weight that a package can take is ≤ 100
 * 2. There might be up to 15 items you need to choose from
 * 3. Max weight and cost of an item is ≤ 100
 **/
public class DefaultPackageValidationService implements PackageValidationService {

    /* Maximum capacity for a Package */
    private static final int MAX_CAPACITY = 100;
    /* Maximum number of itens in a Package */
    private static final int MAX_ITENS = 15;
    /* Maximum weight for an item */
    private static final int MAX_WEIGHT = 100;

    /* Maximum cost for an item */
    private static final int MAX_COST = 100;

    /**
     * This method applies all 3 validation rules, creating dependencies between them using the linkWith function
     * @param command   This is the instance of the Package being analyzed for any constraint violation.
     * @return          The result of processing a given Input. It says if the validation was successfull or not.
     */
    @Override
    public ValidationResult validate(Package command) {
        return new MaxWeightValidationStep()
                .linkWith(new MaxWeightCostItemValidationStep())
                .linkWith(new MaxItemsValidationStep())
                .validate(command);
    }

    /**
     *  Validation that applies the constraint: Max weight that a package can take is ≤ 100
     */
    private static class MaxWeightValidationStep extends ValidationStep<Package> {

        @Override
        public ValidationResult validate(Package command) {

            if (command.getCapacity() > MAX_CAPACITY) {
                return ValidationResult.invalid(String.format("Maximum weight for a package is [%s] but got [%s].", MAX_CAPACITY, command.getCapacity()));
            }

            return checkNext(command);
        }
    }

    /**
     * Validation that applies the constraint: There might be up to 15 items you need to choose from
     */
    private static class MaxItemsValidationStep extends ValidationStep<Package> {

        public ValidationResult validate(Package command) {
            if (command.getItems().size() > MAX_ITENS) {
                return ValidationResult.invalid(String.format("Max itens size is [%s], but got [%s]", MAX_ITENS, command.getItems().size()));
            }
            return checkNext(command);
        }
    }

    /**
     * Validation applying the constraint: Max weight and cost of an item is ≤ 100
     */
    private static class MaxWeightCostItemValidationStep extends ValidationStep<Package> {

        @Override
        public ValidationResult validate(Package command) {
            if (command != null && command.getItems().stream().anyMatch(m -> m!= null && ( m.getCost() > MAX_COST || m.getWeight() > MAX_WEIGHT))) {
                return ValidationResult.invalid(String.format("Maximum cost is [%s] and maximum weight is [%s] " +
                        "for each item in the package.", MAX_COST, MAX_WEIGHT));
            }
            return checkNext(command);
        }
    }
}

