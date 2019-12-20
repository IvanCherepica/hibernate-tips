package com.javahelps.jpa.test;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import javafx.geometry.Pos;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.metamodel.relational.Database;

import javax.persistence.*;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.hibernate.cfg.AvailableSettings.*;
import static org.hibernate.jpa.AvailableSettings.JDBC_DRIVER;
import static org.hibernate.jpa.AvailableSettings.JDBC_URL;

public class Test {

    public static void main(String[] args) {
//        EntityManager entityManager = getEntityManager();
//
//        List<PostComment> postComments = new ArrayList<>();
//        postComments.add(new PostComment("1 comment"));
//        postComments.add(new PostComment("2 comment"));
//        postComments.add(new PostComment("3 comment"));
//
//        entityManager.getTransaction().begin();
//
////        for (PostComment postComment : postComments) {
////            entityManager.persist(postComment);
////        }
//
//        Post post = new Post("1 post");
//
//        entityManager.persist(post);
//
//        post.setTitle("2 post");
//
////        post.getComments().add(postComments.get(0));
////        post.getComments().add(postComments.get(1));
////        post.getComments().add(postComments.get(2));
//
////        entityManager.persist(post);
//
//        entityManager.getTransaction().commit();
//
//        System.out.println("Transaction close");

    }

//    private static EntityManager getEntityManager() {
//        Map<String, Object> options = new HashMap<>();
//        options.put(DRIVER, "com.mysql.jdbc.Driver");
//        options.put(URL, "jdbc:mysql://localhost:3306/world?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=UTC");
//        options.put(DIALECT, "org.hibernate.dialect.MySQL5Dialect");
//        options.put(USER, "root");
//        options.put(PASS, "root");
//        options.put(HBM2DDL_AUTO, "create");
//        options.put(SHOW_SQL, true);
//
//        Class[] persistentClasses = new Class[] {Post.class, PostComment.class};
//
//        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(persistentClasses);
//
//        EntityManagerFactory entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, options);
//
//        return entityManagerFactory.createEntityManager();
//    }
}
