package ru.clevertec.ecl.util.mapping.custom.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.util.mapping.custom.EntityNameMapper;

@Component
public class EntityNameMapperImpl implements EntityNameMapper {

    private static final BiMap<String, String> controllerToEntityBindings;

    static {
        controllerToEntityBindings = new ImmutableBiMap.Builder<String, String>()
                                         .put("gift-certificates", "GiftCertificate")
                                         .put("orders", "Order")
                                         .put("tags", "Tag")
                                         .put("users", "User")
                                         .build();
    }

    @Override
    public String mapControllerBindingNameToEntityName(String controllerBindingName) {
        return controllerToEntityBindings.get(controllerBindingName);
    }

    @Override
    public String mapEntityNameToControllerBindingName(String entityName) {
        return controllerToEntityBindings.inverse().get(entityName);
    }
}
