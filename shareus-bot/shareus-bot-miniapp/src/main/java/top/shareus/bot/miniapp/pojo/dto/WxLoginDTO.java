package top.shareus.bot.miniapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel("微信登录")
public class WxLoginDTO {
	private String code;
}
