package com.javahelps.jpa.test.locking.lockmode.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String name = "Product";

     private String lastUpdater;

     private Integer itemAmount = 5;

     @Version
     private Integer version;

     public Product() {
     }

     public Product(String sallername) {
            this.lastUpdater = sallername;
        }
}