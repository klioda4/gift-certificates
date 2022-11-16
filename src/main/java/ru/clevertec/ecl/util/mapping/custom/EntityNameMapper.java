package ru.clevertec.ecl.util.mapping.custom;

public interface EntityNameMapper {

    String mapControllerBindingNameToEntityName(String controllerBindingName);

    String mapEntityNameToControllerBindingName(String controllerBindingName);
}
