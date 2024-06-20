package com.ssafy.s10p31s102be.common.configuration;

import com.ssafy.s10p31s102be.common.util.MockAuthenticationPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MockAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Integer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        MockAuthenticationPrincipal annotation = methodParameter.getParameterAnnotation(MockAuthenticationPrincipal.class);
        Integer value = annotation.value();
        if (methodParameter.getParameterType().equals(Integer.class)) {
            return Integer.parseInt(String.valueOf(value));
        }
        return value;
    }
}
