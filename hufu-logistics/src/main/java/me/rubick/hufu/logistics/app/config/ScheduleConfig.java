package me.rubick.hufu.logistics.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import me.rubick.hufu.logistics.app.schedule.AnalysisData;

@Configuration
@EnableAsync
@EnableScheduling
//@ComponentScan(basePackageClasses = {EntityScan.class})
public class ScheduleConfig {

    @Bean
    public AnalysisData orderSchedule() {
        return new AnalysisData();
    }
}
