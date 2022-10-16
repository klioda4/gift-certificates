package ru.clevertec.ecl.util.spring.properties;

import java.util.Objects;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean yamlFactoryBean = new YamlPropertiesFactoryBean();
        yamlFactoryBean.setResources(resource.getResource());
        Properties properties = yamlFactoryBean.getObject();
        Objects.requireNonNull(properties, "Could not make properties from Yaml");

        String resourceName = (name == null) ? resource.getResource().getDescription() : name;

        return new PropertiesPropertySource(resourceName, properties);
    }
}
