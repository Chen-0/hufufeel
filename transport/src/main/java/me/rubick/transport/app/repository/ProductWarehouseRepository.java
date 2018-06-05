package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {

    ProductWarehouse findTopByUserIdAndProductIdAndWarehouseId(long userId, long productId, long warehouseId);

    @Query("select pw from ProductWarehouse pw where pw.userId = ?1 and pw.quantity > 0 and pw.warehouseId = ?2")
    List<ProductWarehouse> findAvailableStockByUserId(long userId, long warehouseId);


    /**
     * 减库存
     * @param userId
     * @param productId
     * @param warehouseId
     * @param qty
     * @param weight
     * @return
     */
    @Modifying
    @Query("update ProductWarehouse pw " +
            "set pw.quantity = pw.quantity - ?4, pw.weight = pw.weight - ?5 " +
            "where pw.userId = ?1 and pw.productId = ?2 and pw.warehouseId = ?3 " +
            "and pw.quantity - ?4 >= 0")
    int reduceStore(long userId, long productId, long warehouseId, int qty, BigDecimal weight);
}
