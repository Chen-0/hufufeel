package me.rubick.transport.app.vo;

import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductContainer implements Serializable {

    private Set<Long> products = new HashSet<>();

    private long lastUpdateTime = System.currentTimeMillis();

    private final static long HOUR = 60 * 60 * 1000;

    public Set<Long> getProducts() {
        if (ObjectUtils.isEmpty(lastUpdateTime)) {
            return products;
        }

        long yy = System.currentTimeMillis() - this.lastUpdateTime;

        if (yy >= HOUR) {
            products = new HashSet<>();
        }

        return products;
    }

    public void setProducts(Set<Long> products) {
        this.products = products;

        this.lastUpdateTime = System.currentTimeMillis();
    }
}
