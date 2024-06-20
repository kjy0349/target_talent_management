package com.ssafy.s10p31s102be.board.infra.repository;

import com.ssafy.s10p31s102be.board.infra.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Integer> {
}
