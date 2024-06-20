package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.dto.response.KeywordResponseDto;
import com.ssafy.s10p31s102be.common.dto.response.OptionResponseDto;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;

import com.ssafy.s10p31s102be.common.infra.enums.OptionType;
import java.util.List;

public interface KeywordService {
    List<KeywordResponseDto> readKeywords(KeywordType type, String word);

    List<OptionResponseDto> readOptions(OptionType type, String word);
}