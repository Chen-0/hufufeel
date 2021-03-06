package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.ProductWarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class}, isolation = Isolation.SERIALIZABLE)
@Slf4j
public class StockService {

    @Resource
    private ProductWarehouseRepository productWarehouseRepository;

    @Resource
    private PackageRepository packageRepository;

    @Transactional(readOnly = true)
    public List<ProductWarehouse> findAll(User user, Collection<Long> collection) {
        return productWarehouseRepository.findByUserIdAndIdIn(user.getId(), collection);
    }

    @Transactional(readOnly = true)
    public ProductWarehouse findAvailableStockByProductSku(String productSku, User user, long warehouseId) {
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findAvailableStockByProductSku(productSku, user.getId(), warehouseId);

        if (ObjectUtils.isEmpty(productWarehouses)) {
            return null;
        }

        return productWarehouses.get(0);
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
                if (!ObjectUtils.isEmpty(user)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("userId"), user.getId())));
                }

                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("quantity"), 0)));

                if (!ObjectUtils.isEmpty(keyword)) {
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
        p.setStatus(PackageStatusEnum.FINISH);
        packageRepository.save(p);
        List<PackageProduct> products = p.getPackageProducts();

        for (PackageProduct pp : products) {
            addStock(
                    p.getUserId(),
                    pp.getProductId(),
                    p.getWarehouseId(),
                    pp.getQuantity()
            );
        }
    }

    public void addStock(long userId, long productId, long warehouseId, int qty) {
        ProductWarehouse productWarehouse = findOrNewProductWarehouse(
                userId,
                productId,
                warehouseId
        );

        productWarehouse.setQuantity(productWarehouse.getQuantity() + qty);

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

            return productWarehouseRepository.save(p);
        }

        return productWarehouse;
    }

    public boolean reduceStore(User user, Product product, Warehouse warehouse, int qty) {
        log.info("{}, {}", JSONMapper.toJSON(product.getProductName()), qty);
        int raw = productWarehouseRepository.reduceStore(
                user.getId(),
                product.getId(),
                warehouse.getId(),
                qty
        );
        log.info("减库存成功，返回值：{}", raw);

        return raw > 0;
    }

    public boolean checkStore(User user, Product product, Warehouse warehouse, int qty) {
        int raw = productWarehouseRepository.checkStore(
                user.getId(),
                product.getId(),
                warehouse.getId(),
                qty);
        log.info("检查库存成功，返回值：{}", raw);
        return raw > 0;
    }
}
