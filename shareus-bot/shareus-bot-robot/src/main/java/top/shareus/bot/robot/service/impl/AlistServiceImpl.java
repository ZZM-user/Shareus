package top.shareus.bot.robot.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.MessageSourceBuilder;
import net.mamoe.mirai.message.data.MessageSourceKind;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.constant.AlistConstant;
import top.shareus.bot.common.pojo.dto.ResetMetaPasswordDTO;
import top.shareus.bot.common.redis.service.RedisService;
import top.shareus.bot.common.utils.RandomStringGeneratorUtil;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.AlistService;
import top.shareus.common.core.exception.mirai.bot.BotException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Alist服务impl
 *
 * @author 17602
 * @date 2023/05/21
 */
@Slf4j
@Service
public class AlistServiceImpl implements AlistService {
	
	public static final String JSON = "application/json; charset=utf-8";
	/**
	 * 正则表达式
	 */
	private static final String JWT_REGEX = "\"(e.+?)\"";
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private RedissonClient redissonClient;
	@Autowired
	private GroupsConfig groupsConfig;
	
	/**
	 * 上传文件
	 *
	 * @param file 文件
	 *
	 * @return {@code String}
	 */
	@Override
	public String uploadFile(File file) {
		
		try {
			// 文件转二进制
			byte[] bytes = FileUtil.readBytes(file);
			String uploadPath = buildPathOfArchive(file.getName());
			// url编码路径
			String encodePath = URLEncodeUtil.encode(uploadPath);
			log.info("流：" + bytes.length + "\t编码串：" + encodePath);
			
			HttpResponse response = HttpRequest.put(AlistConstant.UPLOAD_FILE_API)
					.header("File-Path", encodePath)
					.header("Authorization", getAuthorization())
					.body(bytes)
					.execute().sync();
			
			log.info("上传文件 结束 " + response.body());
			
			if (HttpStatus.HTTP_OK == response.getStatus()) {
				log.info(uploadPath + " 上传成功");
				return AlistConstant.DOMAIN + uploadPath;
			}
			
			log.error("Alist文件上传失败:" + uploadPath + "\t" + response.body());
			throw new RuntimeException("Alist文件上传失败:" + uploadPath + "\t" + response.body());
		} catch (Exception e) {
			log.error("归档文件异常：{0}", e);
		}
		
		throw new RuntimeException();
	}
	
	/**
	 * 获取目录文件信息
	 *
	 * @param dir  dir
	 * @param page 页面
	 *
	 * @return {@link String}
	 */
	public String ls(String dir, Integer page) {
		return ls(dir, "", page, false);
	}
	
	/**
	 * 获取目录文件信息
	 *
	 * @param dir       dir
	 * @param password  密码
	 * @param page      页面
	 * @param isRefresh 是否刷新
	 *
	 * @return {@link String}
	 */
	public String ls(String dir, String password, Integer page, boolean isRefresh) {
		
		HashMap<String, Object> map = new HashMap() {{
			put("path", dir);
			put("password", password);
			put("page", page);
			put("per_page", 30);
			put("refresh", isRefresh);
		}};
		
		HttpResponse response = HttpRequest.post(AlistConstant.LS_API)
				.body(JSONUtil.toJsonPrettyStr(map), JSON)
				.header("authorization", getAuthorization())
				.execute().sync();
		
		if (HttpStatus.HTTP_OK == response.getStatus()) {
			log.info("获取文件夹：" + dir + "\n" + response.body());
			return response.body();
		}
		
		throw new RuntimeException("获取文件夹信息失败：" + dir);
	}
	
	/**
	 * 获取 Alist token
	 *
	 * @return {@code String}
	 */
	public String getAuthorization() {
		log.info("开始获取Alist授权");
		RLock lock = redissonClient.getLock("get-authorization");
		
		lock.lock();
		try {
			String token = redisService.get(AlistConstant.AUTH_REDIS_KEY);
			if (StrUtil.isNotEmpty(token)) {
				log.info("无需更新 token");
				return token;
			}
			log.info("需要更新 token");
			// token已经失效 需要重新登录 登录后再存到redis里
			token = login();
			log.info("获取Alist Token：" + token);
			
			redisService.set(AlistConstant.AUTH_REDIS_KEY, token, AlistConstant.AUTH_REDIS_EXPIRE);
			return token;
		} finally {
			lock.unlock();
		}
		
	}
	
	/**
	 * 登录
	 *
	 * @return {@code String}
	 */
	public String login() {
		// 构建请求体
		HashMap<String, String> map = new HashMap(2) {{
			put("username", AlistConstant.USERNAME);
			put("password", AlistConstant.PASSWORD);
		}};
		
		String token = "";
		HttpResponse response = null;
		try {
			log.warn(AlistConstant.LOGIN_API);
			response = HttpRequest.post(AlistConstant.LOGIN_API)
					.body(JSONUtil.toJsonPrettyStr(map), JSON)
					.execute();
			
			if (HttpStatus.HTTP_OK == response.getStatus()) {
				String body = response.body();
				
				log.info("登录成功 " + body);
				token = ReUtil.get(JWT_REGEX, body, 0).replace("\"", "");
			}
			
			if (CharSequenceUtil.isEmpty(token)) {
				log.error("登录失败 " + response.body());
			}
		} catch (Exception e) {
			log.error("Alist登录失败-获取token失败:{}", response.body());
			throw new BotException("Alist登录失败-获取token失败", e);
		} finally {
			if (ObjectUtil.isNotNull(response)) {
				response.close();
			}
		}
		
		return token.trim();
	}
	
	/**
	 * 创建目录
	 *
	 * @param dir dir
	 *
	 * @return {@code Boolean}
	 */
	private void mkdir(String dir) {
		HashMap<String, String> map = new HashMap(1) {{
			put("path", dir);
		}};
		
		HttpRequest.post(AlistConstant.MKDIR_API)
				.body(JSONUtil.toJsonPrettyStr(map), JSON)
				.header("authorization", getAuthorization())
				.execute().sync();
		
		log.info("创建文件夹：" + dir);
	}
	
	/**
	 * 构建 存档 路径
	 *
	 * @param fileName 文件名称
	 *
	 * @return {@code String}
	 */
	private String buildPathOfArchive(String fileName) {
		DateTime date = DateUtil.date();
		int year = date.year();
		int month = date.month();
		String archivedPath = AlistConstant.UPLOAD_ALIST_PATH_DOMAIN + year + "/" + (month + 1) + "/" + fileName.trim();
		
		// 每月、每年的第一天 创建一次目录
		if (date.dayOfMonth() == 1 || date.dayOfYear() == 1) {
			mkdir(archivedPath);
		}
		
		log.info("组建归档目录：" + archivedPath);
		return archivedPath;
	}
	
	@Override
	public void resetMetaPassword(ResetMetaPasswordDTO dto) {
		// 设计密码
		String password = dto.getPassword();
		if (CharSequenceUtil.isBlank(password)) {
			password = RandomStringGeneratorUtil.generateRandomString(8);
		}
		
		// 是否取消加密
		if (Boolean.TRUE.equals(dto.getCancelPassword())) {
			password = "";
		}
		
		// 构建结果集
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		map.put("p_sub", true);
		map.put("password", password);
		map.put("h_sub", false);
		map.put("header", "");
		map.put("header_sub", false);
		map.put("hide", "");
		map.put("path", "/OneDrive/群文件");
		map.put("r_sub", false);
		map.put("readme", "");
		map.put("w_sub", true);
		map.put("write", true);
		
		HttpResponse httpResponse = HttpRequest.post(AlistConstant.ADMIN_META_UPDATE)
				.body(JSONUtil.toJsonPrettyStr(map), JSON)
				.header("authorization", getAuthorization())
				.execute().sync();
		
		log.info("重置资源云盘密码结果：{}", httpResponse.body());
		
		String msg = "资源云盘密码已重置为：" + password;
		if (! httpResponse.isOk()) {
			msg = "重置资源云盘密码失败！";
		} else {
			redisService.set(AlistConstant.GROUP_META_PWD_REDIS_KEY, password);
		}
		
		// 结果通知
		String finalMsg = msg;
		Bot bot = BotManager.getBot();
		groupsConfig.getTest().forEach(groupId -> {
			bot.getGroup(groupId).sendMessage(finalMsg);
		});
		
		if (Boolean.TRUE.equals(dto.getNotifyAdminGroup())) {
			groupsConfig.getAdmin().forEach(groupId -> bot.getGroup(groupId).sendMessage(finalMsg));
		}
		
		if (Boolean.TRUE.equals(dto.getNotifyResourceGroup()) && httpResponse.isOk()) {
			groupsConfig.getRes().forEach(groupId -> {
				MessageReceipt<Group> messageReceipt = bot.getGroup(groupId).sendMessage(finalMsg);
				// 设置精华消息
				bot.getGroup(groupId).setEssenceMessage(new MessageSourceBuilder().allFrom(messageReceipt.getSource()).build(bot.getId(), MessageSourceKind.GROUP));
			});
		}
	}
}
