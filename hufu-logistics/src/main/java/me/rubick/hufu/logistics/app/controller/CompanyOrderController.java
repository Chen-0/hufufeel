package me.rubick.hufu.logistics.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.library.Common;
import me.rubick.hufu.logistics.app.model.Company;
import me.rubick.hufu.logistics.app.model.CompanyExpress;
import me.rubick.hufu.logistics.app.model.CompanyOrder;
import me.rubick.hufu.logistics.app.service.CompanyOrderService;
import me.rubick.hufu.logistics.app.service.CompanyService;
import me.rubick.hufu.logistics.app.service.ExcelHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes("status")
public class CompanyOrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyOrderService companyOrderService;

    @Resource
    private ExcelHelper excelHelper;

    @Resource
    private CompanyService companyService;

    @RequestMapping(value = "order/index")
    public String orderIndex(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "1") Integer status,
            @RequestParam(value = "ts", required = false) String ts,
            @RequestParam(value = "te", required = false) String te,
            @PageableDefault(value = 50) Pageable pageable,
            @RequestParam(value = "expressId", required = false, defaultValue = "-1") Integer expressId,
            Model model
    ) {
        keyword = Common.encodeStr(keyword);

        if (companyService.isAdminWithLogin()) {
            return adminIndex(keyword, status, ts, te, pageable, expressId, model);
        } else {
            return normalIndex(keyword, status, ts, te, pageable, model);
        }

    }

    private String adminIndex(
            String keyword,
            Integer status,
            String ts,
            String te,
            Pageable pageable,
            Integer expressId,
            Model model
    ) {
        Page<CompanyOrder> companyOrders = companyOrderService.findOrder(
                keyword,
                ts,
                te,
                status,
                expressId,
                pageable,
                -1
        );

        List<CompanyExpress> companyExpressList = companyOrderService.findAllCompanyExpress();

        model.addAttribute("orders", companyOrders);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("ts", ts);
        model.addAttribute("te", te);
        model.addAttribute("expressId", expressId);
        model.addAttribute("companyExpressList", companyExpressList);

        return "company/order/index";
    }

    private String normalIndex(
            String keyword,
            Integer status,
            String ts,
            String te,
            Pageable pageable,
            Model model
    ) {
        Company company = companyService.getLogin();
        Page<CompanyOrder> companyOrders = companyOrderService.findOrder(
                keyword,
                ts,
                te,
                status,
                -1,
                pageable,
                company.getId()
        );

        model.addAttribute("orders", companyOrders);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("ts", ts);
        model.addAttribute("te", te);

        return "normal/company/order/index";
    }


    // -------------------- 导入 excel -----------------------------
    @RequestMapping(value = "/order/excel/upload", method = RequestMethod.GET)
    public String uploadExcel(Model model) {
        if (companyService.isAdminWithLogin()) {
            List<Company> companies = companyOrderService.findAllCompany();

            model.addAttribute("companies", companies);
            model.addAttribute("flag", 1);

            return "/company/order/upload";
        } else {
            Company company = companyService.getLogin();

            model.addAttribute("company", company);

            return "/normal/company/order/import";
        }
    }

    @RequestMapping(value = "/order/excel/parse", method = RequestMethod.POST)
    public String parseExcel(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("company_id") int companyId,
            Model model
    ) {
        try {
            companyOrderService.parseExcel(multipartFile, companyId);
        } catch (BaseException e) {
            Common.logExceptionToFile(logger, e);
            model.addAttribute("message", e.getMessage());
            return "exception/error";
        }
        return "redirect:/order/index?status=1";
    }

    @RequestMapping(value = "/order/select", method = RequestMethod.GET)
    public String selectOrder(
            @RequestParam(value = "tracking_number[]") String[] trackingNumber,
            Model model
    ) {
        List<CompanyOrder> orderList = companyOrderService.findInTrackingNumber(trackingNumber);

        model.addAttribute("orders", orderList);
        return "company/order/list";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.GET)
    public String createOrder() {
        return "/normal/company/order/create";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String postCreateOrder(CompanyOrder companyOrder) throws BaseException {
        Common.checkNotNull(companyOrder.getTrackingNumber(), "订单号不能为空");
        companyOrderService.create(companyOrder);
        return "redirect:/order/index";
    }

    @RequestMapping(value = "/order/export/default", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcelByDefault(
            @RequestParam("tracking_number") String trackingNumber,
            HttpServletResponse response
    ) throws BaseException {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);
        List<CompanyOrder> companyOrders = companyOrderService.findInTrackingNumber(trackingNumbers);

        Date nowTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String time = simpleDateFormat.format(nowTime);
        try {
            String filename = "虎芙导出_" + time + ".xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/msexcel");
            excelHelper.writeDefault(companyOrders, response.getOutputStream());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @RequestMapping(value = "order/{id}/recheck", method = RequestMethod.GET)
    public String getRecheck(
            Model model,
            @PathVariable("id") Integer id
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null) {
            throw new BaseException("该运单不存在");
        }

        model.addAttribute("order", order);

        return "normal/company/order/recheck";
    }

    @RequestMapping(value = "order/{id}/recheck", method = RequestMethod.POST)
    public String postRecheck(
            @PathVariable("id") Integer id,
            @RequestParam("batch") String batch,
            @RequestParam("tracking_number") String trackingNumber,
            @RequestParam("sender") String sender,
            @RequestParam("sender_phone") String senderPhone,
            @RequestParam("contact") String contact,
            @RequestParam("identity") String identity,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("zip_code") String zipCode,
            @RequestParam("goods_name") String goodsName,
            @RequestParam("quantity") BigDecimal quantity,
            @RequestParam("declared") BigDecimal declared,
            @RequestParam("comment") String comment,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("status") Integer s
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null) {
            throw new BaseException("该运单不存在！");
        }

        order.setBatch(batch);
        order.setTrackingNumber(trackingNumber);
        order.setSender(sender);
        order.setSenderPhone(senderPhone);
        order.setContact(contact);
        order.setIdentity(identity);
        order.setPhone(phone);
        order.setAddress(address);
        order.setZipCode(zipCode);
        order.setGoodsName(goodsName);
        order.setQuantity(quantity);
        order.setDeclared(declared);
        order.setComment(comment);
        order.setStatusId(100);
        companyOrderService.update(order);

        redirectAttributes.addFlashAttribute("success", "修改运单信息成功！");
        return "redirect:/order/index?status=" + s;
    }

    @RequestMapping(value = "order/{id}/show", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("id") Integer id) {
        CompanyOrder order = companyOrderService.find(id);
        model.addAttribute("order", order);
        return "company/order/show";
    }
}
