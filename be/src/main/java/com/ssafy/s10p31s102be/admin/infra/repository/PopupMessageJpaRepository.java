package com.ssafy.s10p31s102be.admin.infra.repository;

import com.ssafy.s10p31s102be.admin.infra.entity.PopupMessage;
import com.ssafy.s10p31s102be.admin.infra.entity.QPopupMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PopupMessageJpaRepository extends JpaRepository<PopupMessage, Integer>, QuerydslPredicateExecutor<PopupMessage> {
    default Page<PopupMessage> findPopupMessages(Pageable pageable){
        return findAll(pageable);
    }
}
