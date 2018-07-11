package me.rubick.transport.app.service.cache;

import me.rubick.transport.app.vo.ProductContainer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class SimpleCacheService {
    private Map<Long, ProductContainer[]> map = new ConcurrentHashMap<>();

//    public void put(long id, )

    public ProductContainer[] get(long id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            map.put(id, new ProductContainer[]{new ProductContainer(), new ProductContainer()});
            return get(id);
        }
    }
}
