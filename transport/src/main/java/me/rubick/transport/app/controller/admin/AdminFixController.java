package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.Order;
import me.rubick.transport.app.model.OrderItem;
import me.rubick.transport.app.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/admin")
@Controller
@Slf4j
public class AdminFixController {

    @Resource
    private OrderRepository orderRepository;

    @RequestMapping("/fix/bug")
    @ResponseBody
    public String fixBug() {

        List<Order> orders = orderRepository.findAll();

        for (Order order: orders) {

            BigDecimal weight = BigDecimal.ZERO;

            for (OrderItem orderItem: order.getOrderItems()) {
                weight = weight.add(orderItem.getProduct().getWeight().multiply(new BigDecimal(orderItem.getQuantity())));
            }

            order.setWeight(weight);
            log.info("order id={}, weight={}", order.getId(), order.getWeight());
            orderRepository.save(order);
        }

        return "1.0.0";
    }
}
