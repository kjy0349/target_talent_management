package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import java.util.List;

public interface ProfileColumnDataQueryDSLRepository{
    List<String> getSimilarName(UserDetailsImpl userDetails, String nameSearchString);
}
