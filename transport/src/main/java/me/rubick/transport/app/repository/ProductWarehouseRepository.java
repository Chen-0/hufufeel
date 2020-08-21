package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.ProductWarehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long>, JpaSpecificationExecutor<ProductWarehouse> {

    ProductWarehouse findTopByUserIdAndProductIdAndWarehouseId(long userId, long productId, long warehouseId);

    List<ProductWarehouse> findByUserIdAndIdIn(long userId, Collection<Long> collection);

    @Query("select pw from ProductWarehouse pw where pw.userId = ?1 and pw.quantity > 0 and pw.warehouseId = ?2")
    List<ProductWarehouse> findAvailableStockByUserId(long userId, long warehouseId);

    @Query("select pw from ProductWarehouse pw where pw.userId = ?1 and pw.quantity > 0")
    Page<ProductWarehouse> findAvailableStockByUserId(long userId, Pageable pageable);

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
            "set pw.quantity = pw.quantity - ?4 " +
            "where pw.userId = ?1 and pw.productId = ?2 and pw.warehouseId = ?3 " +
            "and pw.quantity - ?4 >= 0")
    int reduceStore(long userId, long productId, long warehouseId, int qty);

    @Query("select count(pw) from ProductWarehouse pw " +
            "where pw.userId = ?1 and pw.productId = ?2 and pw.warehouseId = ?3 " +
            "and pw.quantity - ?4 >= 0")
    int checkStore(long userId, long productId, long warehouseId, int qty);

    @Query("select pw from ProductWarehouse pw, Product p where pw.productId = p.id and p.productSku = ?1 and pw.userId=?2 and pw.warehouseId = ?3")
    List<ProductWarehouse> findAvailableStockByProductSku(String productSku, long userId, long warehouseId);
}
