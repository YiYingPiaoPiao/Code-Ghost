package com.alicia.projects.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;

@Repository
public abstract class baseDao<T> {

    private final MongoTemplate mongoTemplate;
    private final Class<T> Clazz = (java.lang.Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected baseDao (

            MongoTemplate mongoTemplate
    ) {

        this.mongoTemplate = mongoTemplate;
    }
}
