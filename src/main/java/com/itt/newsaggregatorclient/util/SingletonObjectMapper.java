package com.itt.newsaggregatorclient.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class SingletonObjectMapper {

    private SingletonObjectMapper() {}

    private static class Holder {
        private static final ObjectMapper INSTANCE = createMapper();

        private static ObjectMapper createMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper;
        }
    }

    public static ObjectMapper getInstance() {
        return Holder.INSTANCE;
    }
}

