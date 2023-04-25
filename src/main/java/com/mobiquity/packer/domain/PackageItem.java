package com.mobiquity.packer.domain;

import com.mobiquity.exception.ParserException;

import java.util.stream.Collectors;

/**
 * A PackageItem is a specific item which possibly fits in the Package
 */
public class PackageItem
{
    /* The item ID or the item order */
    private int id;

    /* The weight for this item - tp choose an elegible item to fit in the Package,
    the sum of all itens must fit into Package's capacity */
    private float weight;

    /* The cost of this item - this value is used to maximize the result for our finding */
    private float cost;

    public PackageItem(int id, float weight, float cost ) {
        this.id = id;
        this.weight = weight;
        this.cost = cost;
    }

    /**
     * Filters out only the decimal symbols - digits and dot(.)
     * @param decimalStr
     * @return
     */
    private static String retainOnlyDecimalSymbols( final String decimalStr ) {
        return decimalStr.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> Character.isDigit(c) || c == '.')
                .map( c -> String.valueOf(c))
                .collect(Collectors.joining());
    }


    /**
     * Create a PackageItem from a String representing a set of itens
     *
     * Example:
     *      (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
     *
     * @param str       A String representing a set of itens
     * @return          An instance for PackageItem
     * @throws ParserException
     */
    public static PackageItem fromString(String str ) throws ParserException {
        /* removes all parenthesis, they aren't necessary */
        str = str.replaceAll("[()]", "");

        /* split the elements in fields separated by comma - these values are: id, weight and cost */
        String[] parts = str.split(",");

        int id = 0;
        float weight = 0, cost = 0;
        try {
            id = Integer.parseInt(PackageItem.retainOnlyDecimalSymbols(parts[0].trim()));
        } catch (NumberFormatException exc) {
            throw new ParserException("Error parsing ID field "+parts[0], exc);
        }

        try {
            weight = Float.parseFloat(parts[1].trim());
        } catch (NumberFormatException exc) {
            throw new ParserException("Error parsing WEIGHT field "+parts[1], exc);
        }

        try {
            cost = Float.parseFloat(PackageItem.retainOnlyDecimalSymbols(parts[2].trim()));
        } catch (NumberFormatException exc) {
            throw new ParserException("Error parsing COST field "+parts[2], exc);
        }

        return new PackageItem( id, weight,  cost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        return this.weight;
    }

    public float getCost() {
        return this.cost;
    }

    public void setWeight( float weight ) {
        this.weight = weight;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String toString() {
        return this.id+", "+this.weight+", "+this.cost;
    }

}
