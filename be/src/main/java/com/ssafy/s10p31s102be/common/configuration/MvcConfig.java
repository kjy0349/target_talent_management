package com.ssafy.s10p31s102be.common.configuration;

import com.ssafy.s10p31s102be.common.util.FilesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
    private final FilesUtil filesUtil;
    private final MockAuthenticationPrincipalArgumentResolver customHandlerMethodArgumentResolver;
    // 업로드 된 파일에 접근하여 처리하기 위한 ResourceHandler -> 혹시 모르니 캐시 적용을 위해 미리 세팅
    // 빈 등록된 path 반환 유틸 주입
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("upload/files/**").addResourceLocations("file:////" + filesUtil.getImgPath());
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        CacheControl staticCacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
//        CacheControl imgCacheControl = CacheControl.maxAge(20, TimeUnit.MINUTES);
//
////        registry.addResourceHandler("**/*.*").addResourceLocations("classpath:/static/").setCacheControl(staticCacheControl);
//        //registry.addResourceHandler("img/upload/**").addResourceLocations("file:////"+Utils.getImgPATHwithOS()+"upload"+ File.separator).setCacheControl(imgCacheControl);
//        //registry.addResourceHandler("img/static/**").addResourceLocations("file:////"+Utils.getImgPATHwithOS()+"static"+File.separator).setCacheControl(imgCacheControl);
//        registry.addResourceHandler( "upload/files/**" ).addResourceLocations("file:////" + filesUtil.getImgPath() ).setCacheControl(imgCacheControl );
//
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(customHandlerMethodArgumentResolver);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://k10s102.p.ssafy.io:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
