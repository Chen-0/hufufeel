package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.OrderItemRepository;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.vo.CostSnapshotVo;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import me.rubick.transport.app.vo.ProductSnapshotVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
@Slf4j
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    @Resource
    private StockService stockService;

    public Page<Order> findAll(User user, String keyword, Integer status, Pageable pageable) {
        return orderRepository.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("userId"), user.getId()));

                if (StringUtils.hasText(keyword)) {
                    String _keyword = getKeyword(keyword);
                    predicates.add(cb.or(
                            cb.like(root.get("referenceNumber"), _keyword),
                            cb.like(root.get("tn"), _keyword),
                            cb.like(root.get("sn"), _keyword)
                    ));
                }

                if (!ObjectUtils.isEmpty(status)) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        }, pageable);
    }

    private String getKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    public void createOrder(
            User user,
            List<Product> products,
            List<Integer> quantities,
            Warehouse warehouse,
            Map<String, String> params
    ) throws BusinessException {
        //检查库存
        int count = products.size();
        int totalQty = 0;
        List<OrderItem> orderItems = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {

            Product product = products.get(i);
            int quantity = quantities.get(i);

            boolean flag = stockService.reduceStore(user, product, warehouse, quantity);

            if (!flag) {
                log.error("库存不足::userId={}, productId={}, wid={}, qty={}", user.getId(), product.getId(), warehouse.getId(), quantity);
                throw new BusinessException("库存不足");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(quantity);
            orderItem.setProductSnapshot(JSONMapper.toJSON(BeanMapperUtils.map(products.get(i), ProductSnapshotVo.class)));

            orderItems.add(orderItem);

            totalQty += quantity;
        }

        OrderSnapshotVo orderSnapshotVo = getOrderSnapshotVo(params);
        Order order = new Order();
        order.setReferenceNumber(orderSnapshotVo.getCkt4());
        order.setTn(orderSnapshotVo.getCkt5());
        order.setUserId(user.getId());
        order.setStatus(OrderStatusEnum.CHECK);
        order.setWarehouseId(warehouse.getId());
        order.setWarehouseName(warehouse.getName());
        order.setTotal(new BigDecimal(0));
        order.setWeight(new BigDecimal(0));
        order.setOrderSnapshot(JSONMapper.toJSON(orderSnapshotVo));
        order.setQuantity(totalQty);
        order.setPhone(orderSnapshotVo.getCkf4());
        order.setComment(orderSnapshotVo.getCkt6());
        order.setContact(orderSnapshotVo.getCkf2());
        order.setReason("");
        order.setSkuQty(products.size());
        order.setCostSnapshot(JSONMapper.toJSON(new CostSnapshotVo()));
        order.setSn(generateBatch());

        orderRepository.save(order);

        log.info("{}", JSONMapper.toJSON(order));

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }

        orderItemRepository.save(orderItems);
    }

    private OrderSnapshotVo getOrderSnapshotVo(Map<String, String> params) {
        OrderSnapshotVo orderSnapshotVo = BeanMapperUtils.map(params, OrderSnapshotVo.class);

        return orderSnapshotVo;
    }

    public String generateBatch() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        String batch = orderRepository.getMaxSN(dateString);

        if (batch == null) {
            return "CK" + dateString + "0001";
        } else {
            String temp = batch.substring(0, 8);
            Integer no = Integer.valueOf(batch.substring(8)) + 1;
            return temp + no;
        }
    }

    public void cancelOrder(Order order, long userId) {
        order.setStatus(OrderStatusEnum.CANCEL);

        for (OrderItem orderItem: order.getOrderItems()) {
            stockService.addStock(
                    userId,
                    orderItem.getProductId(),
                    order.getWarehouseId(),
                    orderItem.getQuantity()
            );
        }

        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findOne(long id) {
        Order order = orderRepository.findOne(id);
        order.setOrderSnapshotVo(JSONMapper.fromJson(order.getOrderSnapshot(), OrderSnapshotVo.class));
        order.setCostSnapshotVo(JSONMapper.fromJson(order.getCostSnapshot(), CostSnapshotVo.class));

        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setProductSnapshotVo(JSONMapper.fromJson(orderItem.getProductSnapshot(), ProductSnapshotVo.class));
        }
        return order;
    }
}
