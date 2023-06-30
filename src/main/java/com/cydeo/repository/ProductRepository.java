package com.cydeo.repository;


import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE p.category.company.id = ?1  order by p.category.description,p.name ")
    List<Product> findAllByCompanyAndProductNameSort(Long companyId);

}
