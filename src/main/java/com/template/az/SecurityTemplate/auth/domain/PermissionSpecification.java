package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.PermissionFilterForm;
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
class PermissionSpecification implements Specification<Permission> {
    private final PermissionFilterForm filterForm;

    @Override
    public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        addLikePredicate(criteriaBuilder, predicates, root.get(Permission.Fields.name), filterForm.name());

        return buildAndPredicates(criteriaBuilder, predicates);
    }
}
