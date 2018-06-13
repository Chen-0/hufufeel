package me.rubick.transport.app.service;

import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageProductRepository;
import me.rubick.transport.app.repository.PackageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PackageService {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageProductRepository packageProductRepository;


    public Page<Package> searchPackage(
            final String keyword,
            final User user,
            final Integer status,
            final Pageable pageable
    ) {
        return packageRepository.findAll(new Specification<Package>() {
            @Override
            public Predicate toPredicate(Root<Package> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.hasText(keyword)) {
                    String _keyword = getKeyword(keyword);
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(root.get("referenceNumber"), _keyword)
                    ));
                }

                if (!ObjectUtils.isEmpty(user)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), user.getId()));
                }

                if (!ObjectUtils.isEmpty(status)) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        }, pageable);
    }

    private String getKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    public void store(Package p, List<PackageProduct> products) {
        p = packageRepository.save(p);

        for (PackageProduct packageProduct: products) {
            packageProduct.setPackageId(p.getId());
        }

        packageProductRepository.save(products);
    }

    public Package inbound(long packageId, List<Product> products, List<Integer> qty) {
        int count = products.size();
        int t = 0;
        for (int i = 0; i < count; i ++) {
            packageProductRepository.inbound(
                    packageId,
                    products.get(i).getId(),
                    qty.get(i));

            t += qty.get(i);
        }

        Package p = packageRepository.findOne(packageId);
        p.setQuantity(t);
        p.setStatus(PackageStatus.RECEIVED);
        return packageRepository.save(p);
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
}
