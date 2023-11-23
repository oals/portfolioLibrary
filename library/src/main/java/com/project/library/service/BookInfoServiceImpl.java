package com.project.library.service;

import com.project.library.dto.BookInfoDTO;
import com.project.library.entity.BookInfo;
import com.project.library.repository.BookInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookInfoServiceImpl implements BookInfoService{

    private final BookInfoRepository bookInfoRepository;

    private final ModelMapper modelMapper;

    @Override
    public BookInfoDTO selectBookInfo(String bookName) {


       BookInfo bookInfo = bookInfoRepository.findById(bookName).orElseThrow();

       BookInfoDTO bookInfoDTO = modelMapper.map(bookInfo,BookInfoDTO.class);


        return bookInfoDTO;
    }
}
