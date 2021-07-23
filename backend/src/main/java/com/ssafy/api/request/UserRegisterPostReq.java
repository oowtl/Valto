package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 회원가입 API ([POST] /api/v1/users) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("UserRegisterPostRequest")
public class UserRegisterPostReq {
	@ApiModelProperty(name="유저 ID", example="ssafy_web")
	String userId;
	@ApiModelProperty(name="유저 Password", example="your_password")
	String password;
	@ApiModelProperty(name="유저 Name", example="홍길동")
	String name;
	@ApiModelProperty(name="유저 NickName", example="SSAFY")
	String nickName;
	int point;
	@Override
	public String toString() {
		return "UserRegisterPostReq [userId=" + userId + ", password=" + password + ", name=" + name + ", nickName="
				+ nickName + ", point=" + point + "]";
	}
	
	
}
