package me.rubick.hufu.logistics.app.schedule;


import me.rubick.hufu.logistics.app.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;

public class AnalysisData {

    @Resource
    private AnalysisService analysisService;

    private Logger logger = LoggerFactory.getLogger(AnalysisData.class);

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void run() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        logger.info("analysis {}-{} user data", year, month);
        analysisService.analysisData(year, month);
    }
}
