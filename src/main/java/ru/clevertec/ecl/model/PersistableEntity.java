package ru.clevertec.ecl.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class PersistableEntity<ID> implements Persistable<ID> {

    @Getter
    @Transient
    @Builder.Default
    private boolean isNew = true;

    @PrePersist
    @PostLoad
    private void markNotNew() {
        isNew = false;
    }
}
