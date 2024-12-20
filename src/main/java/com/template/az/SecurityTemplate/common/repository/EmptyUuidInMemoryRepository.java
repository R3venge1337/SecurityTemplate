package com.template.az.SecurityTemplate.common.repository;


import com.template.az.SecurityTemplate.common.entity.AbstractUUIDEntity;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings(value = "all")
public
class EmptyUuidInMemoryRepository<T extends AbstractUUIDEntity, ID> extends EmptyInMemoryRepository<T, ID> implements UUIDAwareJpaRepository<T, ID> {

    @Override
    public Optional<T> findByUuid(final UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUuid(final UUID uuid) {
        return false;
    }

    @Override
    public void deleteByUuid(final UUID uuid) {

    }
}