package top.shareus.bot.miniapp.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @author 17602
 */
@Data
public class WxUserInfoVO {
	private Long id;
	private String nickName;
	private String avatarUrl;
	private String description;
	private Date lastLoginTime;
	private String token;
}
