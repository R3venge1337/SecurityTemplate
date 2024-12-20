package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.FilterUserForm;
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
class UserSpecification implements Specification<User> {

    private final FilterUserForm filterUserForm;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        addLikePredicate(criteriaBuilder, predicates, root.get(User.Fields.firstName), filterUserForm.username());
        addLikePredicate(criteriaBuilder, predicates, root.get(User.Fields.secondName), filterUserForm.secondName());
        addLikePredicate(criteriaBuilder, predicates, root.get(User.Fields.surname), filterUserForm.surname());
        addLikePredicate(criteriaBuilder, predicates, root.get(User.Fields.yearOfBirth), filterUserForm.yearOfBirth());

        return buildAndPredicates(criteriaBuilder, predicates);
    }
}
