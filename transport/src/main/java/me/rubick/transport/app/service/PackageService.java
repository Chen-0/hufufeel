package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.PackageTypeEnum;
import me.rubick.transport.app.constants.ProductStatusEnum;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
@Slf4j
public class PackageService {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageProductRepository packageProductRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private UserRepository userRepository;


    public Page<Package> searchPackage(
            final String keyword,
            final User user,
            final Integer status,
            final int type,
            final Pageable pageable
    ) {
        return packageRepository.findAll(new Specification<Package>() {
            @Override
            public Predicate toPredicate(Root<Package> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.hasText(keyword)) {
                    String _keyword = getKeyword(keyword);
                    Join<Package, User> joinU = root.join("user", JoinType.INNER);
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(root.get("cn"), _keyword),
                            criteriaBuilder.like(root.get("referenceNumber"), _keyword),
                            criteriaBuilder.like(root.get("searchNo"), _keyword),
                            criteriaBuilder.like(joinU.get("hwcSn"), _keyword),
                            criteriaBuilder.like(joinU.get("name"), _keyword),
                            criteriaBuilder.like(joinU.get("username"), _keyword)

                    ));
                }

                if (type > -1) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                if (!ObjectUtils.isEmpty(user)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), user.getId()));
                }

                if (!ObjectUtils.isEmpty(status)) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                predicates.add(criteriaBuilder.equal(root.get("isDelete"), false));

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        }, pageable);
    }

    private String getKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    public void store(Package p, List<PackageProduct> products) {
        p = packageRepository.save(p);

        for (PackageProduct packageProduct : products) {
            packageProduct.setPackageId(p.getId());
        }

        packageProductRepository.save(products);
    }

    public Package inbound(long packageId) {
        Package p = packageRepository.findOne(packageId);
        p.setStatus(PackageStatusEnum.RECEIVED);
        return packageRepository.save(p);
    }

    public Package inbound(long packageId, List<Product> products, List<Integer> qty) {
        int count = products.size();
        int t = 0;
        for (int i = 0; i < count; i++) {
            packageProductRepository.inbound(
                    packageId,
                    products.get(i).getId(),
                    qty.get(i));

            t += qty.get(i);
        }

        Package p = packageRepository.findOne(packageId);
        p.setQuantity(t);
        p.setExpectQuantity(t);
        return packageRepository.save(p);
    }

//    public void inboundReject(Package p, List<Product> products, List<Integer> qty) {
//        int count = products.size();
//        for (int i = 0; i < count; i++) {
//            packageProductRepository.inboundReject(
//                    p.getId(),
//                    products.get(i).getId(),
//                    qty.get(i));
//        }
//
//
//
//        //copy the package info
//
//        //to Map
//        Map<Long, Integer> map = new HashMap<>();
//
//        for (int i = 0; i < count; i++) {
//            //check the qty
//            if (qty.get(i) > 0) {
//                map.put(products.get(i).getId(), qty.get(i));
//            }
//        }
//
//        Package newP = new Package();
//        BeanMapperUtils.copy(p, newP);
//        newP.setId(0);
//        newP.setQuantity(0);
//        newP.setExpectQuantity(0);
//        newP.setCreatedAt(new Date());
//        newP.setUpdatedAt(new Date());
//        newP.setComment("从退货单：" + p.getCn() + "中生成子退货单");
//        newP.setSn(this.generateBatch());
//        newP.setPid(p.getId());
//        newP.setCn(generateCN(userRepository.findOne(p.getUserId())));
//
//        newP = packageRepository.save(newP);
//
//        List<PackageProduct> packageProducts = new ArrayList<>();
//        for (Map.Entry<Long, Integer> entry: map.entrySet()) {
//            PackageProduct pp = new PackageProduct();
//            pp.setPackageId(newP.getId());
//            pp.setProductId(entry.getKey());
//            pp.setExpectQuantity(entry.getValue());
//            pp.setQuantity(entry.getValue());
//            packageProducts.add(pp);
//        }
//
//        packageProductRepository.save(packageProducts);
//
//        inbound(newP.getId());
//    }

    /**
     * 退货入库
     *
     * @param p
     * @param products
     * @param qty
     */
    public void inboundReject(Package p, List<Product> products, List<Integer> qty) {
        List<PackageProduct> existPPs = p.getPackageProducts();
        List<PackageProduct> pps = new ArrayList<>();
        int count = products.size();

        //to Map
        Map<Long, Integer> map = new HashMap<>();

        for (int i = 0; i < count; i++) {
            //check the qty
            if (qty.get(i) > 0) {
                map.put(products.get(i).getId(), qty.get(i));
            }
        }

        boolean flag = false;
        Set<Long> dlist = new HashSet<>();

        for (PackageProduct pp : existPPs) {
            int tc = map.get(pp.getProductId());
            if (tc < pp.getExpectQuantity()) {
                flag = true;
            }
        }

        for (PackageProduct pp : existPPs) {
            int tc = map.get(pp.getProductId());

//            if (tc < pp.getExpectQuantity()) {
//                //计算剩余预计
//                pp.setExpectQuantity(pp.getExpectQuantity() - tc);
//
//                //生成新的入库货品单
//                PackageProduct npp = new PackageProduct();
//                npp.setExpectQuantity(tc);
//                npp.setQuantity(tc);
//                npp.setProductId(pp.getProductId());
//                pps.add(npp);
//            } else {
//                pp.setQuantity(tc);
//            }

            if (! flag) {
                pp.setQuantity(tc);
            } else {
                pp.setExpectQuantity(pp.getExpectQuantity() - tc);

                //生成新的入库货品单
                PackageProduct npp = new PackageProduct();
                npp.setExpectQuantity(tc);
                npp.setQuantity(tc);
                npp.setProductId(pp.getProductId());
                pps.add(npp);

                if (pp.getExpectQuantity() - tc <= 0) {
                    dlist.add(pp.getId());
                }
            }
        }

        packageProductRepository.save(existPPs);

        if (dlist.size() > 0) {
            packageProductRepository.deleteById(dlist);
        }

        //判断是否需要拆单
        if (!flag) {
            return;
        }

        log.info("需要拆单：{}", JSONMapper.toJSON(pps));

        Package newP = new Package();
        BeanMapperUtils.copy(p, newP);
        newP.setId(0);
        newP.setQuantity(0);
        newP.setExpectQuantity(0);
        newP.setCreatedAt(new Date());
        newP.setUpdatedAt(new Date());
        newP.setComment("从退货单：" + p.getCn() + "中生成子退货单");
        newP.setSn(this.generateBatch());
        newP.setPid(p.getId());
        newP.setCn(generateCN(userRepository.findOne(p.getUserId())));

        newP = packageRepository.save(newP);

        for (PackageProduct pp: pps) {
            pp.setPackageId(newP.getId());
        }

        packageProductRepository.save(pps);

        inbound(newP.getId());
    }

    public void saveLocation(Package p, String location) {
        Set<Long> set = new HashSet<>();
        for (PackageProduct pp : p.getPackageProducts()) {
            set.add(pp.getProductId());
        }

        productRepository.updateLocation(set, location);
    }

    public String generateBatch() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        String batch = packageRepository.getMaxSN(dateString);

        if (batch == null) {
            return "RK" + dateString + "0001";
        } else {
            String temp = batch.substring(0, 8);
            Integer no = Integer.valueOf(batch.substring(8)) + 1;
            return temp + no;
        }
    }

    public String generateCN(User user) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        String batch = packageRepository.getMaxCN("HUFU" + user.getHwcSn() + dateString);

        if (batch == null) {
            return "HUFU" + user.getHwcSn() + dateString + "0001";
        } else {
            String temp = batch.substring(0, 13);
            Integer no = Integer.valueOf(batch.substring(13)) + 1;
            return temp + no;
        }
    }

    public void create(User user, Warehouse warehouse, String referenceNumber, String contact, String comment, List<Integer> qtys, List<Long> pids, PackageTypeEnum type, String searchNo) {
        Package p = new Package();
        p.setUserId(user.getId());
        p.setWarehouseId(warehouse.getId());
        p.setStatus(PackageStatusEnum.READY);
        p.setReferenceNumber(referenceNumber);
        p.setSearchNo(searchNo);

        if (type.equals(PackageTypeEnum.REJECT)) {
            p.setContact(user.getHwcSn() + "-" + contact);
        } else {
            p.setContact("");
        }

        p.setWarehouseName(warehouse.getName());
        p.setNickname(user.getName());
        p.setComment(comment);
        p.setCn(generateCN(user));
        p.setType(type);

        List<PackageProduct> packageProducts = new ArrayList<>(qtys.size());

        int _qty = 0;

        for (int i = 0; i < pids.size(); i++) {
            PackageProduct packageProduct = new PackageProduct();
            packageProduct.setProductId(pids.get(i));
            packageProduct.setExpectQuantity(qtys.get(i));
            packageProduct.setQuantity(0);
            packageProducts.add(packageProduct);
            _qty += packageProduct.getExpectQuantity();
        }

        p.setExpectQuantity(_qty);
        p.setSn(this.generateBatch());
        this.store(p, packageProducts);
    }

    public void create(User user, Warehouse warehouse, List<Integer> qtys, List<String> skus, String contact, String comment) throws BusinessException {
        List<Long> pids = new ArrayList<>();

        for (String s : skus) {
            Product product = productRepository.findTopByProductSkuAndUserIdAndStatus(s, user.getId(), ProductStatusEnum.READY_CHECK);
            if (ObjectUtils.isEmpty(product)) {
                throw new BusinessException("货品SKU: " + s + " 不存在，请检查");
            }
            pids.add(product.getId());
        }

        create(user, warehouse, contact, "", comment, qtys, pids, PackageTypeEnum.NORMAL, "");
    }

    public List<Warehouse> findAllWarehouse() {
        return warehouseRepository.findAllByVisible(true);
    }

    public Package findOne(long id) {
        return packageRepository.findOne(id);
    }

    public void deletePackage(long id) {
        Package p = packageRepository.findOne(id);

        if (ObjectUtils.isEmpty(p)) {
            return;
        }

        p.setIsDelete(true);
        packageRepository.save(p);
    }
}
