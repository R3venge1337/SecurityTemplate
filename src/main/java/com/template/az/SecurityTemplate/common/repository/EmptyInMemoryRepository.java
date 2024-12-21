package com.template.az.SecurityTemplate.common.repository;

import com.template.az.SecurityTemplate.common.entity.AbstractEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings(value = "all")
class EmptyInMemoryRepository<T extends AbstractEntity, ID> implements JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findAllById(final Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final ID id) {

    }

    @Override
    public void delete(final T entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends ID> ids) {

    }

    @Override
    public void deleteAll(final Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends T> S save(final S entity) {
        return null;
    }

    @Override
    public <S extends T> List<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<T> findById(final ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final ID id) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends T> S saveAndFlush(final S entity) {
        return null;
    }

    @Override
    public <S extends T> List<S> saveAllAndFlush(final Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(final Iterable<T> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<ID> ids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public T getOne(ID id) {
        return null;
    }

    @Override
    public T getById(ID id) {
        return null;
    }

    @Override
    public T getReferenceById(ID id) {
        return null;
    }

    @Override
    public <S extends T> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example, final Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends T> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

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