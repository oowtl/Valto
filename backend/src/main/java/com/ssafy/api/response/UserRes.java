package com.ssafy.api.response;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserRecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 해당 페이지 권한 여부 API ([GET] /api/v1/users/me) 요청에 대한 응답값 정의.
 * 회정 정보 확인 API ([GET] /api/v1/users/myProfile) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserResponse")
public class UserRes{
	@ApiModelProperty(name="User ID")
	String userId;
	@ApiModelProperty(name="User NickName")
	String nickName;
	@ApiModelProperty(name="User Name")
	String name;
	@ApiModelProperty(name="User Point")
	int point;
	@ApiModelProperty(name="User Record")
	UserRecord userRecord;
	
	public static UserRes of(String userId) {
		UserRes res = new UserRes();
		res.setUserId(userId);		
		return res;
	}
	
	public static UserRes of(User user) {
		UserRes res = new UserRes();
		res.setUserId(user.getUserId());
		res.setNickName(user.getNickName());
		res.setName(user.getName());
		res.setPoint(user.getPoint());
		return res;
	}
}
