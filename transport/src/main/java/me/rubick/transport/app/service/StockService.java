package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.ProductWarehouseRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class}, isolation = Isolation.SERIALIZABLE)
@Slf4j
public class StockService {

    @Resource
    private ProductWarehouseRepository productWarehouseRepository;

    @Transactional(readOnly = true)
    public List<ProductWarehouse> findAll(User user, Collection<Long> collection) {
        return productWarehouseRepository.findByUserIdAndIdIn(user.getId(), collection);
    }

    @Transactional(readOnly = true)
    public Page<ProductWarehouse> findAvailableStockByUser(
            User user, Pageable pageable,
            String keyword,
            List<Long> wIds
    ) {

        return productWarehouseRepository.findAll(new Specification<ProductWarehouse>() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("userId"), user.getId())));
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("quantity"), 0)));

                if (! ObjectUtils.isEmpty(keyword)) {
                    Join<ProductWarehouse, Product> joinP = root.join("product", JoinType.INNER);
                    Join<ProductWarehouse, Product> joinW = root.join("warehouse", JoinType.INNER);
                    predicates.add(criteriaBuilder.and(criteriaBuilder.or(
                            criteriaBuilder.like(joinP.get("productName"), getKeyword(keyword)),
                            criteriaBuilder.like(joinP.get("productSku"), getKeyword(keyword)),
                            criteriaBuilder.like(joinW.get("name"), getKeyword(keyword))

                    )));
                }

                if (!ObjectUtils.isEmpty(wIds) && wIds.size() > 0) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("warehouseId")).value(wIds)));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        }, pageable);
    }

    private String getKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    @Transactional(readOnly = true)
    public List<ProductWarehouse> findAvailableStockByUser(User user, Warehouse warehouse) {
        return productWarehouseRepository.findAvailableStockByUserId(user.getId(), warehouse.getId());
    }

    public void addStock(Package p) {
        List<PackageProduct> products = p.getPackageProducts();

        for (PackageProduct pp : products) {
            addStock(p.getUserId(), pp.getProductId(), p.getWarehouseId(), pp.getRealQty(), pp.getRealWeight());
        }
    }

    private void addStock(long userId, long productId, long warehouseId, int qty, BigDecimal weight) {
        ProductWarehouse productWarehouse = findOrNewProductWarehouse(
                userId,
                productId,
                warehouseId
        );

        productWarehouse.setQuantity(productWarehouse.getQuantity() + qty);
        productWarehouse.setWeight(productWarehouse.getWeight().add(weight));
        productWarehouseRepository.save(productWarehouse);
    }

    private ProductWarehouse findOrNewProductWarehouse(long userId, long productId, long warehouseId) {
        ProductWarehouse productWarehouse = productWarehouseRepository.findTopByUserIdAndProductIdAndWarehouseId(userId, productId, warehouseId);

        if (ObjectUtils.isEmpty(productWarehouse)) {
            ProductWarehouse p = new ProductWarehouse();
            p.setProductId(productId);
            p.setUserId(userId);
            p.setWarehouseId(warehouseId);
            p.setQuantity(0);
            p.setWeight(new BigDecimal(0));

            return productWarehouseRepository.save(p);
        }

        return productWarehouse;
    }

    public void reduceStore(User user, Product product, Warehouse warehouse, int qty, BigDecimal weight) {
        int raw = productWarehouseRepository.reduceStore(
                user.getId(),
                product.getId(),
                warehouse.getId(),
                qty,
                weight
        );
        log.info("减库存成功，返回值：{}", raw);
    }
}
