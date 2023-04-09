package application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

}
