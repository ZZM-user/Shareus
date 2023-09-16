package top.shareus.bot.robot.service;

import java.util.List;

/**
 * 邮件服务
 *
 * @author 17602
 * @date 2023-09-16
 */
public interface MailService {
	
	/**
	 * 1
	 *
	 * @param mailRecipient 邮件接收方
	 * @param subject       邮件主题
	 * @param messageText   HTML格式的邮件文本内容
	 *
	 * @description 发送简单文本邮件
	 */
	void sendSimpleMail(String mailRecipient, String subject, String messageText);
	
	/**
	 * 2
	 *
	 * @param mailRecipient 邮件接收方
	 * @param subject       邮件主题
	 * @param htmlText      HTML格式的邮件文本内容
	 *
	 * @description 发送HTML格式邮件
	 */
	void sendHtmlMail(String mailRecipient, String subject, String htmlText);
	
	
	/**
	 * 3
	 *
	 * @param mailRecipient 邮件接收方
	 * @param subject       邮件主题
	 * @param messageText   邮件文本内容
	 * @param filePathList  附件（文件路径集合）
	 *
	 * @description 发送包含附件的邮件
	 */
	void sendAppendixMail(String mailRecipient, String subject, String messageText, List<String> filePathList);
}
