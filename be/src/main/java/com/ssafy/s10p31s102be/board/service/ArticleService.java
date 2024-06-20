package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.ArticleCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.ArticleFindDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticlePageDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticleReadDto;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import java.util.List;

public interface ArticleService {
    Article create(UserDetailsImpl userDetails, ArticleCreateDto articleCreateDto);

    ArticlePageDto findArticles(UserDetailsImpl userDetails, ArticleFindDto articleFindDto);

    ArticleReadDto findArticleDetail(UserDetailsImpl userDetails, Integer boardId, Integer articleId);

    Article update(UserDetailsImpl userDetails, Integer articleId, ArticleCreateDto articleCreateDto);

    void delete(UserDetailsImpl userDetails, List<Integer> articleIds);
}
