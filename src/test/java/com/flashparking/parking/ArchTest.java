package com.flashparking.parking;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.flashparking.parking");

        noClasses()
            .that()
                .resideInAnyPackage("com.flashparking.parking.service..")
            .or()
                .resideInAnyPackage("com.flashparking.parking.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.flashparking.parking.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
