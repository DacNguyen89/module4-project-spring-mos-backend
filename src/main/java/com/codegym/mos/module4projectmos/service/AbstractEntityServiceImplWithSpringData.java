package com.codegym.mos.module4projectmos.service;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractEntityServiceImplWithSpringData<E, I extends Serializable> {
    protected abstract CrudRepository<E, I> repository();

    public E save(E E) {
        return repository().save(E);
    }

    public E findOne(I id) {
        return repository().findById(id).orElse(null);
    }

    public List<E> findAll() {
        return streamAll().collect(Collectors.toList());
    }

    protected Stream<E> streamAll() {
        return StreamSupport.stream(repository().findAll().spliterator(), false);
    }

    protected Stream<E> streamAll(Iterable<E> Es) {
        return StreamSupport.stream(Es.spliterator(), false);
    }
}
