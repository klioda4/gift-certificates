package ru.clevertec.ecl.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.projection.TagOfUser;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(
        value = "SELECT t.id      AS tagId,"
            + "         t.name    AS tagName,"
            + "         o.user_id AS userId"
            + "  FROM tag t"
            + "           INNER JOIN gift_certificate_tag gct ON t.id = gct.tag_id"
            + "           INNER JOIN gift_certificate gc ON gc.id = gct.gift_certificate_id"
            + "           INNER JOIN orders o ON gc.id = o.gift_certificate_id"
            + "  WHERE o.user_id = (SELECT u.id"
            + "                     FROM users u"
            + "                              INNER JOIN orders o ON u.id = o.user_id"
            + "                     GROUP BY u.id"
            + "                     ORDER BY sum(o.cost) DESC"
            + "                     LIMIT 1)"
            + "  GROUP BY t.id, t.name, o.user_id"
            + "  ORDER BY count(t.name) DESC"
            + "  LIMIT 1",
        nativeQuery = true)
    TagOfUser findMostUsedTagOfMostValuableUser();
}
