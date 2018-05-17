package me.rubick.hufu.logistics.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.rubick.hufu.logistics.app.model.Finance;
import me.rubick.hufu.logistics.app.model.Paymoney;
import me.rubick.hufu.logistics.app.model.User;
import me.rubick.hufu.logistics.app.model.UserStatistics;
import me.rubick.hufu.logistics.app.repository.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class AnalysisService {

    @Resource
    private PaymoneyRepository paymoneyRepository;

    @Resource
    private FinanceRepository financeRepository;

    @Resource
    private UserStatisticsRepository userStatisticsRepository;

    @Resource
    private WaybillRepository waybillRepository;

    @Resource
    private UserRepository userRepository;

    public List<UserStatistics> getUserStatistics(Integer year, Integer month) {

        Integer time = getThisMonth(year, month);

        return userStatisticsRepository.findByTime(time);
    }


    public synchronized void analysisData(int year, int month) {
        List<User> users = userRepository.findAll();


        Integer time = getThisMonth(year, month);

        for (User user : users) {
            UserStatistics userStatistics = findOrNewByUserIdAndTime(user.getId(), time);
//            System.out.println("aaa:" + userStatistics);

            Double in = paymoneyRepository.sumMoneyByUserIdAndTime(user.getId(), year, month);
            if (in == null) {
                in = 0D;
            }
            userStatistics.setIn(new BigDecimal(in));

            Double out = financeRepository.sumCostByUserIdAndTime(user.getId(), year, month);
            if (out == null) {
                out = 0D;
            }
            userStatistics.setOut(new BigDecimal(out));

            Integer totalWaybill = waybillRepository.countByUserIdAndTime(user.getId(), year, month);
            if (totalWaybill == null) {
                totalWaybill = 0;
            }
            userStatistics.setTotalWaybill(totalWaybill);
//            System.out.println();
            userStatisticsRepository.save(userStatistics);
        }
    }

    private UserStatistics findOrNewByUserIdAndTime(Integer userId, Integer time) {
        UserStatistics userStatistics = userStatisticsRepository.findByUserIdAndTime(userId, time);

        if (userStatistics == null) {
            userStatistics = new UserStatistics();
            userStatistics.setUserId(userId);
            userStatistics.setTime(time);
            userStatistics.setIn(new BigDecimal(0));
            userStatistics.setOut(new BigDecimal(0));
            userStatistics.setTotalWaybill(0);
            userStatisticsRepository.save(userStatistics);
        }

        return userStatistics;
    }

    private Integer getThisMonth(Integer year, Integer month) {

        return year * 33 + month * 33 * 33;
    }

    public List<Paymoney> getPaymenyByUserIdAndTime(Integer userId, Integer year, Integer month) {
        return paymoneyRepository.getPaymenyByUserIdAndTime(userId, year, month);
    }

    public List<Finance> getFinanceByUserIdAndTime(Integer userId, Integer year, Integer month) {
        return financeRepository.getFinanceByUserIdAndTime(userId, year, month);
    }
}
