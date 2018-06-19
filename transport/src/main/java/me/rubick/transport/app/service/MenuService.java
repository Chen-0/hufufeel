package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Menu;
import me.rubick.transport.app.repository.MenuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MenuService {

    @Resource
    private MenuRepository menuRepository;

    private Map<String, List<Menu>> cache = new HashMap<>();

    public List<Menu> getMenu(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"sortIndex"));
        List<Menu> menus = menuRepository.findByMenuKey(key, new Sort(orders));
        cache.put(key, menus);
        return menus;
    }
}
