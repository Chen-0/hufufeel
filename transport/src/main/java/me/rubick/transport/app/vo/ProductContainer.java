package me.rubick.transport.app.vo;

import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
