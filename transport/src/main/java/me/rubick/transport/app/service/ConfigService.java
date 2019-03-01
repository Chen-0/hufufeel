package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.Config;
import me.rubick.transport.app.repository.ConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class ConfigService {

    @Resource
    private ConfigRepository configRepository;

    public String findOneByKey(String key) {
        return findByKey(key).get(0);
    }

    @Transactional(readOnly = true)
    public List<String> findByKey(String key) {
        List<Config> configs = configRepository.findByKey(key);

        List<String> list = new ArrayList<>();

        for (Config config : configs) {
            list.add(config.getValue());
        }

        return list;
    }

    @Transactional(readOnly = true)
    public Map<String, String> findMapByKey(String key) {
        List<Config> configs = configRepository.findByKey(key);

        Map<String, String> map = new HashMap<>();

        for (Config config : configs) {
            map.put(config.getValue(), config.getComment());
        }

        log.info("{}", JSONMapper.toJSON(map));
        return map;
    }

    public void update(String key, String value) {
        configRepository.update(key, value);
    }
}
