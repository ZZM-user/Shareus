package top.shareus.bot.common.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 正常成员 VO
 *
 * @author zhaojl
 * @date 2023/01/08
 */
@Data
public class NormalMemberVO {
	private Long id;
	private String nameCard;
	private String nick;
	private Long groupId;
	private String groupName;
	private String specialTitle;
	private String avatarUrl;
	private Integer isMuted;
	private String muted;
	private Long muteTimeRemaining;
	private Long lastSpeakTimestamp;
	private Date lastSpeakTime;
	private Long joinTimestamp;
	private Date joinTime;
	private String remark;
	private String remark2;
	
}