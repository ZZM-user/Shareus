package top.shareus.bot.robot.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchFileDTO {
	
	@ApiModelProperty(value = "搜索类型", notes = "空全部 1书名 2作者")
	private Integer type;
	
	@Length(max = 50, message = "搜索关键字长度不能超过50")
	@NotEmpty(message = "搜索关键字不能为空")
	@ApiModelProperty(value = "搜索关键字")
	private String keyword;
}
