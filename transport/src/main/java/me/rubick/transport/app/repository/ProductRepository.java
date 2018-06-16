package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    public List<Product> findByIdInAndStatusIn(Collection<Long> id, Collection<ProductStatus> productStatuses);


    public Product findTopByProductSkuAndStatus(String sku, ProductStatus productStatus);
}
