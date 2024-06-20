package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapPageDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import java.util.List;

public interface TechmapService {
    Techmap create(UserDetailsImpl userDetails, TechmapCreateDto techmapCreateDto);

    TechmapPageDto findtechmaps(UserDetailsImpl userDetails, TechmapFindDto techmapFindDto);

    Techmap update(UserDetailsImpl userDetails, Integer techmapId, TechmapUpdateDto techmapUpdateDto);

    void delete(UserDetailsImpl userDetails, List<Integer> techmapId);
}
