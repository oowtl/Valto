package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 정보 수정 API ([PATCH] /api/v1/users/{userId}) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("UserModifyPatchRequest")
public class UserModifyPatchReq {
	@ApiModelProperty(name="유저 Name", example="홍길동")
	String name;
	@ApiModelProperty(name="유저 NickName", example="SSAFY")
	String nickName;
	@ApiModelProperty(name="유저 Password", example="your_password")
	String password;
}
