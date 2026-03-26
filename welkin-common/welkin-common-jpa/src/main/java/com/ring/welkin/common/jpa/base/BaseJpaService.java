package com.ring.welkin.common.jpa.base;

import com.ring.welkin.common.jpa.rsql.RSQLJPASupportService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BaseJpaService<T, ID> extends RSQLJPASupportService<T> {

    BaseJpaRepository<T, ID> getBaseJpaRepository();

    @Override
    default JpaSpecificationExecutor<T> getJpaSpecificationExecutor() {
        return getBaseJpaRepository();
    }

    default Page<T> findAll(Pageable pageable) {
        return getBaseJpaRepository().findAll(pageable);
    }

    default T save(T entity) {
        return getBaseJpaRepository().save(entity);
    }

    default T findById(ID id) {
        Optional<T> optional = getBaseJpaRepository().findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    default boolean existsById(ID id) {
        return getBaseJpaRepository().existsById(id);
    }

    default long count() {
        return getBaseJpaRepository().count();
    }

    default void deleteById(ID id) {
        getBaseJpaRepository().deleteById(id);
    }

    default void delete(T entity) {
        getBaseJpaRepository().delete(entity);
    }

    default void deleteAll(Iterable<T> entities) {
        getBaseJpaRepository().deleteAll(entities);
    }

    default void deleteAll() {
        getBaseJpaRepository().deleteAll();
    }

    default T findOne(Example<T> example) {
        Optional<T> optional = getBaseJpaRepository().findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    default Page<T> findAll(Example<T> example, Pageable pageable) {
        return getBaseJpaRepository().findAll(example, pageable);
    }

    default long count(Example<T> example) {
        return getBaseJpaRepository().count(example);
    }

    default boolean exists(Example<T> example) {
        return getBaseJpaRepository().exists(example);
    }

    default List<T> findAll() {
        return getBaseJpaRepository().findAll();

    }

    default List<T> findAll(Sort sort) {
        return getBaseJpaRepository().findAll(sort);
    }

    default List<T> findAllById(Iterable<ID> ids) {
        return getBaseJpaRepository().findAllById(ids);
    }

    default List<T> saveAll(Iterable<T> entities) {
        return getBaseJpaRepository().saveAll(entities);
    }

    default void flush() {
        getBaseJpaRepository().flush();
    }

    default T saveAndFlush(T entity) {
        return getBaseJpaRepository().saveAndFlush(entity);
    }

    default void deleteInBatch(Iterable<T> entities) {
        getBaseJpaRepository().deleteAllInBatch(entities);
    }

    default void deleteAllInBatch() {
        getBaseJpaRepository().deleteAllInBatch();
    }

    default T getOne(ID id) {
        return getBaseJpaRepository().getReferenceById(id);
    }

    default List<T> findAll(Example<T> example) {
        return getBaseJpaRepository().findAll(example);
    }

    default List<T> findAll(Example<T> example, Sort sort) {
        return getBaseJpaRepository().findAll(example, sort);
    }
}
