package com.paymybuddy.moneytransferapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PMBUtilsTest {

    @Autowired
    private PMBUtils pmbUtils;

    private final String item = "item1";
    private final List<String> stringList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        stringList.add(item);
    }

    @Test
    public void transformListIntoPageTest(){
        Page<?> expectedPage = new PageImpl<>(stringList, PageRequest.of(0,1),stringList.size());
        Page<?> resultPage = pmbUtils.transformListIntoPage(PageRequest.of(0,1),stringList);

        assertThat(resultPage).isEqualTo(expectedPage);
    }

    @Test
    public void transformListIntoPageWithEmptyListTest(){
        Page<?> resultPage = pmbUtils.transformListIntoPage(PageRequest.of(0,1),new ArrayList<>());

        assertThat(resultPage.getContent()).isEmpty();
    }

    @Test
    public void transformListIntoPageWithNullListTest(){
        Page<?> resultPage = pmbUtils.transformListIntoPage(PageRequest.of(0,1),null);

        assertThat(resultPage.getContent()).isEmpty();
    }
}
