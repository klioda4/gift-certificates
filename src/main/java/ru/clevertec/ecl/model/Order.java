package ru.clevertec.ecl.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clevertec.ecl.model.entityGraph.EntityGraphNames;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(
    name = EntityGraphNames.ORDER_WITH_CERTIFICATE,
    attributeNodes = @NamedAttributeNode(Order_.GIFT_CERTIFICATE))
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal cost;
    private Integer duration;
    private LocalDateTime purchaseDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
               optional = false)
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
               optional = false)
    private GiftCertificate giftCertificate;
}
