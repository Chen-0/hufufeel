package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class OrderController extends AbstractController {

    @Resource
    private ProductService productService;

    @Resource
    private OrderService orderService;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private OrderRepository orderRepository;

    @RequestMapping("/order/index")
    public String index(
            Model model,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        User user = userService.getByLogin();
        model.addAttribute("elements", orderService.findAll(user, keyword, status, pageable));
        model.addAttribute("_STATUS", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("MENU", "DINGDANGUANLI");
        return "/order/index";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String postCreateOrder(
            @RequestParam("pids[]") List<Long> productIds,
            @RequestParam("qty[]") List<Integer> quantities,
            @RequestParam("wid") long wid,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {

        Warehouse warehouse = warehouseRepository.findOne(wid);

        if (ObjectUtils.isEmpty(warehouse)) {
            log.error("仓库不存在 wid = {}", wid);
            throw new BusinessException("提交失败！含有一个或多个错误");
        }

        List<Product> products = productService.findProducts(productIds);

        if (products.size() == productIds.size() && productIds.size() == quantities.size()) {
            User user = userService.getByLogin();

            orderService.createOrder(user, products, quantities, warehouse, params);
            redirectAttributes.addFlashAttribute("SUCCESS", "新建发货单成功！");
            return "redirect:/order/index";
        } else {
            log.info("{}", products.size());
            log.info("{}", JSONMapper.toJSON(productIds));
            log.info("{}", JSONMapper.toJSON(quantities));
            log.info("{}", JSONMapper.toJSON(params));
            throw new BusinessException("提交失败！含有一个或多个错误");
        }
    }

    @RequestMapping("/order/{id}/cancel")
    public String cancelOrder(
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes) throws BusinessException {
        Order order = orderRepository.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            throw new BusinessException("[A005] 数据异常");
        }

        User user = userService.getByLogin();

        if (order.getUserId() != user.getId()) {
            throw new BusinessException("[A006] 禁止访问");
        }

        if (order.getStatus().equals(OrderStatusEnum.CHECK) || order.getStatus().equals(OrderStatusEnum.FAIL)) {
            orderService.cancelOrder(order, user.getId());
            redirectAttributes.addFlashAttribute("SUCCESS", "取消订单成功！");
        } else {
            throw new BusinessException("[A007] 状态异常");
        }

        return "redirect:/order/index";
    }
}
