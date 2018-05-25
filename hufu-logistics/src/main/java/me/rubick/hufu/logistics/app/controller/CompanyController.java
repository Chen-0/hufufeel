package me.rubick.hufu.logistics.app.controller;

import me.rubick.hufu.logistics.app.library.Common;
import me.rubick.hufu.logistics.app.library.MessageHelper;
import me.rubick.hufu.logistics.app.model.Company;
import me.rubick.hufu.logistics.app.model.CompanyExpress;
import me.rubick.hufu.logistics.app.model.CompanyOrder;
import me.rubick.hufu.logistics.app.model.CompanyRExpress;
import me.rubick.hufu.logistics.app.service.CompanyOrderService;
import me.rubick.hufu.logistics.app.service.ExcelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.*;
import me.rubick.hufu.logistics.app.service.CompanyService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes("status")
public class CompanyController {
    @Resource
    private CompanyOrderService companyOrderService;

    @Resource
    private ExcelHelper excelHelper;

    @Resource
    private CompanyService companyService;

    private Logger logger = LoggerFactory.getLogger(CompanyController.class);


    @RequestMapping(value = "order/prepare/{id}", method = RequestMethod.GET)
    public String prepareIn(
            @PathVariable("id") Integer id,
            Model model
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);
        List<CompanyExpress> companyExpresses = companyOrderService.getAllCompanyExpress();

        if (order == null || !order.getStatusId().equals(1)) {
            throw new BaseException("运单状态错误 System Error Code: 1");
        }

        model.addAttribute("expresses", companyExpresses);
        model.addAttribute("order", order);

        return "company/order/prepare_in";
    }

    @RequestMapping(value = "order/prepare/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String postPrepare(
            @PathVariable("id") Integer id,
            @RequestParam("in_weight") BigDecimal inWeight,
            @RequestParam("express_id") Integer expressId,
            Model model
    ) throws Exception {
        CompanyOrder order = companyOrderService.find(id);

        Common.checkNotNull(inWeight, "入库重不能为空");
        Common.checkNotNull(expressId, "请选择发货渠道");

        if (order != null && order.getStatusId().equals(1)) {
            Map<String, String> result = new HashMap<>();
            result.put("url", "/order/index?status=1");

            BigDecimal price = companyOrderService.getPriceByUserIdAndExpressId(order.getCompanyId(), expressId);
            BigDecimal total = price.multiply(inWeight).add(new BigDecimal(order.getInsurance()).multiply(new BigDecimal(0.02D)));

            if (order.getCompany().getMoney().compareTo(total) >= 0) {
                order.setStatusId(2);
                order.setInTime(new Date());
                order.setInWeight(inWeight);

                if (order.getComment() != null && order.getComment().equals("您的余额不足，无法入库")) {
                    order.setComment("");
                }
                companyOrderService.update(order);

                return MessageHelper.toSuccess("成功入库", result);
            } else {
                order.setComment("您的余额不足，无法入库");
                companyOrderService.update(order);

                return MessageHelper.toSuccess("您的余额不足，无法入库", result);
            }
        }

        return MessageHelper.toError();
    }

    @RequestMapping(value = "order/send/{id}", method = RequestMethod.GET)
    public String getSend(
            @PathVariable("id") Integer id,
            Model model
    ) throws Exception {
        CompanyOrder order = companyOrderService.find(id);
        List<CompanyExpress> companyExpresses = companyOrderService.getAllCompanyExpress();

        if (order == null || !order.getStatusId().equals(2)) {
            throw new BaseException("运单状态错误 System Error Code: 2");
        }

        model.addAttribute("expresses", companyExpresses);
        model.addAttribute("order", order);

        return "company/order/send";
    }

    @RequestMapping(value = "order/send/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String postSend(
            @PathVariable("id") Integer id,
            @RequestParam("in_weight") BigDecimal outWeight,
            @RequestParam("express_id") Integer expressId,
            @RequestParam("comment") String comment
    ) throws Exception {
        CompanyOrder order = companyOrderService.find(id);

        Common.checkNotNull(outWeight, "出库重量不能为空");
        Common.checkNotNull(expressId, "请选择发货渠道");

        if (order != null && order.getStatusId().equals(2)) {
            Map<String, String> result = new HashMap<>();
            result.put("url", "/order/index?status=2");

            BigDecimal price = companyOrderService.getPriceByUserIdAndExpressId(order.getCompanyId(), expressId);
            BigDecimal total = price.multiply(outWeight).add(new BigDecimal(order.getInsurance()).multiply(new BigDecimal(0.02D)));

            if (order.getCompany().getMoney().compareTo(total) >= 0) {
                //扣钱
                Company company = order.getCompany();
                company.setMoney(company.getMoney().subtract(total));
                companyOrderService.updateCompany(company);

                order.setStatusId(3);
                order.setOutTime(new Date());
                order.setOutWeight(outWeight);
                order.setComment(comment);
                order.setTotal(total);
                order.setCompanyExpressId(expressId);
                order.setLgStatus("已发货");
                order.setLgInfo("包裹已预报（" + getTime(order.getCreatedAt()) + "）\n包裹已签收（" + getTime(order.getInTime()) + "）\n包裹打包完成，正在发往中国（" + getTime(order.getOutTime()) + "）");

                companyOrderService.update(order);

                return MessageHelper.toSuccess("成功发货", result);
            } else {
                return MessageHelper.toSuccess("您的余额不足，无法发货", result);
            }
        }

        return MessageHelper.toError();
    }

    private static String getTime(Date time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    @RequestMapping(value = "order/{id}/update", method = RequestMethod.GET)
    public String getUpdateOrder(
            @PathVariable("id") Integer id,
            Model model
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null) {
            throw new BaseException("运单不存在");
        }

        model.addAttribute("order", order);

        return "company/order/update";
    }

    @RequestMapping(value = "order/{id}/update", method = RequestMethod.POST)
    public String postUpdateOrder(
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
            @RequestParam("insurance") Integer insurance,
            @RequestParam("comment") String comment,
            @RequestParam(value = "lg_status", required = false) String lgStatus,
            @RequestParam(value = "lg_info", required = false) String lgInfo,
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
        order.setInsurance(insurance);
        order.setComment(comment);
        if (lgInfo != null) {
            order.setLgInfo(lgInfo);
        }
        if (lgStatus != null) {
            order.setLgStatus(lgStatus);
        }
        companyOrderService.update(order);

        redirectAttributes.addFlashAttribute("success", "修改运单信息成功！");
        return "redirect:/order/index?status=" + s;
    }


    @RequestMapping(value = "/order/excel/upload/lg", method = RequestMethod.GET)
    public String uploadExcelLg(Model model) {
        model.addAttribute("flag", 2);
        return "/company/order/upload";
    }

    @RequestMapping(value = "/order/excel/lg", method = RequestMethod.POST)
    public String parseLg(
            @RequestParam("file") MultipartFile multipartFile
    ) throws BaseException {
        companyOrderService.parseExcelLg(multipartFile);
        return "redirect:/order/index?status=3";
    }

    @RequestMapping(value = "/order/export_guangzhuo", method = RequestMethod.GET)
    public void exportGuangZhuo(
            @RequestParam("tracking_number") String trackingNumber,
            HttpServletResponse response
    ) throws BaseException {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);
        List<CompanyOrder> companyOrders = companyOrderService.findInTrackingNumber(trackingNumbers);

        try {
            String filename = "广州渠道导出.xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/msexcel");
            excelHelper.writeGuangZhuo(companyOrders, response.getOutputStream());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @RequestMapping(value = "/order/export_e", method = RequestMethod.GET)
    public void exportE(
            @RequestParam("tracking_number") String trackingNumber,
            HttpServletResponse response
    ) throws BaseException {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);
        List<CompanyOrder> companyOrders = companyOrderService.findInTrackingNumber(trackingNumbers);

        try {
            String filename = "E特快渠道导出.xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/msexcel");
            excelHelper.writeE(companyOrders, response.getOutputStream());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @RequestMapping(value = "/order/export_lg", method = RequestMethod.GET)
    public void exportLg(
            @RequestParam("tracking_number") String trackingNumber,
            HttpServletResponse response
    ) throws BaseException {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);
        List<CompanyOrder> companyOrders = companyOrderService.findInTrackingNumber(trackingNumbers);

        try {
            String filename = "物流信息.xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/msexcel");
            excelHelper.writeLg(companyOrders, response.getOutputStream());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @RequestMapping(value = "/order/remove", method = RequestMethod.POST)
    public String remove(
            @RequestParam("tracking_number") String trackingNumber
    ) {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);

        companyOrderService.deleteIn(trackingNumbers);

        return "redirect:/order/index?status=1";
    }

    @RequestMapping(value = "/order/print/brcode", method = RequestMethod.GET)
    public String printBrcode(
            @RequestParam("tracking_number") String trackingNumber,
            @RequestParam("flag") Integer flag,
            Model model
    ) {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);
        List<CompanyOrder> orderList = companyOrderService.findInTrackingNumber(trackingNumbers);

        model.addAttribute("orders", orderList);
        model.addAttribute("flag", flag);

        return "company/order/print_brcode";
    }

    @RequestMapping(value = "/order/reject/{id}", method = RequestMethod.GET)
    public String getRejectOrder(
            @PathVariable("id") Integer id,
            Model model
    ) throws BaseException {
        CompanyOrder companyOrder = companyOrderService.find(id);

        if (!companyOrder.getStatusId().equals(2)) {
            throw new BaseException("订单的状态错误");
        }

        model.addAttribute("order", companyOrder);

        return "company/order/reject";
    }

    @RequestMapping(value = "/order/reject/{id}", method = RequestMethod.POST)
    public String postRejctOrder(
            @PathVariable("id") Integer id,
            @RequestParam("error_msg") String errorMsg
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        Common.checkNotNull(errorMsg, "请填写错误的信息");

        if (order == null || !order.getStatusId().equals(2)) {
            throw new BaseException("订单的状态错误");
        }

        order.setStatusId(99);
        order.setErrorMsg(errorMsg);
        companyOrderService.update(order);

        return "redirect:/order/index?status=2";
    }

    @RequestMapping(value = "/order/{id}/check", method = RequestMethod.GET)
    public String getCheckOrder(
            @PathVariable("id") Integer id,
            Model model
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null || !order.getStatusId().equals(100)) {
            throw new BaseException("运单的状态错误 System Error Code: 100");
        }

        model.addAttribute("order", order);

        return "company/order/check";
    }

    @RequestMapping(value = "/order/{id}/check", method = RequestMethod.POST)
    public String postCheckOrder(
            @PathVariable("id") Integer id,
            @RequestParam("flag") Integer flag,
            @RequestParam("error_msg") String errorMsg
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (!(order != null && order.getStatusId().equals(100))) {
            throw new BaseException("运单的状态错误 System Error Code: 100");
        }

        if (flag.equals(1)) {
            order.setStatusId(2);
            order.setErrorMsg("");
            companyOrderService.update(order);
        } else {
            order.setStatusId(99);
            order.setErrorMsg(errorMsg);
            companyOrderService.update(order);
        }

        return "redirect:/order/index?status=100";
    }

    @RequestMapping(value = "/order/{id}/recover", method = RequestMethod.GET)
    public String getRecoverOrder(
            @PathVariable("id") Integer id,
            Model model
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null || !order.getStatusId().equals(99)) {
            throw new BaseException("运单状态错误 System Error Code: 99");
        }

        model.addAttribute("order", order);

        return "company/order/recover";
    }

    @RequestMapping(value = "/order/{id}/recover", method = RequestMethod.POST)
    public String postRecoverOrder(
            @PathVariable("id") Integer id
    ) throws BaseException {
        CompanyOrder order = companyOrderService.find(id);

        if (order == null || !order.getStatusId().equals(99)) {
            throw new BaseException("运单状态错误 System Error Code: 99");
        }

        order.setStatusId(2);
        order.setErrorMsg("");
        companyOrderService.update(order);

        return "redirect:/order/index?status=99";
    }

    @RequestMapping(value = "/order/in", method = RequestMethod.GET)
    public String inOrder(
            @RequestParam("tracking_number") String trackingNumber
    ) throws BaseException {
        String[] trackingNumbers = Common.splitStringToArrayWithSpace(trackingNumber);

        companyOrderService.updateOrderStatusTo2(trackingNumbers);

        return "redirect:/order/index?status=1";
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //大客户渠道

    @RequestMapping(value = "company/express/index", method = RequestMethod.GET)
    public String getExpress(Model model) {
        List<CompanyExpress> companyExpressList = companyService.getAllCompanyExpress();

        model.addAttribute("expresses", companyExpressList);
        return "/company/express/index";
    }

    @RequestMapping(value = "company/express/create", method = RequestMethod.GET)
    public String getCreateExpress(Model model) {
        return "/company/express/create";
    }

    @RequestMapping(value = "company/express/create", method = RequestMethod.POST)
    public String postCreateExpress(
            RedirectAttributes redirectAttributes,
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price
    ) throws BaseException {
        Common.checkNotNull(name, "渠道名称不能为空");
        Common.checkNotNull(price, "请填写默认的渠道价格");
        CompanyExpress companyExpress = new CompanyExpress();
        companyExpress.setName(name);
        companyExpress.setPrice(price);

        companyExpress = companyService.storeCompanyExpress(companyExpress);

        //帮所有用户新增
        List<Company> companies = companyOrderService.findAllCompany();

        for (Company company : companies) {
            CompanyRExpress companyRExpress = new CompanyRExpress();
            companyRExpress.setCompanyId(company.getId());
            companyRExpress.setExpressId(companyExpress.getId());
            companyRExpress.setPrice(companyExpress.getPrice());

            companyService.storeCompanyRExpress(companyRExpress);
        }

        redirectAttributes.addFlashAttribute("success", "修改成功!");
        return "redirect:/company/express/index";
    }

    @RequestMapping(value = "company/express/{id}/update", method = RequestMethod.GET)
    public String getUpdateExpress(Model model, @PathVariable("id") Integer id) {
        CompanyExpress companyExpress = companyService.findCompanyExpress(id);

        Common.checkNotNull(companyExpress);

        model.addAttribute("express", companyExpress);
        return "/company/express/update";
    }

    @RequestMapping(value = "/company/express/{id}/update", method = RequestMethod.POST)
    public String postUpdateExpress(
            RedirectAttributes redirectAttributes,
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price
    ) throws BaseException {
        Common.checkNotNull(name, "请填写渠道名称");
        Common.checkNotNull(price, "请填写渠道默认价格");
        CompanyExpress companyExpress = companyService.findCompanyExpress(id);

        Common.checkNotNull(companyExpress);
        companyExpress.setName(name);
        companyExpress.setPrice(price);
        companyService.storeCompanyExpress(companyExpress);

        redirectAttributes.addFlashAttribute("success", "修改成功!");
        return "redirect:/company/express/index";
    }


    /////////////////////////////////////大客户管理///////////////////////////////////////////////
    @RequestMapping(value = "/company/index", method = RequestMethod.GET)
    public String getCompany(Model model) {
        List<Company> companyList = companyOrderService.findAllCompany();
        model.addAttribute("companies", companyList);
        return "/company/index";
    }

    @RequestMapping(value = "/company/{id}/update", method = RequestMethod.GET)
    public String getUpdateCompany(
            Model model,
            @PathVariable("id") Integer id
    ) {
        Company company = companyService.findOne(id);

        model.addAttribute("company", company);
        return "company/update";
    }

    @RequestMapping(value = "/company/{id}/update", method = RequestMethod.POST)
    public String postUpdateCompany(
            Model model,
            @PathVariable("id") Integer id,
            Company newCompany,
            @RequestParam("ids[]") Integer[] ids,
            @RequestParam("price[]") BigDecimal[] prices,
            RedirectAttributes redirectAttributes
    ) throws BaseException {
        Company company = companyService.findOne(id);
        Common.checkNotNull(company);

        company.setMoney(newCompany.getMoney());
        company.setName(newCompany.getName());
        company.setUsername(newCompany.getUsername());

        if (Common.hasText(newCompany.getPassword())) {
            company.setPassword(Common.md5(newCompany.getPassword()));
        }

        companyService.storeCompany(company);

        //更新各个渠道的价格

        for (int i = 0; i < ids.length; i++) {
            CompanyRExpress companyRExpress = companyService.findCompanyRExpress(ids[i]);
            companyRExpress.setPrice(prices[i]);
            companyService.storeCompanyRExpress(companyRExpress);
        }

        redirectAttributes.addFlashAttribute("success", "修改成功！");
        return "redirect:/company/index";
    }

    @RequestMapping(value = "/company/create", method = RequestMethod.GET)
    public String getCreateCompany() {
        return "company/create";
    }

    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String postCreateCompany(
            Company company
    ) throws BaseException {

        Common.checkNotNull(company.getName(), "请填写【大客户名称】");
        Common.checkNotNull(company.getUsername(), "请填写【登录账号】");
        Common.checkNotNull(company.getPassword(), "请填写【登录密码】");

        if (companyService.findByUsername(company.getUsername()) != null) {
            throw new BaseException("该登录账号已存在");
        }
        company.setMoney(new BigDecimal(0));
        company.setPassword(Common.md5(company.getPassword()));

        company = companyService.storeCompany(company);
        List<CompanyExpress> companyExpressList = companyService.getAllCompanyExpress();

        for (CompanyExpress ce :
                companyExpressList) {
            CompanyRExpress companyRExpress = new CompanyRExpress();
            companyRExpress.setPrice(ce.getPrice());
            companyRExpress.setCompanyId(company.getId());
            companyRExpress.setExpressId(ce.getId());
            companyService.storeCompanyRExpress(companyRExpress);
        }


        return "redirect:/company/index";
    }
}
