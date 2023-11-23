package com.project.library.restController;


import com.project.library.dto.BookInfoDTO;
import com.project.library.service.BookHopeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookHopeRestController {


    private final BookHopeService bookHopeService;

    @PostMapping("/hopeBook")
    public boolean hopeBook(BookInfoDTO bookInfoDTO, Principal principal){

        boolean chk = bookHopeService.memberHopeBook(bookInfoDTO,principal.getName());

        return chk;

    }
}
