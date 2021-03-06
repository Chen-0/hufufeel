package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.ProductStatusEnum;
import me.rubick.transport.app.model.Order;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.*;
import me.rubick.transport.app.vo.CostSnapshotVo;
import me.rubick.transport.app.vo.OrderExcelVo;
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
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
@Slf4j
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    @Resource
    private StockService stockService;

    @Resource
    private OrderLogisticsRepository orderLogisticsRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MessageService messageService;

    @Resource
    private PayService payService;

    public Page<Order> findAll(User user, String keyword, Integer status, Date start, Date end, Pageable pageable) {
        return orderRepository.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!ObjectUtils.isEmpty(user)) {
                    predicates.add(cb.equal(root.get("userId"), user.getId()));
                }

                if (StringUtils.hasText(keyword)) {
                    String _keyword = getKeyword(keyword);
                    Join<Order, User> joinU = root.join("user", JoinType.INNER);
                    predicates.add(cb.or(
                            cb.like(root.get("expressNo"), _keyword),
                            cb.like(root.get("sn"), _keyword),
                            cb.like(joinU.get("hwcSn"), _keyword),
                            cb.like(joinU.get("name"), _keyword)
                    ));
                }

                if (!ObjectUtils.isEmpty(start)) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
                }

                if (!ObjectUtils.isEmpty(end)) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
                }

                if (!ObjectUtils.isEmpty(status) && status != -1) {
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

        List<OrderItem> orderItems = createOrderItem(user, warehouse, products, quantities);

        int totalQty = 0;
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            totalQty += item.getQuantity();
            totalWeight = totalWeight.add(item.getProduct().getWeight().multiply(new BigDecimal(item.getQuantity())));
        }

        OrderSnapshotVo orderSnapshotVo = getOrderSnapshotVo(params);

        if (!StringUtils.hasText(orderSnapshotVo.getCkt4())) {
            orderSnapshotVo.setCkt4("");
        }

        if (!StringUtils.hasText(orderSnapshotVo.getCkt5())) {
            orderSnapshotVo.setCkt5("");
        }

        Order order = new Order();
        order.setReferenceNumber(orderSnapshotVo.getCkt4());
        order.setTn(orderSnapshotVo.getCkt5());

        order.setUserId(user.getId());
        order.setStatus(OrderStatusEnum.CHECK);
        order.setWarehouseId(warehouse.getId());
        order.setWarehouseName(warehouse.getName());
        order.setTotal(new BigDecimal(0));
        order.setWeight(totalWeight);
        order.setOrderSnapshot(JSONMapper.toJSON(orderSnapshotVo));
        order.setQuantity(totalQty);
        order.setPhone(orderSnapshotVo.getCkf4());
        order.setComment(orderSnapshotVo.getCkt6());
        order.setContact(orderSnapshotVo.getCkf2());
        order.setReason("");
        order.setSkuQty(products.size());
        order.setCostSnapshot(JSONMapper.toJSON(new CostSnapshotVo()));
        order.setSn(generateBatch());
        order.setcType(params.get("c_type"));

        if (order.getcType().equals("u")) {
            order.setDocumentId(Long.valueOf(params.get("did")));
            order.setPhone("");
            order.setContact("");
        }

        orderRepository.save(order);

        log.info("{}", JSONMapper.toJSON(order));

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }

        orderItemRepository.save(orderItems);
    }

    private List<OrderItem> createOrderItem(User user, Warehouse warehouse, List<Product> products, List<Integer> quantities) throws BusinessException {
        int count = products.size();
        List<OrderItem> orderItems = new ArrayList<>(count);

        if (products.size() != quantities.size()) {
            throw new BusinessException("SKU和数量不匹配");
        }

        for (int i = 0; i < count; i++) {

            Product product = products.get(i);
            int quantity = quantities.get(i);

            boolean flag = stockService.reduceStore(user, product, warehouse, quantity);

            if (!flag) {
                log.error("库存不足::userId={}, productId={}, wid={}, qty={}", user.getId(), product.getId(), warehouse.getId(), quantity);
                throw new BusinessException("SKU:" + product.getProductSku() + "库存不足！");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(quantity);
            orderItem.setProductSnapshot(JSONMapper.toJSON(BeanMapperUtils.map(products.get(i), ProductSnapshotVo.class)));
            orderItem.setProduct(product);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private OrderSnapshotVo getOrderSnapshotVo(Map<String, String> params) {
        OrderSnapshotVo orderSnapshotVo = BeanMapperUtils.map(params, OrderSnapshotVo.class);

        return orderSnapshotVo;
    }

    public String generateBatch() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        String batch = orderRepository.getMaxSN(dateString);

        //一共 2  +  8   +  4
        //    CK   date    no

        try {
            if (batch == null) {
                return "CK" + dateString + "0001";
            } else {
                String bb = batch.substring(10);
                String temp = batch.substring(0, 10);
                return temp + s2n(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("", e);
            return "CK" + dateString + "0001";
        }
    }

    private String s2n(String bb) {
        int no = Integer.valueOf(bb);
        no += 1;

        bb = String.valueOf(no);

        StringBuilder stringBuilder = new StringBuilder();

        int len = 4 - bb.length();

        for (int i = 0; i < len; i ++) {
            stringBuilder.append("0");
        }

        stringBuilder.append(bb);

        return stringBuilder.toString();
    }

    public void cancelOrder(Order order, long userId) {
        order.setStatus(OrderStatusEnum.CANCEL);

        for (OrderItem orderItem : order.getOrderItems()) {
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

    public void createOrder(List<OrderExcelVo> orderExcelVos, User user) throws BusinessException {
        for (OrderExcelVo orderExcelVo : orderExcelVos) {
            this.createOrder(orderExcelVo, user);
        }
    }

    private void createOrder(OrderExcelVo orderExcelVo, User user) throws BusinessException {
        Warehouse warehouse = warehouseRepository.findTopByNameAndVisible(orderExcelVo.getA(), true);

        if (ObjectUtils.isEmpty(warehouse)) {
            throw new BusinessException("错误！ 仓库：" + orderExcelVo.getA() + "无法匹配，请检查");
        }

        OrderSnapshotVo orderSnapshotVo = new OrderSnapshotVo();
        orderSnapshotVo.setCkt1(orderExcelVo.getB());
        orderSnapshotVo.setCkt2("");
        orderSnapshotVo.setCkf3(orderExcelVo.getC());
        orderSnapshotVo.setCkf4(orderExcelVo.getD());
        orderSnapshotVo.setCkf5(orderExcelVo.getE());
        orderSnapshotVo.setCkf6("");

        orderSnapshotVo.setCkf2(orderExcelVo.getF());
        orderSnapshotVo.setCkf4(orderExcelVo.getG());
        orderSnapshotVo.setCkf1(orderExcelVo.getH());
        orderSnapshotVo.setCkf3(orderExcelVo.getI());
        orderSnapshotVo.setCkf5(orderExcelVo.getJ());
        orderSnapshotVo.setCkf10(orderExcelVo.getK());
        orderSnapshotVo.setCkf11(orderExcelVo.getL());
        orderSnapshotVo.setCkf7(orderExcelVo.getM());

        //检查库存
        List<String> skus = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        getImportOrderItems(skus, quantities, orderExcelVo.getN(), orderExcelVo.getO());
        getImportOrderItems(skus, quantities, orderExcelVo.getP(), orderExcelVo.getQ());
        getImportOrderItems(skus, quantities, orderExcelVo.getR(), orderExcelVo.getS());
        getImportOrderItems(skus, quantities, orderExcelVo.getT(), orderExcelVo.getU());
        getImportOrderItems(skus, quantities, orderExcelVo.getV(), orderExcelVo.getW());
        getImportOrderItems(skus, quantities, orderExcelVo.getX(), orderExcelVo.getY());


        List<Product> products = new ArrayList<>(skus.size());

        Set<String> set = new HashSet<>();

        for (String s : skus) {
            Product product = productRepository.findTopByProductSkuAndUserIdAndStatus(s, user.getId(), ProductStatusEnum.READY_CHECK);

            if (ObjectUtils.isEmpty(product)) {
                throw new BusinessException("错误！SKU：" + s + "不存在！");
            }

            if (set.contains(s)) {
                throw new BusinessException("错误！SKU：" + s + "含有一个或多个！");
            }

            set.add(s);
            products.add(product);
        }

        int totalQty = 0;
        BigDecimal totalWeight = BigDecimal.ZERO;

        List<OrderItem> orderItems = createOrderItem(user, warehouse, products, quantities);

        for (OrderItem orderItem : orderItems) {
            totalQty += orderItem.getQuantity();
            totalWeight = totalWeight.add(orderItem.getProduct().getWeight().multiply(new BigDecimal(orderItem.getQuantity())));
        }

        Order order = new Order();
        order.setReferenceNumber(orderSnapshotVo.getCkt4());
        order.setTn(orderSnapshotVo.getCkt5());
        order.setUserId(user.getId());
        order.setStatus(OrderStatusEnum.CHECK);
        order.setWarehouseId(warehouse.getId());
        order.setWarehouseName(warehouse.getName());
        order.setTotal(new BigDecimal(0));
        order.setWeight(totalWeight);
        order.setOrderSnapshot(JSONMapper.toJSON(orderSnapshotVo));
        order.setQuantity(totalQty);
        order.setPhone(orderSnapshotVo.getCkf4());
        order.setComment(orderSnapshotVo.getCkt6());
        order.setContact(orderSnapshotVo.getCkf2());
        order.setReason("");
        order.setSkuQty(products.size());
        order.setCostSnapshot(JSONMapper.toJSON(new CostSnapshotVo()));
        order.setSn(generateBatch());
        order.setcType("w");

        orderRepository.save(order);

        log.info("{}", JSONMapper.toJSON(order));

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }

        orderItemRepository.save(orderItems);

    }

    private void getImportOrderItems(List<String> skus, List<Integer> quantities, String a, String b) throws BusinessException {
        if (StringUtils.hasText(a)) {
            if (!StringUtils.hasText(b)) {
                throw new BusinessException("请检查SKU:" + a + "的数量不能为空！");
            }

            skus.add(a);

            try {
                BigDecimal bigDecimal = new BigDecimal(b);
                quantities.add(bigDecimal.intValue());
            } catch (NumberFormatException e) {
                throw new BusinessException("请检查SKU:" + a + "的数量，必须为整数！");
            }
        }
    }

    /**
     * 订单出库
     * @param order
     * @param total 运费
     * @param express 快递公司
     * @param expressNo 快递单号
     */
    public void checkOut(Order order, BigDecimal total, String express, String expressNo) {
        if (!order.getStatus().equals(OrderStatusEnum.CHECK)) {
            return;
        }

        log.info("ORDER NO:{} is total:{}", order.getSn(), total);

        total =  total.setScale(2, BigDecimal.ROUND_HALF_UP);

        List<Statements> statementsList = new ArrayList<>();
        Statements statements = payService.createExpressFee(order, total);


        statementsList.add(payService.saveStatements(statements, total));

        order.setStatus(OrderStatusEnum.READY);
        order.setTotal(order.getTotal().add(statementsList.get(0).getTotal()));
        order.setExpressNo(expressNo);
        order.setExpress(express);
        orderRepository.save(order);

        boolean flag1 = payService.payStatements(statementsList);

        if (flag1) {
            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}审核成功！", order.getSn())
            );
        } else {
            order.setNextStatus(order.getStatus());
            order.setStatus(OrderStatusEnum.FREEZE);
            orderRepository.save(order);

            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}，扣费失败，请充值账号并重新缴费。", order.getSn())
            );
        }
    }

    public Order outbound(Order order, BigDecimal total, BigDecimal surcharge, String surchargeComment) {
        order.setStatus(OrderStatusEnum.SEND);
        order.setTotal(order.getTotal().add(total));
        order.setOutTime(new Date());
        order.setSurcharge(surcharge);
        order.setSurchargeComment(surchargeComment);
        return orderRepository.save(order);
    }

    public OrderLogistics findOrNewOrderLogistics(long orderId) {
        OrderLogistics orderLogistics = orderLogisticsRepository.findTopByOrderId(orderId);

        if (ObjectUtils.isEmpty(orderLogistics)) {
            orderLogistics = new OrderLogistics();
            orderLogistics.setOrderId(orderId);
        }

        return orderLogistics;
    }

    public OrderLogistics storeOrderLogistics(OrderLogistics orderLogistics) {
        return orderLogisticsRepository.save(orderLogistics);
    }

    public Order updateOrder(Order order) {
        if (order.getStatus() == OrderStatusEnum.FAIL) {
            order.setStatus(OrderStatusEnum.CHECK);
        }

        return orderRepository.save(order);
    }
}
