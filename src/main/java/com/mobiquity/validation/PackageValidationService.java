package com.mobiquity.validation;

import com.mobiquity.packer.domain.Package;

/**
 * An interface for the service which validate our Package instance data
 */
public interface PackageValidationService {

        ValidationResult validate(Package command);

}
