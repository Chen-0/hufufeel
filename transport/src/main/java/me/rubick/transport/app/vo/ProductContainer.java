package me.rubick.transport.app.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ProductContainer implements Serializable {

    private Set<Long> products = new HashSet<>();


    private final static long HOUR = 60 * 60 * 1000;

    public Set<Long> getProducts() {
        return products;
    }

    public void clearAll() {
        products.clear();
    }
}
