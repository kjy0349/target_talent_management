package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.exception.FileEmptyException;
import com.ssafy.s10p31s102be.common.exception.FileSizeOverException;
import com.ssafy.s10p31s102be.common.exception.FileTypeNotSupportedException;
import com.ssafy.s10p31s102be.common.util.FilesUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class FilesServiceImplTest {
    @Spy
    @InjectMocks
    FilesServiceImpl filesService;
//    @Mock
//    FilesRepository repository;
    @Mock
    FilesUtil filesUtil;


    /*
    @Override
    public void validateFile(MultipartFile multipartFile) {
        // 널 체크 () -> FileEmptyException
        // 파일 형식 체크 -> FileTypeNotSupportedException
        // image 사이즈 체크 50KB -> FileSizeOverException
    }

     */

    @Test
    public void 파일이_비었으면_FileEmptyException이_발생한다(){
        //given
        MultipartFile nullFile = null;

        //when
        //then
        assertThatThrownBy(()->{filesService.validateFile(nullFile);})
                .isInstanceOf(FileEmptyException.class);
    }

    @Test
    public void 파일이_지원하지_않는_형식이면_FileTypeNotSupportedException이_뜬다(){
        //given
        MultipartFile file = BDDMockito.mock(MultipartFile.class);
        given( file.getContentType()).willReturn("application/i'mWrongBoy");
        //when
        //then
        assertThatThrownBy(() -> filesService.validateFile(file))
                .isInstanceOf(FileTypeNotSupportedException.class);
    }

    @Test
    public void 이미지_크기가_50KB를_넘으면_FileSizeOverException이_발생한다(){
        //given
        MultipartFile file = BDDMockito.mock(MultipartFile.class);
        long overSize = 51 * 1024L;
        given( file.getSize() ).willReturn( overSize );
        given( file.getContentType()).willReturn("image/jpeg");
        //when
        //then
        assertThatThrownBy(() -> filesService.validateFile(file))
                .isInstanceOf(FileSizeOverException.class);
    }

    @Test
    void 파일_쓰기_성공() throws IOException {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", "test data".getBytes());
        String dirPath = "/image/png/";
        String filePath = dirPath + "test.png";

        given(filesService.createDirectoryPath(multipartFile)).willReturn(dirPath);

        File directory = mock(File.class);
        given(directory.exists()).willReturn(true);
        given(filesService.createDirectory(dirPath)).willReturn(directory);

        File file = mock(File.class);
        given(filesService.createFile(filePath)).willReturn(file);

        FileOutputStream fos = mock(FileOutputStream.class);
        //내부적으로 given은 호출 후에 반환값을 지정하는 것이고 doReturn().when()은 실제 메서드를 호출하지 않는다.
        //따라서 given으로 실행하면 NullPointerException이 터진다 -> null값으로 new FileOutputStream을 실행하기 때문.
        //        given( filesService.createFileOutputStream(any())).willReturn( fos );
        doReturn(fos).when(filesService).createFileOutputStream(file);

        // when
        filesService.writeFile(multipartFile);

        // then
        //호출 순서 검증
        InOrder inOrder = inOrder(filesService, directory, fos);
        inOrder.verify(filesService).validateFile(multipartFile);
        inOrder.verify(filesService).createDirectoryPath(multipartFile);
        inOrder.verify(filesService).createDirectory(dirPath);
        inOrder.verify(directory).exists();
        inOrder.verify(directory, never()).mkdirs();
        inOrder.verify(filesService).createFile(filePath);
        inOrder.verify(filesService).createFileOutputStream(file);
        inOrder.verify(filesService).writeToFileOutputStream(fos, multipartFile);
        inOrder.verify(filesService).closeFileOutputStream(fos);

        then(filesService).should(never()).createFileOutputStream(null);
        then(filesService).should(never()).writeToFileOutputStream(null, multipartFile);
        then(filesService).should(never()).closeFileOutputStream(null);
    }






}