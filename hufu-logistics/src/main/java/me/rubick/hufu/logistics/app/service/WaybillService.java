package me.rubick.hufu.logistics.app.service;

import me.rubick.hufu.logistics.app.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.Goods;
import me.rubick.hufu.logistics.app.model.Waybill;
import me.rubick.hufu.logistics.app.repository.BrandRepository;
import me.rubick.hufu.logistics.app.repository.GoodsRepository;
import me.rubick.hufu.logistics.app.repository.WaybillRepository;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class WaybillService {

    @Resource
    private WaybillRepository waybillRepository;

    @Resource
    private BrandRepository brandRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private ExcelHelper excelHelper;

    public Page<Waybill> getWaybill(Integer status, String keyword, Pageable pageable) {
        return waybillRepository.getWaybill(status, keyword, pageable);
    }

    public Double getTotalAtMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return waybillRepository.getTotalAtMonth(month);
    }

    public List<Brand> getAllBrand() {
        return brandRepository.findByfatherid(0);
    }

    public Waybill save(Waybill waybill) {
        return waybillRepository.save(waybill);
    }

    public Goods saveGoods(Goods goods) {
        return goodsRepository.save(goods);
    }

    public Waybill get(Integer id) {
        return waybillRepository.findOne(id);
    }

    public void destroy(Integer id) {
        waybillRepository.delete(id);
    }

    public void destroy(Integer waybillId, Integer[] goodsId) {
        goodsRepository.deleteByFarheridAndIdNotIn(waybillId, goodsId);
    }

    public List<Waybill> findByTrackingnumberIn(String[] trackingNumber) {
        return waybillRepository.findByTrackingnumberIn(trackingNumber);
    }

    public void importByExcel(MultipartFile file) throws BaseException {
        List<Waybill> waybills = excelHelper.readWaybill(file);

        for (Waybill waybill : waybills) {
            Waybill temp = waybillRepository.findByTrackingnumber(waybill.getTrackingNumber());
            if(temp.getLogistics_status() != null && temp.getLogistics_status().length() > 0) {
                temp.setLogistics_status(temp.getLogistics_status() + "\n" + waybill.getLogistics_status());
            } else {
                temp.setLogistics_status(waybill.getLogistics_status());
            }
            waybillRepository.save(temp);
        }
    }

    public Goods findGoodsById(Integer id) {
        return goodsRepository.findOne(id);
    }

    public void destroy(List<Waybill> waybills) {
        waybillRepository.delete(waybills);
    }
}
