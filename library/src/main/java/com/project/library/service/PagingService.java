package com.project.library.service;

import com.project.library.dto.PageRequestDTO;
import com.project.library.dto.PageResponseDTO;

public interface PagingService {

    PageResponseDTO paging(PageRequestDTO pageRequestDTO, int resultCount);


}
