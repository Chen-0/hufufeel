package me.rubick.transport.app.service;

import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.PackageTypeEnum;
import me.rubick.transport.app.constants.ProductStatusEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageProductRepository;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class PackageService {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageProductRepository packageProductRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private WarehouseRepository warehouseRepository;


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
        return packageRepository.save(p);
    }

    public void saveLocation(Package p, String location) {
        Set<Long> set = new HashSet<>();
        for (PackageProduct pp: p.getPackageProducts()) {
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
        packageRepository.delete(id);
    }
}
