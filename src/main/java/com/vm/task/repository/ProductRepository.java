package com.vm.task.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vm.task.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}
