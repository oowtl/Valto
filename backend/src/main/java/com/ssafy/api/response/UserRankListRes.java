package com.ssafy.api.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// 유저 랭킹 출력 API ([GET] /api/v1/users/rank/list) 요청에 대한 응답 값 정의

@Getter
@Setter
@ApiModel("UserRankListRes")
public class UserRankListRes extends BaseResponseBody{
	
	@ApiModelProperty(name="rank")
	ArrayList<HashMap> rank;
	
	public static UserRankListRes of(List<User> rankingList) {
		
		UserRankListRes res = new UserRankListRes();
		
		ArrayList<HashMap> rankListRes = new ArrayList<HashMap>();
		for (User user : rankingList) {
			HashMap<String, Object> userHashMap = new HashMap<>();
			
			userHashMap.put("userId", user.getUserId());
			userHashMap.put("nickName", user.getNickName());
			userHashMap.put("name", user.getName());
			userHashMap.put("point", user.getPoint());

			rankListRes.add(userHashMap);
		}
		
		res.setRank(rankListRes);
		return res;
	}
	
	
	
}
