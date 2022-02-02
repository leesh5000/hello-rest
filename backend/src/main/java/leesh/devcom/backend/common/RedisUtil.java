package leesh.devcom.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void set(String key, Object value, Long timeout, TimeUnit unit) {

        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            if (StringUtils.hasText(jsonValue)) {
                redisTemplate.opsForValue().set(key, jsonValue, timeout, unit);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public <T> T get(String key, Class<T> classType) throws Exception {
        String jsonValue = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(jsonValue)) {
            return null;
        } else {
            return objectMapper.readValue(jsonValue, classType);
        }
    }
}
