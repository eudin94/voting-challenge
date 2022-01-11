package com.comerlato.voting_challenge.repository.specification;

import com.comerlato.voting_challenge.entity.Associate;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Optional;

@Builder
public class AssociateSpecification implements Specification<Associate> {
    @Builder.Default
    private final transient Optional<String> cpf = Optional.empty();
    @Builder.Default
    private final transient Optional<String> name = Optional.empty();

    @Override
    public Predicate toPredicate(Root<Associate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        final var predicates = new ArrayList<Predicate>();
        cpf.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("cpf")), "%" + s.toLowerCase() + "%")));
        name.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("name")), "%" + s.toLowerCase() + "%")));
        criteriaQuery.distinct(true);
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
