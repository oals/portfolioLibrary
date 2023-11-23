package com.project.library.restController;

import com.project.library.dto.*;
import com.project.library.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookRentalRestController {

    private final PagingService pagingService;
    private final BookRentalService bookRentalService;

    private final BookLikeService bookLikeService;

    private final BookHopeService bookHopeService;

    private final BookRentalReturnService bookRentalReturnService;

    private final BookInfoService bookInfoService;


    @PostMapping("/likeBook")
    public boolean likeBook(BookInfoDTO bookInfoDTO, Principal principal){

        boolean returnChk = bookRentalService.likeBook(bookInfoDTO,principal.getName());

        return returnChk;
    }


    @PostMapping("/rentalBook")
    public String rentalBook(BookInfoDTO bookInfoDTO,Principal principal){

        String returnChk= "";
        String countchk = bookRentalService.rentalCountChk(principal.getName());

        if(countchk.equals("대여 가능")) {
            returnChk = bookRentalService.rentalBook(bookInfoDTO, principal.getName());
            
        }else{
            returnChk = "최대 대여 개수 제한";
        }

        return returnChk;
    }


    @GetMapping("/likeBookHistory")
    public List<BookLikeHistoryDTO> likeBookHistory(Principal principal,PageRequestDTO pageRequestDTO){

        List<BookLikeHistoryDTO> list = bookLikeService.BookLikeHistory(principal.getName(),pageRequestDTO);

        return list;
    }


    @Transactional
    @Modifying
    @DeleteMapping("/likeBookDelete")
    public Long likeBookDelete(String bookName,Principal principal){


        Long count = bookLikeService.LikeBookDel(principal.getName(),bookName);


        return count;
    }

    @Transactional
    @Modifying
    @PostMapping("/likeBookRental")
    public Long likeBookRental(String bookName,Principal principal){



        String chk = bookRentalService.rentalCountChk(principal.getName());
        Long count = 1234L;

        if(chk.equals("대여 가능")){


            BookInfoDTO bookInfoDTO = bookInfoService.selectBookInfo(bookName);
            bookRentalService.rentalBook(bookInfoDTO,principal.getName());
            count = bookLikeService.LikeBookDel(principal.getName(),bookName);

        }

        return count;
    }


    @GetMapping("/rentalBookHistory")
    public List<BookRentalHistoryDTO> rentalBookHistory(Principal principal){



       List<BookRentalHistoryDTO>  list =  bookRentalService.rentalBookHistory(principal.getName());

       return list;
    }




    @GetMapping("/hopeBookHistory")
    public List<BookHopeHistoryDTO> hopeBookHistory(Principal principal, PageRequestDTO pageRequestDTO){

        List<BookHopeHistoryDTO> list = null;

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")) {
            list =  bookHopeService.bookHopeHistory("admin",pageRequestDTO);
        }else{
            list =  bookHopeService.bookHopeHistory(principal.getName(),pageRequestDTO);
        }


        return list;
    }


    @GetMapping("/searchBookPaging")
    public Object searchBook(PageRequestDTO pageRequestDTO, int resultCount){


        PageResponseDTO pageResponseDTO = pagingService.paging(pageRequestDTO,resultCount);

        return pageResponseDTO;

    }


    @Transactional
    @DeleteMapping("/returnRentalBook")
    public Long returnRentalBook(Principal principal,BookInfoDTO bookInfoDTO,boolean chk){


        bookRentalReturnService.returnBook(bookInfoDTO,principal.getName());

       Long count = bookRentalReturnService.rentalReturnCount(principal.getName(),chk);

        return count;
    }


    @GetMapping("/returnRentalBookHistory")
    public List<BookRentalReturnHistoryDTO> returnRentalBookHistory(Principal principal,PageRequestDTO pageRequestDTO,boolean chk){
        List<BookRentalReturnHistoryDTO>  list = null;

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ADMIN")){

            if(chk){
                list = bookRentalReturnService.rentalReturnHistory("admin",pageRequestDTO,true);
            }else{
                list = bookRentalReturnService.rentalReturnHistory("admin",pageRequestDTO,false);
            }

        }else{
            if(chk){
                list = bookRentalReturnService.rentalReturnHistory(principal.getName(),pageRequestDTO,true);
            }else{
                list = bookRentalReturnService.rentalReturnHistory(principal.getName(),pageRequestDTO,false);
            }

        }

        return list;
    }

    @Transactional
    @PutMapping("/returnCompleteBook")
    public List<Long> returnCompleteBook(String bookName,String memberId){

        bookRentalReturnService.rentalBookComplete(bookName,memberId);


        Long completeReturnBookCount = bookRentalReturnService.rentalReturnCount("admin",true);
        Long waitReturnBookCount = bookRentalReturnService.rentalReturnCount("admin",false);

        List<Long> list = new ArrayList<>();
        list.add(completeReturnBookCount);
        list.add(waitReturnBookCount);


        return list;

    }


    @Transactional
    @Modifying
    @DeleteMapping("/hopeBookDelete")
    public Long hopeBooKDelete(String bookName,Principal principal){

        Long count = bookHopeService.bookHopeDel(principal.getName(),bookName);

        return count;
    }




}
