package com.ssafy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserModifyPatchReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.response.UserRes;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserRecord;
import com.ssafy.db.repository.UserRepositorySupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping()
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.") 
    @ApiResponses({
        @ApiResponse(code = 201, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {
		System.out.println(registerInfo.toString());
		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		User user = userService.createUser(registerInfo);
		return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
	}
	
	@GetMapping("/me")
	@ApiOperation(value = "해당 페이지 권한 여부 확인", notes = "로그인한 회원 본인의 정보를 응답한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<UserRes> getAuth(@ApiIgnore Authentication authentication) {
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
//		System.out.println(authentication.getDetails().toString());
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
		return ResponseEntity.status(200).body(UserRes.of(user.getUserId()));
	}
	
	@GetMapping("/{userId}/id")
	@ApiOperation(value = "아이디 중복 검사 요청", notes = "회원가입 시 아이디 중복 여부를 응답한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "사용 가능한 아이디"),
        @ApiResponse(code = 409, message = "이미 존재하는 아이디"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> isDuplicateId(@PathVariable("userId") String userId){

		if(userId.equals(userService.getUserByUserId(userId).getUserId())) {
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "exist userId"));
		}
		
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "possible userId"));
	}
	
	@GetMapping("/{nickName}/nick")
	@ApiOperation(value = "닉네임 중복 검사 요청", notes = "회원가입 시 닉네임 중복 여부를 응답한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "사용 가능한 아이디"),
        @ApiResponse(code = 409, message = "이미 존재하는 아이디"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> isDuplicateNick(@PathVariable("nickName") String nickName){

		if(nickName.equals(userService.getUserByNickName(nickName).getNickName())) {
			return ResponseEntity.status(409).body(BaseResponseBody.of(409, "exist nickName"));
		}
		
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "possible nickName"));
	}
	
	@PatchMapping("/{userId}")
	@ApiOperation(value = "회원 정보 수정", notes = "해당 회원 정보를 수정한다.") 
    @ApiResponses({
        @ApiResponse(code = 201, message = "성공"),
        @ApiResponse(code = 403, message = "권한 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> modify(
			@ApiIgnore Authentication authentication, 
			@PathVariable("userId") String userId,
			@RequestBody @ApiParam(value="회원 수정 정보", required = true) UserModifyPatchReq modifyInfo){
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String inputUserId = userDetails.getUsername();
		if(inputUserId.equals(userId)) {
			User user = userService.modifyUser(modifyInfo, userId);
			return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
		}
		
		return ResponseEntity.status(403).body(BaseResponseBody.of(403, "권한 없음"));
		
	}
	
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "회원 정보 삭제", notes = "해당 회원 정보를 삭제한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 403, message = "권한 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> delete(@ApiIgnore Authentication authentication, @PathVariable("userId") String userId){
		
		
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String inputUserId = userDetails.getUsername();
		if(inputUserId.equals(userId)) {
			userService.deleteUser(userId);
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}
		
		return ResponseEntity.status(403).body(BaseResponseBody.of(403, "권한 없음"));
	}
	
	@GetMapping("/myprofile")
	@ApiOperation(value = "회원 정보 확인", notes = "해당 회원 정보를 확인한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 403, message = "권한 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<UserRes> getMyInfo(@ApiIgnore Authentication authentication){
				
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
//		UserRecord userRecord = userService.getUserByUserRecord(userId);
		
		return ResponseEntity.status(200).body(UserRes.of(user));
	}
	
	@GetMapping("/{userId}/profile")
	@ApiOperation(value = "회원 정보 확인", notes = "해당 회원 정보를 확인한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 403, message = "권한 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication, @PathVariable("userId") String userId){
				
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
//		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
//		UserRecord userRecord = userService.getUserByUserRecord(userId);
		
		return ResponseEntity.status(200).body(UserRes.opp(user));
	}
		
}
