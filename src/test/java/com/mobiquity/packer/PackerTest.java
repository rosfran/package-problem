package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackerTest {
    @Test
    @DisplayName("Case scenario with Package capacities of 81, 8, 75 e 56")
    public void testCaseScenario1() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        String result = Packer.pack(absolutePath);

        Path resourceOutputFile = Paths.get("src","test","resources", "example_output");

        assertNotEquals( Files.readString(resourceOutputFile), result);

        Path resourceOutputCorrectFile = Paths.get("src","test","resources", "example_output_correct");

        assertEquals( Files.readString(resourceOutputCorrectFile), result);

    }

    @Test
    @DisplayName("Case scenario with Package capacities of 31, 8, 75 e 56")
    public void testCaseScenario2() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input_2");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input_2"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        String result = Packer.pack(absolutePath);

        Path resourceOutputCorrectFile = Paths.get("src","test","resources", "example_output_2");

        assertEquals(Files.readString(resourceOutputCorrectFile), result);

    }

    @Test
    @DisplayName("Package with invalid capacity")
    public void testInvalidCaseMaxCapacity() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input_violate_max_capacity");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input_violate_max_capacity"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        assertThrows(
                APIException.class,
                () -> Packer.pack(absolutePath),
                "Expected Packer.pack(absolutePath) to throw, but it didn't"
        );

    }

    @Test
    @DisplayName("Package with invalid weight on a package item")
    public void testInvalidCaseMaxWeightItem() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input_violate_max_item_weight");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input_violate_max_item_weight"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        assertThrows(
                APIException.class,
                () -> Packer.pack(absolutePath),
                "Expected Packer.pack(absolutePath) to throw, but it didn't"
        );

    }

    @Test
    @DisplayName("Package with invalid cost on a package item")
    public void testInvalidCaseMaxCosttItem() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input_violate_max_item_cost");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input_violate_max_item_cost"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        assertThrows(
                APIException.class,
                () -> Packer.pack(absolutePath),
                "Expected Packer.pack(absolutePath) to throw, but it didn't"
        );

    }

    @Test
    @DisplayName("Package with invalid size for a list of itens - greater than 15")
    public void testInvalidCaseMaxItensSize() throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","resources", "example_input_violate_max_itens_size");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        assertTrue(absolutePath.endsWith("src/test/resources/example_input_violate_max_itens_size"));
        assertTrue(resourceDirectory.toFile().length() > 0);
        assertTrue(resourceDirectory.toFile().exists());

        assertThrows(
                APIException.class,
                () -> Packer.pack(absolutePath),
                "Expected Packer.pack(absolutePath) to throw, but it didn't"
        );

    }


}
