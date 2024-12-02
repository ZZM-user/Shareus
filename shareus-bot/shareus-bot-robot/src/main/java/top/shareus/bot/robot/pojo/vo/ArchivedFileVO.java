package top.shareus.bot.robot.pojo.vo;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.shareus.bot.common.domain.ArchivedFile;

import java.util.Date;

/**
 * 归档文件VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivedFileVO {
	
	@ApiModelProperty(value = "id")
	private String id;
	
	@ApiModelProperty(value = "书名")
	private String title;
	
	@ApiModelProperty(value = "作者")
	private String author;
	
	@ApiModelProperty(value = "平台")
	private String platform;
	
	@ApiModelProperty(value = "链接")
	private String link;
	
	@ApiModelProperty(value = "发送人")
	private String sender;
	
	@ApiModelProperty(value = "是否对外展示 0展示 1不展示")
	private Integer enabled;
	
	@ApiModelProperty(value = "文件归档日期")
	private Date arachivedDate;
	
	public ArchivedFileVO(ArchivedFile archivedFile) {
		this.setId(archivedFile.getId());
		this.setTitle(archivedFile.getName());
		this.setAuthor(null);
		this.setPlatform(null);
		this.setLink(archivedFile.getArchiveUrl());
		this.setSender(Convert.toStr(archivedFile.getSenderId()));
		this.setEnabled(archivedFile.getEnabled());
		this.setArachivedDate(archivedFile.getArchiveDate());
	}
}
