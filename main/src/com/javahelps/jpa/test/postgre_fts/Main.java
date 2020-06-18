package com.javahelps.jpa.test.postgre_fts;

import com.javahelps.jpa.test.postgre_fts.entity.Article;
import com.javahelps.jpa.test.postgre_fts.service.ArticleService;
import com.javahelps.jpa.test.postgre_fts.util.TestDataInitializer;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.List;

public class Main {

    private static ArticleService articleService = new ArticleService();

    public static void main(String[] args) {

        TestDataInitializer.initArticle(articleService);
//        entityManager.clear();

        testSelect();
    }

    private static void testSelect() {
//        entityManager.getTransaction().begin();

//        List<Article> articles = entityManager.createQuery("FROM "+Article.class.getName(), Article.class).getResultList();

//        entityManager.getTransaction().commit();
    }

//    private static void printTitleByKeyWords(String keyWords) {
//        entityManager.getTransaction().begin();
//
//        List<String> titles = entityManager.createNativeQuery(
//                "SELECT a.title FROM article a WHERE to_tsvector(a.title) || to_tsvector(a.content) @@ plainto_tsquery(:keyWords);"
//        )
//                .setParameter("keyWords", keyWords)
//                .getResultList();
//
////        entityManager.createNativeQuery()
//    }
}
