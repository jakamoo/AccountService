package com.digitopia.caseStudy.controller;


import java.util.List;

public interface CrudController<T> {

    List<T> getAll();
/*
    T getById(Long id);

    T create(T entity);

    T update(Long id, T entity);

    void deleteById(Long id);*/
}