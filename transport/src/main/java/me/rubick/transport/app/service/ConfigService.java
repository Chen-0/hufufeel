package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Config;
import me.rubick.transport.app.repository.ConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConfigService {

    @Resource
    private ConfigRepository configRepository;

    @Transactional(readOnly = true)
    public List<String> findByKey(String key) {
        List<Config> configs = configRepository.findByKey(key);

        List<String> list = new ArrayList<>();

        for (Config config : configs) {
            list.add(config.getValue());
        }

        return list;
    }
}
