package com.template.az.SecurityTemplate.common.repository;

import com.template.az.SecurityTemplate.common.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings(value = "all")
class EmptySpecificationRepository<T extends AbstractEntity, ID> implements Repository<T, ID>, JpaSpecificationExecutor<T> {

  @Override
  public Optional<T> findOne(final Specification<T> spec) {
    return Optional.empty();
  }

  @Override
  public List<T> findAll(final Specification<T> spec) {
    return null;
  }

  @Override
  public Page<T> findAll(final Specification<T> spec, final Pageable pageable) {
    return null;
  }

  @Override
  public List<T> findAll(final Specification<T> spec, final Sort sort) {
    return null;
  }

  @Override
  public long count(final Specification<T> spec) {
    return 0;
  }

  @Override
  public boolean exists(Specification<T> spec) {
    return false;
  }

  @Override
  public long delete(Specification<T> spec) {
    return 0;
  }

  @Override
  public <S extends T, R> R findBy(Specification<T> spec, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }
}