package top.shareus.bot.robot.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankVO {
	
	@ApiModelProperty(value = "排名")
	private Integer rank;
	
	@ApiModelProperty(value = "用户名")
	private String username;
	
	@ApiModelProperty(value = "头像")
	private String avatar;
	
	@ApiModelProperty(value = "贡献值（百分比）")
	private String contribution;
	
	@ApiModelProperty(value = "贡献值（详细）")
	private Long contributionValue;
	
	public RankVO(Integer rank, String username, String contribution, Long contributionValue) {
		this.rank = rank;
		this.username = username;
		this.contribution = contribution;
		this.contributionValue = contributionValue;
	}
}

