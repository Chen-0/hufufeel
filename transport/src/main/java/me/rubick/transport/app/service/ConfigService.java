package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Config;
import me.rubick.transport.app.repository.ConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConfigService {

    @Resource
    private ConfigRepository configRepository;

    @Transactional(readOnly = true)
    public Map<String, String> findByKey(String key) {
        List<Config> configs = configRepository.findByKey(key);

        Map<String, String> map = new HashMap<>();

        for (Config config : configs) {
            map.put(config.getKey().trim(), config.getValue().trim());
        }

        return map;
    }
}
