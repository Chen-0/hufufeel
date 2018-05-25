package me.rubick.hufu.logistics.app.controller;

import me.rubick.hufu.logistics.app.service.CompanyOrderService;
import me.rubick.hufu.logistics.app.service.WaybillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import me.rubick.hufu.logistics.app.service.CompanyService;

import javax.annotation.Resource;

@Controller
public class IndexController {


    @Resource
    private CompanyOrderService companyOrderService;

    @Resource
    private WaybillService waybillService;

    @Resource
    private CompanyService companyService;

    @RequestMapping("/")
    public String index(Model model) {
        if (companyService.isAdminWithLogin()) {
            Double total1 = waybillService.getTotalAtMonth();
            Double total2 = companyOrderService.getTotalAtMonth();
            if (total1 == null) {
                total1 = 0D;
            }

            if (total2 == null) {
                total2 = 0D;
            }

            model.addAttribute("total1", total1);
            model.addAttribute("total2", total2);

            return "index";
        } else {
            return "redirect:/order/index";
        }
    }

//    @Resource
//    private FinanceRepository financeRepository;
//
//    @Resource
//    private WaybillRepository waybillRepository;
//
//    @RequestMapping("/test")
//    @ResponseBody
//    public String test() {
//        List<Finance> financeRepositories = financeRepository.findAll();
//
//        for (Finance finance : financeRepositories) {
//            if (finance.getUserId().equals(258)) {
//                List<Waybill> waybill = waybillRepository.findByCostAndUserid(finance.getCost(), 258);
//                if (waybill.size() > 0) {
////                    System.out.println(JSON.toJSON(waybill));
//
//                    System.out.println("T:" + waybill.get(0).getTrackingNumber() + "  F:" + finance.getTrackingNumber());
//                    finance.setTrackingNumber(waybill.get(0).getTrackingNumber());
//                    financeRepository.save(finance);
//                }
//
//            }
//        }
//        return  "OK";
//    }
}
