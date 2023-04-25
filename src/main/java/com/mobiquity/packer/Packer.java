package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.exception.ParserException;
import com.mobiquity.packer.domain.Package;
import com.mobiquity.packer.domain.PackageItem;
import com.mobiquity.validation.DefaultPackageValidationService;
import com.mobiquity.validation.ValidationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Packer {

    private Packer() {
    }

    /**
     * Returns the maximum cost occupied by the itens in a Package.
     *
     * This method only returns the maximum cost of all itens, but we need the list of all itens choosen.
     *
     * @param capacity      Number representing the capacity of a Package
     * @param items         Itens to be fit in the Package
     * @return              The maximum cost of all itens choosen
     */
    static private int maximumCostWithinCapacity(int capacity, List<PackageItem> items) {
        //System.out.println(capacity + " "+Stream.of(items).map( i -> i.toString()).collect(Collectors.joining()));
        return items.stream().mapToInt(item -> {
            if (item != null) {
                if (item.getWeight() > capacity) {
                    return 0;
                }

                int tempCost = maximumCostWithinCapacity((int) (capacity - item.getWeight()),
                        items.subList(items.indexOf(item) + 1, items.size()));

                return (int) (item.getCost() + tempCost);
            }
            return 0;
        }).max().orElse(0);
    }

    /**
     * Finds the best fit for a given Package, considering the weights and costs of each item, and
     * maximizing the total cost of the itens choosen.
     *
     * The problem description suggests the usage of a well recognized problem known as Knapsack 0-1.
     *
     * The problem on the assignement is pretty much similar to the one we found for Knapsack, as it can be seen below:
     *
     * "Each thing you put inside the package has such parameters as index number, weight and cost.
     * The package has a weight limit. Your goal is to determine which things to put into the package so
     * that the total weight is less than or equal to the package limit and the total cost is as large as possible."
     *
     * The 0-1 knapsack problem is the following. A thief robbing a store finds n items.
     * The ith item is worth dollars and weighs wi pounds, where nd wi are integers. The thief wants to
     * take as valuable a load as possible, but he can carry at most W pounds in his knapsack, for some integer W .
     * Which items should he take? (We call this the 0-1 knapsack problem because for each item, the thief must either
     * Greedy Algorithms take it or leave it behind; he cannot take a fractional amount of an item or take an item more than once.)
     *
     * @param capacity An Integer number representing maximum capacity for a Pack
     * @param costs    An array of float numbers representing the costs of each item that can possibly fills in the Pack
     * @param weights  An array of float numbers representing the weights of each item that can possibly fills in the Pack
     * @param items    This is a Set representing the itens choosen to be the best fit in the Bag (or Package)
     * @return         The maximum cost, summing up all costs for all the itens choosen as the best fit
     */
    static float findBestFitsIntoPackage(int capacity, float costs[],
                                         float weights[], Set<Integer> items) {
        int n = weights.length;
        int i, w;
        float K[][] = new float[n + 1][capacity + 1];

        /* using dynamic programming, we iteratively fill the array above with maximum cost of each item */
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= capacity; w++) {
                /* in the beggining (weight 0) we fill the array element with 0 */
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (weights[i - 1] <= w) {
                    // we look back and see an item which fit in the given weight
                    K[i][w] = Math.max(costs[i - 1] +
                            K[i - 1][(int) Math.ceil(w - weights[i - 1])], K[i - 1][w]);
                } else {
                    K[i][w] = K[i - 1][w];
                }
            }
        }

        /* the maximum cost is found */
        float maximumCost = K[n][capacity];

        w = capacity;
        /* since we have the maximum cost, now we must find all the items which sum up to this maximum cost */
        for (i = n; i > 0 && maximumCost > 0; i--) {
            if (maximumCost == Math.ceil(K[i - 1][w]))
                continue;
            else {
                // we keep adding the item's ID in the Set passed as a parameter
                items.add(i - 1);

                /* iteratively, decreases the maximumCost and the actual weight, so in the next iteration
                 we keep trying to find all the itens */
                maximumCost = (int) Math.ceil(maximumCost - costs[i - 1]);
                w = (int) Math.ceil(w - weights[i - 1]);
            }
        }

        return maximumCost;
    }

    /**
     * Opens a file with Package and PackageItem's and parsers its contents
     *
     * @param fileName      Path to the text file with all test cases
     * @return              Returns a Map, representing the Test Case ID on its Key, and a Package object on its value
     * @throws APIException
     */
    private static Map<Integer, Package> loadFromFile(final String fileName) throws APIException {

        final Map<Integer, Package> result = new HashMap<>();

        final DefaultPackageValidationService validation = new DefaultPackageValidationService();

        /* declares an atomic Integer - this is to create an unique number to identify each test case */
        AtomicInteger ind = new AtomicInteger();
        try {

            /* Iterates over all lines from the File) - each single line represents a set of items which are supposedly able to fit in the Package */
            for (final String line : Files.readAllLines(Paths.get(fileName))) {
                if (!line.isBlank() && !line.isEmpty()) {
                    final String[] fields = line.split(":");
                    if (!fields[0].trim().isEmpty()) {
                        Integer capacity = 0;
                        try {
                            capacity = Integer.parseInt(fields[0].trim());
                        } catch (NumberFormatException exc) {
                            throw new APIException("Error parsing CAPACITY field " + fields[0].trim() + ".", exc);
                        }

                        /* We create a Package instance - which represents a Bag, in which we can fit a lot of itens (PackageItem) */
                        /* The set of itens are triples in the form (xxx,yyy,zzz) and they are separated by spaces */
                        Package pack = new Package(capacity, Stream.of(fields[1].replaceAll("\\s+$", " ").split(" "))
                                .map(m -> {
                                    if (!m.trim().isEmpty()) {
                                        try {
                                            return PackageItem.fromString(m);
                                        } catch (ParserException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    return null;
                                })
                                .collect(toList()));

                        /* At this point we validate the Package and list of PackageItem, for the given validation criterias
                           The constraints are:
                            1. Max weight that a package can take is ≤ 100
                            2. There might be up to 15 items you need to choose from
                            3. Max weight and cost of an item is ≤ 100
                        */
                        final ValidationResult validationResult = validation.validate(pack);
                        if (validationResult.notValid()) {
                            System.out.println(validationResult.getErrorMsg());
                            throw new APIException("Error parsing fields" + validationResult.getErrorMsg());
                        }

                        /* Add an item to the Map - the Key is the unique Integer created by the AtomicInteger, and the Map value is a Package instance */
                        result.put(ind.getAndIncrement(), pack);
                    }

                } // if

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new APIException("Error reading file.", e);
        }

        return result;
    }

    public static String pack(String filePath) throws APIException {
        /* a Strign representing the processing result for all test cases obtained from the File at filePath */
        String result = new String();

        /* Parsers all test cases from the File passed in the filePath */
        Map<Integer, Package> allCases = loadFromFile(filePath);

        /* Iterates over all test cases, which were extracted from the File on the path at filePath */
        for (Map.Entry<Integer, Package> caseItem : allCases.entrySet()) {
            Set<Integer> selectedItems = new HashSet<>();

            Package pack = caseItem.getValue();

            /* it is necessary to transform all weights and costs to separate arrays - this is to prepare to our matching algorithm */
            float[] costs = new float[pack.getItems().size()];
            float[] weights = new float[pack.getItems().size()];
            int ind = 0;
            for (final PackageItem p : pack.getItems()) {
                if (p != null) {
                    costs[ind] = p.getCost();
                    weights[ind] = p.getWeight();
                }
                ++ind;
            }
            /* runs the Knapsack algorithm  to identify the items (PackageItem) which best fit the Package capacity */
            float max = findBestFitsIntoPackage(pack.getCapacity(), costs, weights, selectedItems);

            /* Convert the Set with all PackageItem (best fit) to a String */
            String caseResult = Stream.of(selectedItems.toArray()).map(Object::toString).collect(Collectors.joining(","));
            result += (caseResult.trim().length() > 0 ? caseResult : "-") + "\n";
        }

        /* Return the result, removing the last character, which is a line break */
        return result.substring(0, result.length() - 1);
    }

    public static void main(String[] args) {
        try {
            System.out.println(Packer.pack("src/test/resources/example_input_violate_max_item"));
        } catch (APIException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
