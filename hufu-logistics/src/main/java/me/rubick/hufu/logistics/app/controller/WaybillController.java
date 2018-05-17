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
import me.rubick.hufu.logistics.app.exception.NotFoundException;
import me.rubick.hufu.logistics.app.library.Common;
import me.rubick.hufu.logistics.app.library.MessageHelper;
import me.rubick.hufu.logistics.app.model.Brand;
import me.rubick.hufu.logistics.app.model.Goods;
import me.rubick.hufu.logistics.app.model.User;
import me.rubick.hufu.logistics.app.model.Waybill;
import me.rubick.hufu.logistics.app.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("waybill")
@SessionAttributes("status")
public class WaybillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WaybillService waybillService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private EmailService emailService;

    @Resource
    private ExcelHelper excelHelper;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(
            Model model,
            @RequestParam(value = "s", defaultValue = "0") Integer s,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @PageableDefault(value = 50) Pageable pageable,
            @ModelAttribute(value = "success") String success
    ) {
        keyword = Common.encodeStr(keyword);
        Page<Waybill> waybills = waybillService.getWaybill(s, keyword, pageable);

        model.addAttribute("orders", waybills);
        model.addAttribute("status", s);
        model.addAttribute("keyword", keyword);

        return "waybill/index";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createWaybill(Model model) {
        List<Brand> brands = waybillService.getAllBrand();

        model.addAttribute("brands", brands);

        return "waybill/create";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String storeWaybill(
            Waybill w,
            @RequestParam(value = "gooda[]", required = false) String[] gooda,
            @RequestParam(value = "goodb[]", required = false) String[] goodb,
            @RequestParam(value = "brandname[]", required = false) String[] brandname,
            @RequestParam(value = "goodname[]", required = false) String[] goodname,
            @RequestParam(value = "countnum[]", required = false) String[] countnum,
            @RequestParam(value = "extraCost", required = false, defaultValue = "0") BigDecimal extraCost,
            @RequestParam(value = "extraCostReason", required = false) String extraCostReason,
            @ModelAttribute("status") String s,
            RedirectAttributes redirectAttributes
    ) throws BaseException {
        Common.checkNotNull(w.getCustomer(), "客户标示不能为空");
        User user = userService.findByCustomer(w.getCustomer());

        Common.checkNotNull(user, "没有找到用户，请填写正确的用户标识");
        Common.checkNotNull(w.getExpress(), "快递公司不能为空");
        Common.checkNotNull(w.getExpressnum(), "快递单号不能为空");
        Common.checkNotNull(w.getStorageNo(), "仓储位不能为空");
        Common.checkNotNull(w.getWeight(), "入库重不能为空");
        Common.checkNotNull(gooda, "货物分类不能为空");
        Common.checkNotNull(goodb, "货物子分类不能为空");

        w.setTrackingNumber(Common.generateTranCode());
        w.setArrive(1);
        w.setCreateTime(new Date());
        w.setIsOurGoodId(0);
        w.setIsOurGood("待发货");
        w.setUserId(user.getId());
        w.setMendCost(new BigDecimal(0));
        w.setUsername(user.getUsername());
        w.setOutWeight(new BigDecimal(0));
        w.setPaymentStatus("Paid");
        w.setInTime(new Date());
        w.setLogistics_status("包裹已签收（" + Common.dateConvertString(new Date()) + "）");
        w.setExtra_cost_reason(extraCostReason);
        w.setExtra_cost(extraCost);

        Waybill waybill = waybillService.save(w);

        if (
                Common.isNotEmpty(gooda) &&
                        Common.isNotEmpty(goodb) &&
                        Common.isNotEmpty(brandname) &&
                        Common.isNotEmpty(goodname) &&
                        Common.isNotEmpty(countnum)
                ) {
            for (int i = 0; i < gooda.length; i++) {
                Goods goods = new Goods();
                goods.setBrandname(brandname[i]);
                goods.setContnum(countnum[i]);
                goods.setGooda(gooda[i]);
                goods.setGoodb(goodb[i]);
                goods.setGoodname(goodname[i]);
                goods.setFarherId(waybill.getId());

                waybillService.saveGoods(goods);
            }
        }

        messageService.sendInMessage(user.getId());
        emailService.sendInEmail(user.getUsername());

        redirectAttributes.addFlashAttribute("success", "入库成功");

        return "redirect:/waybill/" + waybill.getId() + "/show";
    }

    @RequestMapping(value = "/{id}/in", method = RequestMethod.GET)
    public String getIn(Model model, @PathVariable("id") Integer id) {
        Waybill waybill = waybillService.get(id);
        Common.checkNotNull(waybill);

        model.addAttribute("waybill", waybill);

        return "waybill/in";
    }

    @RequestMapping(value = "/{id}/in", method = RequestMethod.POST)
    public String postIn(
            @PathVariable("id") Integer id,
            @RequestParam("weight") BigDecimal weight,
            @RequestParam("storageNo") String storageNo,
            @RequestParam(value = "extraCost", required = false, defaultValue = "0") BigDecimal extraCost,
            @RequestParam(value = "extraCostReason", required = false) String extraCostReason,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("status") Integer s
    ) throws BaseException {
        Waybill waybill = waybillService.get(id);

        Common.checkNotNull(waybill);

        Common.checkNotNull(weight, "入库重不能为空");
        Common.checkNotNull(storageNo, "仓储位不能为空");

        waybill.setWeight(weight);
        waybill.setStorageNo(storageNo);
        waybill.setExtra_cost(extraCost);
        waybill.setExtra_cost_reason(extraCostReason);
        waybill.setTrackingNumber(Common.generateTranCode());
        waybill.setArrive(1);
        waybill.setInTime(new Date());
        if (waybill.getLogistics_status() != null && waybill.getLogistics_status().length() > 0) {
            waybill.setLogistics_status(waybill.getLogistics_status() + "\n包裹已签收（" + Common.dateConvertString(new Date()) + "）");
        } else {
            waybill.setLogistics_status("包裹已签收（" + Common.dateConvertString(new Date()) + "）");
        }

        waybillService.save(waybill);
        messageService.sendInMessage(waybill.getUserId());
        emailService.sendInEmail(waybill.getUsername());

        redirectAttributes.addFlashAttribute("success", "入库成功！");
        return "redirect:/waybill/" + waybill.getId() + "/show";
    }

    @RequestMapping(value = "/{id}/send", method = RequestMethod.GET)
    public String getSend(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Waybill waybill = waybillService.get(id);

        model.addAttribute("waybill", waybill);

        return "waybill/send";
    }

    @RequestMapping(value = "/{id}/send", method = RequestMethod.POST)
    public String postSend(
            @PathVariable("id") Integer id,
            @RequestParam("outWeight") BigDecimal outWeight,
            RedirectAttributes redirectAttributes
    ) throws BaseException {
        Common.checkNotNull(outWeight, "出库重不能为空");

        Waybill waybill = waybillService.get(id);
        waybill.setOutWeight(outWeight);
        waybill.setOutExpress(" ");
        waybill.setIsOurGood("已发货");
        waybill.setIsOurGoodId(1);
        waybill.setArrive(5);
        waybill.setOutTime(new Date());

        if (waybill.getLogistics_status() != null && waybill.getLogistics_status().length() > 0) {
            waybill.setLogistics_status(waybill.getLogistics_status() + "\n正在发往中国（" + Common.dateConvertString(new Date()) + "）");
        } else {
            waybill.setLogistics_status("正在发往中国（" + Common.dateConvertString(new Date()) + "）");
        }

        waybillService.save(waybill);

        redirectAttributes.addFlashAttribute("success", "发货成功！");

        return "redirect:/waybill/index?s=3";
    }

    @RequestMapping(value = "/{id}/mend", method = RequestMethod.GET)
    public String getMend(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Waybill waybill = waybillService.get(id);

        Common.checkNotNull(waybill);

        model.addAttribute("waybill", waybill);
        return "waybill/mend";
    }

    @RequestMapping(value = "/{id}/mend", method = RequestMethod.POST)
    public String postMend(
            @PathVariable("id") Integer id,
            @RequestParam("mendcost") BigDecimal mendcost,
            @RequestParam("mendReason") String mendReason,
            @RequestParam("outweight") BigDecimal outWeight,
            RedirectAttributes redirectAttributes
    ) throws BaseException {
        Waybill waybill = waybillService.get(id);

        Common.checkNotNull(waybill);
        Common.checkNotNull(mendcost, "补差价不能为空");
        Common.checkNotNull(mendReason, "补差价原因不能为空");
        Common.checkNotNull(outWeight, "出库重不能为空");

        waybill.setOutWeight(outWeight);
        waybill.setArrive(4);

        waybill.setMendCost(mendcost);
        waybill.setMend_reason(mendReason);


        BigDecimal allCost = new BigDecimal(0);
        allCost = allCost.add(waybill.getCost());
        allCost = allCost.add(waybill.getInsuranceCost());
        allCost = allCost.add(waybill.getMendCost());
        allCost = allCost.add(waybill.getExtra_cost());

        waybill.setAllCost(allCost);
        waybill.setPaymentStatus("unpaid");


        //补差价为负数，就立刻退钱
        if (mendcost.compareTo(new BigDecimal(0)) < 0) {
            User user = waybill.getUser();
            System.out.println(mendcost.abs());
            user.setMoney(user.getMoney().add(mendcost.abs()));

            waybill.setArrive(5);
            waybill.setPaymentStatus("Paid");
            waybill.setOutExpress(" ");
            waybill.setIsOurGood("已发货");
            waybill.setIsOurGoodId(1);
            waybill.setArrive(5);
            waybill.setOutTime(new Date());

            if (waybill.getLogistics_status() != null && waybill.getLogistics_status().length() > 0) {
                waybill.setLogistics_status(waybill.getLogistics_status() + "\n正在发往中国（" + Common.dateConvertString(new Date()) + "）");
            } else {
                waybill.setLogistics_status("正在发往中国（" + Common.dateConvertString(new Date()) + "）");
            }

            userService.update(user);
//            waybill.setLogistics_status(waybill.getLogistics_status() + "\n付款完成，转运中（" + Common.dateConvertString(new Date()) + "）");
        } else {
//            waybill.setLogistics_status(waybill.getLogistics_status() + "\n运单需要补差，请及时支付");
            messageService.sendInMessage(waybill.getUserId());
            emailService.sendMendEmail(waybill.getUsername());
        }

        waybillService.save(waybill);

        redirectAttributes.addFlashAttribute("success", "补差价成功！");

        return "redirect:/waybill/index?s=3";
    }

    @RequestMapping(value = "/{id}/destroy", method = RequestMethod.POST)
    @ResponseBody
    public String postDestroy(
            @PathVariable("id") Integer id
    ) {
        waybillService.destroy(id);
        return MessageHelper.toSuccess();
    }

    @RequestMapping(value = "/{id}/update/lg", method = RequestMethod.GET)
    public String getUpdateLg(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Waybill waybill = waybillService.get(id);
        Common.checkNotNull(waybill);

        model.addAttribute("waybill", waybill);
        return "/waybill/update_lg";
    }

    @RequestMapping(value = "/{id}/update/lg", method = RequestMethod.POST)
    public String postUpdateLg(
            @PathVariable("id") Integer id,
            @ModelAttribute("status") Integer s,
            @RequestParam("logisticsStatus") String logisticsStatus,
            @RequestParam("isourgood") String isourgood,
            RedirectAttributes redirectAttributes
    ) {
        Waybill waybill = waybillService.get(id);
        Common.checkNotNull(waybill);



        if (!waybill.getArrive().equals(5)) {
            throw new NotFoundException("运单ID:" + waybill.getId() + " 不是【5】状态");
        }

        waybill.setLogistics_status(logisticsStatus);
        waybill.setIsOurGood(isourgood);
        waybillService.save(waybill);

        redirectAttributes.addFlashAttribute("success", "更新物流信息成功！");

        return "redirect:/waybill/index?s=" + s;
    }

    @RequestMapping(value = "select", method = RequestMethod.GET)
    public String select(
            @RequestParam(value = "trackingNumber[]", required = false) String[] trackingNumber,
            @RequestParam(value = "tracking_number", required = false) String no,
            Model model
    ) {
        if (trackingNumber == null && no != null) {
            trackingNumber = Common.splitStringToArrayWithSpace(no);
        }
        List<Waybill> waybills = waybillService.findByTrackingnumberIn(trackingNumber);

        model.addAttribute("orders", waybills);
        return "waybill/select";
    }

    ///////// 导出物流信息
    @RequestMapping(value = "/export/lg", method = RequestMethod.GET)
    @ResponseBody
    public void exportLg(
            @RequestParam("tracking_number") String no,
            HttpServletResponse response
    ) throws BaseException {
        String[] trackingNumber = Common.splitStringToArrayWithSpace(no);
        List<Waybill> waybills = waybillService.findByTrackingnumberIn(trackingNumber);

        try {
            String filename = "物流信息.xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/msexcel");
            excelHelper.writeWaybill(waybills, response.getOutputStream());
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    //////////////导入物流信息
    @RequestMapping(value = "/import/lg", method = RequestMethod.GET)
    public String getImportLg() {
        return "waybill/import";
    }

    @RequestMapping(value = "/import/lg", method = RequestMethod.POST)
    public String postImportLg(
            @RequestParam("file") MultipartFile multipartFile,
            @ModelAttribute("status") Integer s,
            Model model
    ) {
        try {
            waybillService.importByExcel(multipartFile);
        } catch (BaseException e) {
            Common.logExceptionToFile(logger, e);
            model.addAttribute("message", e.getMessage());
            return "exception/error";
        }
        return "redirect:/waybill/index?s=" + s;
    }

    ////////////////////////


    @RequestMapping(value = "print", method = RequestMethod.GET)
    public String print(
            @RequestParam("tracking_number") String no,
            Model model
    ) {
        String[] trackingNumber = Common.splitStringToArrayWithSpace(no);
        List<Waybill> waybills = waybillService.findByTrackingnumberIn(trackingNumber);

        model.addAttribute("waybills", waybills);

        return "waybill/print";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String getUpdate(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Waybill waybill = waybillService.get(id);
        if (waybill == null) {
            throw new NotFoundException();
        }
        List<Brand> brands = waybillService.getAllBrand();

        model.addAttribute("brands", brands);
        model.addAttribute("waybill", waybill);
        return "waybill/update";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String postUpdate(
            @ModelAttribute("status") Integer s,
            @RequestParam(value = "id[]", required = false, defaultValue = "-1") Integer[] id,
            @RequestParam(value = "gooda[]", required = false) String[] gooda,
            @RequestParam(value = "goodb[]", required = false) String[] goodb,
            @RequestParam(value = "brandname[]", required = false) String[] brandname,
            @RequestParam(value = "goodname[]", required = false) String[] goodname,
            @RequestParam(value = "countnum[]", required = false) String[] countnum,
            RedirectAttributes redirectAttributes,
            @RequestParam("waybillId") Integer waybillId
    ) {
        waybillService.destroy(waybillId, id);
        if (
                Common.isNotEmpty(gooda) &&
                        Common.isNotEmpty(goodb) &&
                        Common.isNotEmpty(brandname) &&
                        Common.isNotEmpty(goodname) &&
                        Common.isNotEmpty(countnum)
                ) {
            if (gooda != null) {
                for (int i = 0; i < gooda.length; i++) {
                    if (i < id.length && id[i] != -1) {
                        Goods goods = waybillService.findGoodsById(id[i]);
                        if (goods == null) {
                            continue;
                        }
                        goods.setBrandname(brandname[i]);
                        goods.setContnum(countnum[i]);
                        goods.setGooda(gooda[i]);
                        goods.setGoodb(goodb[i]);
                        goods.setGoodname(goodname[i]);
                        waybillService.saveGoods(goods);
                    } else {
                        Goods goods = new Goods();
                        goods.setBrandname(brandname[i]);
                        goods.setContnum(countnum[i]);
                        goods.setGooda(gooda[i]);
                        goods.setGoodb(goodb[i]);
                        goods.setGoodname(goodname[i]);
                        goods.setFarherId(waybillId);
                        waybillService.saveGoods(goods);
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("success", "更新运单信息成功！");
        return "redirect:/waybill/index?s=" + s;
    }

    @RequestMapping(value = "/{id}/recover", method = RequestMethod.GET)
    public String recover(@PathVariable("id") Integer id, @ModelAttribute("status") Integer s, RedirectAttributes redirectAttributes) {
        Waybill waybill = waybillService.get(id);

        waybill.setArrive(1);
        waybillService.save(waybill);

        redirectAttributes.addFlashAttribute("success", "恢复成功！");
        return "redirect:/waybill/index?s=" + s;
    }

    @RequestMapping(value = "/{id}/show", method = RequestMethod.GET)
    public String show(
            @PathVariable("id") Integer id,
            @ModelAttribute("status") String s,
            Model model
    ) {
        Waybill waybill = waybillService.get(id);

        model.addAttribute("waybill", waybill);
        model.addAttribute("status", s);

        return "/waybill/show";
    }
}
