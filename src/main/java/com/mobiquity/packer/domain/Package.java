package com.mobiquity.packer.domain;


import java.util.ArrayList;
import java.util.List;

/**
 * A Package represent a Bag - it contains a capacity, and a list of itens
 */
public class Package
{

    /* Maximum capacity for this Package */
    private int capacity;

    /* List of itens for this Package */
    private List<PackageItem> items;

    public Package( int capacity, List<PackageItem> items ) {
        this.capacity = capacity;
        this.items = new ArrayList<>(items);
    }

    public List<PackageItem> getItems() {
        return this.items;
    }

    public void setItems( List<PackageItem> items ) {
        this.items = items;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity( int capacity ) {
        this.capacity = capacity;
    }

}
