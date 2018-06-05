package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.ProductWarehouse;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.model.Warehouse;
import me.rubick.transport.app.repository.ProductWarehouseRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class}, isolation = Isolation.SERIALIZABLE)
@Slf4j
public class StockService {

    @Resource
    private ProductWarehouseRepository productWarehouseRepository;

    @Transactional(readOnly = true)
    public List<ProductWarehouse> findAvailableStockByUser(User user, Warehouse warehouse) {
        return productWarehouseRepository.findAvailableStockByUserId(user.getId(), warehouse.getId());
    }

    public void addStock(User user, Product product, Warehouse warehouse, int qty, BigDecimal weight) {
        ProductWarehouse productWarehouse = findOrNewProductWarehouse(
                user.getId(),
                product.getId(),
                warehouse.getId()
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
