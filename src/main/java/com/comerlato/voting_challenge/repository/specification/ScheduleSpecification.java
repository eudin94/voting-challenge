package com.comerlato.voting_challenge.repository.specification;

import com.comerlato.voting_challenge.entity.Schedule;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Optional;

@Builder
public class ScheduleSpecification implements Specification<Schedule> {
    @Builder.Default
    private final transient Optional<String> subject = Optional.empty();
    @Builder.Default
    private final transient Optional<Boolean> closed = Optional.empty();

    @Override
    public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        final var predicates = new ArrayList<Predicate>();
        subject.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("subject")), "%" + s.toLowerCase() + "%")));
        closed.ifPresent(s -> predicates.add(root.get("closed").in(s)));
        criteriaQuery.distinct(true);
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
