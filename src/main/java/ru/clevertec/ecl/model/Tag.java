package ru.clevertec.ecl.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = "name")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Tag extends PersistableEntity<Long> {

    @Id
    private Long id;

    private String name;
}
