package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01_basic.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void insertDummyData() {
        // given
        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRIC)
                .price(1000000)
                .build();

        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();

        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();

        Product p4 = Product.builder()
                .name("쓰레기")
                .category(FOOD)
                .build();

        //when
        Product saved1 = productRepository.save(p1);
        Product saved2 = productRepository.save(p2);
        Product saved3 = productRepository.save(p3);
        Product saved4 = productRepository.save(p4);

    }

    @Test
    @DisplayName("5번째 상품을 데이터 베이스에 저장해야 한다.")
    void saveTest() {

        Product product = Product.builder()
                .name("정장")
                .price(1200000)
                .category(FASHION)
                .build();

        Product saved = productRepository.save(product);

        assertNotNull(saved);
    }


    @Test
    @DisplayName("id가 2번인 데이터를 삭제해야 한다.")
    void removeTest() {
        //given
        Long id = 2L;

        //when
        productRepository.deleteById(id);

        //then

    }

    @Test
    @DisplayName("상품 전체조회를 하면 상품의 갯수가 4개여야 한다.")
    void findAllTest() {
        //given

        //when
        List<Product> list = productRepository.findAll();
        list.forEach(System.out::println);
        //then
        assertEquals(list.size(), 4);
    }

    @Test
    @DisplayName("3번 상품을 조회하면 상품명이 구두이다")
    void findOneTest() {

        //given
        Long id = 3L;

        //when
        Optional<Product> product = productRepository.findById(id);

        //then
        product.ifPresent(p -> {
            assertEquals("구두", p.getName());
        });

        Product product1 = product.get();
        assertNotNull(product1);

        System.out.println("product1 = " + product1);

    }

    @Test
    @DisplayName("2번 상품의 이름과 가격을 변경해야 한다.")
    void updateTest() {
        //given
        Long id = 2L;
        String newName = "짜장면";
        int newPrice = 6000;
        //when

//        productRepository.
        // jpa에서 update는 메서드를 따로 지원하지 않고
        // 조회한 후 setter 로 변경하면 자동으로 update 문이 나갑니다.
        // 변경 후 다시 save 를 호출
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> {
            p.setName(newName);
            p.setPrice(newPrice);

            productRepository.save(p);
        });

        assertTrue(product.isPresent());

        //then
        Product product1 = product.get();
        assertEquals("짜장면", product1.getName());
        assertEquals(6000, product1.getPrice());

    }

}