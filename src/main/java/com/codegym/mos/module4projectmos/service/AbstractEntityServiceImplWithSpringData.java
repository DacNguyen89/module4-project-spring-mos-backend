package com.codegym.mos.module4projectmos.service;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public abstract class AbstractEntityServiceImplWithSpringData<E, I extends Serializable> {
    protected abstract CrudRepository<E, I> repository();
}
