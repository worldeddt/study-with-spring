package aop.prototypes.redis.convertor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JsonSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> clazz;

    public JsonSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (Exception e) {
            throw new SerializationException("Error serializing object to JSON: " + t, e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing JSON: " + new String(bytes), e);
        }
    }
}
