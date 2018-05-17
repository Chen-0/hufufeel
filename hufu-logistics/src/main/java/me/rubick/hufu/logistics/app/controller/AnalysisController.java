package me.rubick.hufu.logistics.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import me.rubick.hufu.logistics.app.model.Finance;
import me.rubick.hufu.logistics.app.model.Paymoney;
import me.rubick.hufu.logistics.app.model.UserStatistics;
import me.rubick.hufu.logistics.app.service.AnalysisService;
import me.rubick.hufu.logistics.app.service.UserService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("analysis")
public class AnalysisController {

    @Resource
    private AnalysisService analysisService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/user/paymoney", method = RequestMethod.GET)
    public String analysisUserUsed(
            Model model,
            @RequestParam(value = "time", required = false, defaultValue = "") String time
    ) {
        int year, month;

        try {
            String[] timeSplit = time.split("/");
            year = Integer.valueOf(timeSplit[1]);
            month = Integer.valueOf(timeSplit[0]);
            if (!(month >= 1 && month <= 12)) {
                throw new Exception("时间不合法");
            }
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
        }


        List<UserStatistics> userStatistics = analysisService.getUserStatistics(year, month);

        model.addAttribute("users", userStatistics);
        model.addAttribute("time", time);
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "analysis/user/used";
    }

    @RequestMapping(value = "/user/paymoney/{userId}", method = RequestMethod.GET)
    public String showUserPaymoney(
            Model model,
            @PathVariable("userId") Integer userId,
            @RequestParam(value = "time", required = false, defaultValue = "") String time
    ) {
        int year, month;

        try {
            String[] timeSplit = time.split("/");
            year = Integer.valueOf(timeSplit[1]);
            month = Integer.valueOf(timeSplit[0]);
            if (!(month >= 1 && month <= 12)) {
                throw new Exception("时间不合法");
            }
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
        }


        List<Paymoney> paymonies = analysisService.getPaymenyByUserIdAndTime(userId, year, month);
        List<Finance> finances = analysisService.getFinanceByUserIdAndTime(userId, year, month);

        model.addAttribute("paymonies", paymonies);
        model.addAttribute("finances", finances);

        return "analysis/user/show";
    }
}
