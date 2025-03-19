package com.prac.ktb.comment.controller;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@Transactional
public class CommentController {


}
