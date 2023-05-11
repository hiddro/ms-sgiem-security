package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.service.CrudService;

import java.util.List;

public abstract class CrudServiceImpl<T, ID> implements CrudService<T, ID> {

    protected abstract GenericRepositories<T, ID> getRepo();

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) throws Exception {
        return getRepo().findById(id).orElse(null);
    }

    @Override
    public void delete(ID id) throws Exception {
        getRepo().deleteById(id);
    }
}
