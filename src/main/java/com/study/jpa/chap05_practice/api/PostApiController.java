package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    // 게시물 등록
    @Parameters({
            @Parameter(name = "writer", description = "게시물의 작성자 이름을 쓰세요!", example = "김또띠", required = true)
            , @Parameter(name = "title", description = "게시물의 제목을 쓰세요!", example = "제목입니다", required = true)
            , @Parameter(name = "content", description = "게시물의 내용을 쓰세요!", example = "내용입니다")
            , @Parameter(name = "hashTags", description = "게시물의 해시태그를 나영해주세요.", example = "['해시태그2입니다','해시태그2입니다']")
    })
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

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;

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


    private static ResponseEntity<List<FieldError>> getValidateResult(BindingResult result) {
        if (result.hasErrors()) { // 입력값 검증에 걸림
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });

            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }


    // 게시물 수정
    @RequestMapping
    public ResponseEntity<?> update(
            @Validated @RequestBody PostModifyDTO dto
            , BindingResult result
            , HttpServletRequest request
    ) {

        log.info("/api/v1/posts {} - dto : {}!!", request.getMethod(), dto);

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;

        PostDetailResponseDTO responseDTO = postService.modify(dto);

        return ResponseEntity.ok(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        log.info("/api/v1/posts/{} DELETE !!", id);

        postService.delete(id);

        return ResponseEntity.ok("DEL SUCCESS!!");
    }

}
