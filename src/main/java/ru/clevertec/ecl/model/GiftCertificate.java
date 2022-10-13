package ru.clevertec.ecl.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import ru.clevertec.ecl.model.entityGraph.EntityGraphNames;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@NamedEntityGraph(
    name = EntityGraphNames.CERTIFICATE_WITH_TAGS,
    attributeNodes = @NamedAttributeNode(GiftCertificate_.TAGS))
@DynamicUpdate
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @EqualsAndHashCode.Exclude
    @Column(updatable = false)
    private LocalDateTime createDate;

    @EqualsAndHashCode.Exclude
    private LocalDateTime lastUpdateDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "gift_certificate_tag",
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "giftCertificate")
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    private void initDates() {
        createDate = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
    }

    @PreUpdate
    private void updateDates() {
        lastUpdateDate = LocalDateTime.now();
    }
}
