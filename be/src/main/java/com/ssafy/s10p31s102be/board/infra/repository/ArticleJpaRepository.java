package com.ssafy.s10p31s102be.board.infra.repository;

//import com.ssafy.s10p31s102be.admin.infra.entity.QArticle;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleJpaRepository extends JpaRepository<Article, Integer>, QuerydslPredicateExecutor<Article> {
    default Page<Article> findArticles(Pageable pageable){
        return findAll(pageable);
    }
}
