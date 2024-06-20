package com.ssafy.s10p31s102be.board.infra.repository;

import com.ssafy.s10p31s102be.board.infra.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Integer> {
    default Page<Comment> findComments(Pageable pageable){
        return findAll(pageable);
    }
}
