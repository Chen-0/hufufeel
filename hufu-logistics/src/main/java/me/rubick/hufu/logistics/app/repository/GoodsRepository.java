package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    void deleteByFarheridAndIdNotIn(Integer waybillId, Integer[] goodsId);

}
