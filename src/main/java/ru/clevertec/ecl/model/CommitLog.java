package ru.clevertec.ecl.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ru.clevertec.ecl.util.enums.CommitLogStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class CommitLog extends PersistableEntity<Long> {

    @Id
    private Long id;

    private String httpMethod;
    private long entityId;
    private String entityName;
    private String body;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CommitLogStatus status = CommitLogStatus.CREATED;
}
