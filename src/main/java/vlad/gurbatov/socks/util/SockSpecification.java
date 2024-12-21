package vlad.gurbatov.socks.util;

import org.springframework.data.jpa.domain.Specification;
import vlad.gurbatov.socks.entity.Sock;

public class SockSpecification {

    private SockSpecification() {
    }

    public static Specification<Sock> equalColor(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("color")),
                        "%" + color.toUpperCase() + "%");
    }

    public static Specification<Sock> betweenCotton(Integer from, Integer to) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("cottonPercentage"), from, to);
    }

    public static Specification<Sock> lessThenCotton(Integer cottonPercentage) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("cottonPercentage"), cottonPercentage));
    }

    public static Specification<Sock> moreThenCotton(Integer cottonPercentage) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("cottonPercentage"), cottonPercentage));
    }

    public static Specification<Sock> equalCotton(Integer cottonPercentage) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cottonPercentage"), cottonPercentage));
    }

}
