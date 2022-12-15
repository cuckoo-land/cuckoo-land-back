package com.example.cuckoolandback.majority.controller;

import com.example.cuckoolandback.majority.dto.*;
import com.example.cuckoolandback.majority.service.MajorityService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MajorityController {


    private final MajorityService majorityService;


}
