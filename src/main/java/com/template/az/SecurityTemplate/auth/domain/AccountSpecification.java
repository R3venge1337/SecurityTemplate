package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.AccountFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.template.az.SecurityTemplate.common.repository.PredicateUtils.addEqualPredicate;
import static com.template.az.SecurityTemplate.common.repository.PredicateUtils.addLikePredicate;
import static com.template.az.SecurityTemplate.common.repository.PredicateUtils.buildAndPredicates;

@RequiredArgsConstructor
class AccountSpecification implements Specification<Account> {

    private final AccountFilterForm filterForm;

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        addLikePredicate(criteriaBuilder, predicates, root.get(Account.Fields.username), filterForm.nickname());
        addLikePredicate(criteriaBuilder, predicates, root.get(Account.Fields.email), filterForm.email());

        addEqualPredicate(criteriaBuilder, predicates, root.get(Account.Fields.enabled), filterForm.isActive());
        addEqualPredicate(criteriaBuilder, predicates, root.get(Account.Fields.roles).get("name"), filterForm.role());
        return buildAndPredicates(criteriaBuilder, predicates);
    }
}
