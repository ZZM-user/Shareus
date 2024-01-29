package top.shareus.bot.miniapp.pojo.dto;

import javax.validation.constraints.NotEmpty;

/**
 * 用户信息更新DTO
 *
 * @author 17602
 */
public record UpdateUserInfoDTO(
		@NotEmpty(message = "头像不能为空") String avatarUrl,
		String description,
		@NotEmpty(message = "昵称不能为空") String nickName) {
}
