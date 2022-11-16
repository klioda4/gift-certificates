package ru.clevertec.ecl.config.objects;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Value
@ConstructorBinding
@ConfigurationProperties("cluster")
@Validated
public class ClusterProperties {

    @NotNull
    List<List<String>> shards;

    @NotNull
    String currentNode;

    String urlPrefix;
}
