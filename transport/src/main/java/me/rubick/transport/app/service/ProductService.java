package me.rubick.transport.app.service;

import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.ProductStatus;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Transactional
@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserService userService;

    public void createProduct(Product product) {
        product.setStatus(ProductStatus.TO_CHECK);
        product.setUserId(userService.getByLogin().getId());
        productRepository.save(product);
    }

    public Page<Product> findProduct(String keyword, int status, Pageable pageable) {
        User user = userService.getByLogin();
        Page<Product> products = productRepository.findAll(new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String _keyword = getKeyword(keyword);
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("userId"), user.getId()),
                        criteriaBuilder.equal(root.get("status"), status),
                        criteriaBuilder.equal(root.get("isDeleted"), 0),
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("productName"), _keyword),
                                criteriaBuilder.like(root.get("productSku"), _keyword),
                                criteriaBuilder.like(criteriaBuilder.concat(root.get("id"), ""), _keyword)
                        )
                );
            }
        }, pageable);

        return products;
    }

    private String getKeyword(String _keyword) {
        return "%" + _keyword + "%";
    }

    public void deleteProduct(long id) throws BusinessException {
        Product product = productRepository.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            throw new BusinessException("[A02]目标不存在");
        }

        User user = userService.getByLogin();

        if (product.getUserId() != user.getId()) {
            throw new BusinessException("[A01]无权限操作该订单");
        }

        product.setIsDeleted(true);
        productRepository.save(product);
    }

    public List<Product> findProducts(Collection<Long> collection) {
        return productRepository.findByIdIn(collection);
    }
}
