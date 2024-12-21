package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.RoleFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.template.az.SecurityTemplate.common.repository.PredicateUtils.addLikePredicate;
import static com.template.az.SecurityTemplate.common.repository.PredicateUtils.buildAndPredicates;

@RequiredArgsConstructor
class RoleSpecification implements Specification<Role> {
    private final RoleFilterForm filterForm;

    @Override
    public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        addLikePredicate(criteriaBuilder, predicates, root.get(Role.Fields.name), filterForm.name());

        return buildAndPredicates(criteriaBuilder, predicates);
    }
}
