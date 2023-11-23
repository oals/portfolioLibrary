package com.project.library.service;

import com.project.library.dto.PageRequestDTO;
import com.project.library.dto.PageResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class PagingServiceImpl implements PagingService{
    @Override
    public PageResponseDTO paging(PageRequestDTO pageRequestDTO, int resultCount) {


        if (resultCount <= 0)
            return new PageResponseDTO();

        int page = pageRequestDTO.getPage();
        int size = pageRequestDTO.getSize();

        int total = resultCount;

        // n block에대한 시작페이지, 끝 페이지 계산
        // 1~10 => 0.1...1.0 => 1 => *10 => 10 - 9 => 1
        int end = (int)(Math.ceil(page/10.0))*10;
        int start = end - 9;

        // 이전, 다음 링크 활성화 처리를 위한 계산
        int last = (int)(Math.ceil((total/(double)size)));
        end = end > last ? last : end;

        boolean prev = start > 1;// true이면 이전링크를 활성화
        boolean next = total > end * size;// true이면 다음링크를 활성화


        PageResponseDTO pageResponseDTO = new PageResponseDTO();
        pageResponseDTO.setEnd(end);
        pageResponseDTO.setPage(page);
        pageResponseDTO.setNext(next);
        pageResponseDTO.setPrev(prev);
        pageResponseDTO.setStart(start);
        pageResponseDTO.setTotal(total);
        pageResponseDTO.setSize(size);



        return pageResponseDTO;
    }
}
