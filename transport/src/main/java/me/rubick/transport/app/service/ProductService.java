package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.ProductStatus;
import me.rubick.transport.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
}
