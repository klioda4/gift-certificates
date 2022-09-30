package ru.clevertec.ecl.repository.specification;

import javax.persistence.criteria.Path;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.GiftCertificate_;
import ru.clevertec.ecl.model.Tag_;

@UtilityClass
public class GiftCertificateSpecifications {

    public Specification<GiftCertificate> isTagPresentOptional(String tagName) {
        return (root, query, criteriaBuilder) -> {
            if (tagName == null) {
                return null;
            }
            Path<String> tagNameField = root.join(GiftCertificate_.tags)
                .get(Tag_.name);
            return criteriaBuilder.equal(tagNameField, tagName);
        };
    }

    public Specification<GiftCertificate> isNameLikeOptional(String nameSample) {
        return (root, query, criteriaBuilder) ->
            (nameSample == null)
                ? null
                : criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(GiftCertificate_.name)),
                    String.format("%%%s%%", nameSample.toLowerCase()));
    }

    public Specification<GiftCertificate> isDescriptionLikeOptional(String descriptionSample) {
        return (root, query, criteriaBuilder) ->
            (descriptionSample == null)
                ? null
                : criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(GiftCertificate_.description)),
                    String.format("%%%s%%", descriptionSample.toLowerCase()));
    }
}
