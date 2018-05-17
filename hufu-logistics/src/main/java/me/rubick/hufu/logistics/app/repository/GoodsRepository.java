package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    void deleteByFarheridAndIdNotIn(Integer waybillId, Integer[] goodsId);

}
