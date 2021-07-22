package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.service.ConferenceService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value ="방 API", tags = {"Conference"})
@RestController
@RequestMapping("api/v1/conference")
public class ConferenceController {
	@Autowired
	ConferenceService conferenceService;
	
	@GetMapping("/conference-categories")
	@ApiOperation(value="방 카테고리 조회", notes="방 카테고리를 불러온다.")
	@ApiResponses({
		 @ApiResponse(code = 200, message = "성공")
	})
	public ResponseEntity<? extends BaseResponseBody> getConferenceCategory(
			@ApiIgnore Authentication authentication){
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
	}
	
	@GetMapping("/conference")
	@ApiOperation(value="방 목록", notes="방 목록을 불러온다.")
	@ApiResponses({
		 @ApiResponse(code = 200, message = "성공")
	})
	public ResponseEntity<? extends BaseResponseBody> conferenceList(
			
			){
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
	}
	

}
