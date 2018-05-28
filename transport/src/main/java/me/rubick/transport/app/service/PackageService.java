package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.PackageProduct;
import me.rubick.transport.app.model.PackageStatus;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.PackageProductRepository;
import me.rubick.transport.app.repository.PackageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
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
            final int status,
            final Pageable pageable
    ) {
        return packageRepository.findAll(new Specification<Package>() {
            @Override
            public Predicate toPredicate(Root<Package> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String _keyword = getKeyword(keyword);

                Predicate predicate = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("userId"), user.getId()),
                        criteriaBuilder.equal(root.get("status"), status),
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("referenceNumber"), _keyword)
                        )
                );

                return predicate;
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
}
