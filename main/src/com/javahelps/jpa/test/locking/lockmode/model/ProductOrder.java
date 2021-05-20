package com.javahelps.jpa.test.locking.lockmode.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_order")
@Getter
@Setter
public class ProductOrder {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String author;

     private Integer itemAmount;

     public ProductOrder(String author, Integer itemAmount) {
         this.author = author;
         this.itemAmount = itemAmount;
     }

     public ProductOrder() {
     }
}