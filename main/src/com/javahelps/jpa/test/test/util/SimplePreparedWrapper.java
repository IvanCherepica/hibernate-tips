package com.javahelps.jpa.test.test.util;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class SimplePreparedWrapper<T> {

    private final Class<T> persistentClass;

    private EntityManager entityManager;

    private T result;

    private List<T> resilts;

    private TypedQuery<T> query;

    private EntityGraph<T> entityGraph;

    @SuppressWarnings("unchecked cast")
    public SimplePreparedWrapper() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public SimplePreparedWrapper(SimplePreparedWrapper<T> simplePreparedWrapper) {
        this.persistentClass = simplePreparedWrapper.persistentClass;
        this.entityManager = simplePreparedWrapper.entityManager;
        this.result = simplePreparedWrapper.result;
        this.resilts = simplePreparedWrapper.resilts;
        this.query = simplePreparedWrapper.query;
        this.entityGraph = simplePreparedWrapper.entityGraph;
    }

    public SimplePreparedWrapper(Class<T> persistentClass, EntityManager entityManager, T result, List<T> resilts, TypedQuery<T> query, EntityGraph<T> entityGraph) {
        this.persistentClass = persistentClass;
        this.entityManager = entityManager;
        this.result = result;
        this.resilts = resilts;
        this.query = query;
        this.entityGraph = entityGraph;
    }

    public SimplePreparedWrapper<T> addQuery(String query) {
        this.query = entityManager.createQuery(query, persistentClass);
        return this;
    }

    public SimplePreparedWrapper<T> addGraph() {
        this.entityGraph = entityManager.createEntityGraph(persistentClass);
        return new GraphPreparedWrapper<>(this);
    }

}
