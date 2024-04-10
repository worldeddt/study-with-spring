package aop.prototypes.redis.cache.convertor;


import aop.prototypes.redis.cache.entities.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;

@Component
public class MemberToStringConverter implements Converter<Member, byte[]> {

    @Override
    public byte[] convert(Member value) {
        try {
            return toJson(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private byte[] toJson(Member member) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(member);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
