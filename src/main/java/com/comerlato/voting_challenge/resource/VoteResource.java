package com.comerlato.voting_challenge.resource;

import com.comerlato.voting_challenge.service.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Tag(name = "Votes")
public class VoteResource {

    private final VoteService service;
}
