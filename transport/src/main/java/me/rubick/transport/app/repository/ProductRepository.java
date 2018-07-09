package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.constants.ProductStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    public List<Product> findByIdInAndStatusIn(Collection<Long> id, Collection<ProductStatusEnum> productStatuses);


    public Product findTopByProductSkuAndUserIdAndStatus(String sku, long userId, ProductStatusEnum productStatus);

    public List<Product> findByProductSkuInAndStatus(Collection<String> collection, ProductStatusEnum productStatus);

    public Product findTopByProductSku(String productSku);
}
