package vlad.gurbatov.socks.util;

import org.springframework.data.jpa.domain.Specification;
import vlad.gurbatov.socks.entity.Sock;

public class SockSpecification {

    private SockSpecification() {
    }

    public static Specification<Sock> equalColor(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Sock> betweenCotton(Integer from, Integer to) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("cotton"), from, to);
    }

    public static Specification<Sock> lessThenCotton(Integer cotton) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("cotton"), cotton));
    }
    public static Specification<Sock> moreThenCotton(Integer cotton) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("cotton"), cotton));
    }

    public static Specification<Sock> equalCotton(Integer cotton) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cotton"), cotton));
    }

}
