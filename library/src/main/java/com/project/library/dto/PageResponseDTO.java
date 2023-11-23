package com.project.library.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Setter
public class PageResponseDTO {

    private int page;
    private int size;
    private int total;

    // block에대한 시작페이지 번호, 끈페이지번호 , 1block=>1~10, 2block=>11~20,...
    private int start;//시작 페이지 번호
    private int end;// 끝 페이지 번호

    private boolean prev;// 이전 페이지 존재 여부
    private boolean next;// 다음 페이지 존재 여부




}
