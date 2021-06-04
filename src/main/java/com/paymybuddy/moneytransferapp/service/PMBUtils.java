package com.paymybuddy.moneytransferapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PMBUtils {

    public Page<?> transformListIntoPage(Pageable pageable, List<?> paramList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int pageFirstItem = currentPage * pageSize;

        List<?> list;

        if (paramList == null || paramList.size() < pageFirstItem) {
            list = Collections.emptyList();
        }
        else{
            int toIndex = Math.min(pageFirstItem + pageSize,paramList.size());
            list = paramList.subList(pageFirstItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), (paramList !=null) ? paramList.size() : 0);
    }
}
