package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PostCreateDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;

    // 리소스 : 게시물 (Post)
    /*
         게시물 목록 조회:  /posts       - GET
         게시물 개별 조회:  /posts/{id}  - GET
         게시물 등록:      /posts       - POST
         게시물 수정:      /posts/{id}  - PATCH
         게시물 삭제:      /posts/{id}  - DELETE
     */

    @GetMapping()
    public ResponseEntity<?> list(PageDTO pageDTO) {
        log.info("/api/v1/posts?page={}&size={}", pageDTO.getPage(), pageDTO.getSize());

        PostListResponseDTO postLIstResponseDTO = postService.getPosts(pageDTO);

        return ResponseEntity
                .ok()
                .body(postLIstResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("api/v1/posts/{} GET", id);

        PostDetailResponseDTO dto = postService.getDetail(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto
            , BindingResult result) {
        log.info("/api/v1/post POST!! - payload : {}", dto);

        if (dto == null) {
            return ResponseEntity
                    .badRequest()
                    .body("게시물 정보를 전달해주세요!");
        }

        if (result.hasErrors()) { // 입력값 검증에 걸림
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });

            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }

        try {
            PostDetailResponseDTO responseDTO = postService.insert(dto);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body("서버 터짐 : " + e.getMessage());
        }

    }
}
