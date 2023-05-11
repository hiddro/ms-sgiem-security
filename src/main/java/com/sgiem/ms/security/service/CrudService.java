package com.sgiem.ms.security.service;

import java.util.List;

public interface CrudService <T, ID> {
    T save(T t);

    T update (T t) throws Exception;

    List<T> findAll();

    T findById(ID id) throws Exception;

    void delete(ID id) throws Exception;
}
