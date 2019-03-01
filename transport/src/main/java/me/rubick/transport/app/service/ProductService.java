package me.rubick.transport.app.service;

import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.constants.ProductTypeEnum;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.constants.ProductStatusEnum;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserService userService;

    public void createProduct(Product product) {
        User user = userService.getByLogin();
        product.setVol(product.getLength().multiply(product.getHeight().multiply(product.getWidth())).divide(new BigDecimal(1000000), 12, ROUND_HALF_DOWN));
        product.setStatus(ProductStatusEnum.TO_CHECK);
        product.setUserId(user.getId());
        product.setType(ProductTypeEnum.NORMAL);
        if (!product.getProductSku().startsWith(user.getHwcSn() + "-")) {
            product.setProductSku(user.getHwcSn() + "-" + product.getProductSku());
        }
        productRepository.save(product);
    }

    public void createRejectProduct(Product product) {
        User user = userService.getByLogin();
        product.setVol(product.getLength().multiply(product.getHeight().multiply(product.getWidth())).divide(new BigDecimal(1000000), 12, ROUND_HALF_DOWN));
        product.setStatus(ProductStatusEnum.READY_CHECK);
        product.setUserId(user.getId());
        product.setType(ProductTypeEnum.REJECT);
        productRepository.save(product);
    }

    public Page<Product> findProduct(User user, String keyword, Integer status, int type, Pageable pageable) {
        Page<Product> products = productRepository.findAll(new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String _keyword = getKeyword(keyword);

                ArrayList<Predicate> predicates = new ArrayList<>(10);

                if (!ObjectUtils.isEmpty(user)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("userId"), user.getId())));
                }

                if (!ObjectUtils.isEmpty(status)) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                if (type > -1) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));

                if (StringUtils.hasText(keyword)) {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(root.get("productName"), _keyword),
                            criteriaBuilder.like(root.get("productSku"), _keyword)
                    ));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
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
        return productRepository.findByIdInAndStatusIn(collection, Arrays.asList(ProductStatusEnum.READY_CHECK));
    }

    @Transactional(readOnly = true)
    public Product findOne(long id) {
        return productRepository.findOne(id);
    }

    public boolean checkProduct(String productSku, long userId) {
        Product product = productRepository.findTopByProductSku(productSku);

        if (ObjectUtils.isEmpty(product)) {
            return false;
        }

        if (product.getUserId() == userId && product.getStatus().equals(ProductStatusEnum.READY_CHECK)) {
            return true;
        }

        return false;
    }
}
