package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SingletonObjectMapper {

    private SingletonObjectMapper() {}

    private static class Holder {
        private static final ObjectMapper INSTANCE = createMapper();

        private static ObjectMapper createMapper() {
        	ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Fix for Instant
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    public static ObjectMapper getInstance() {
        return Holder.INSTANCE;
    }
}

