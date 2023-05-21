package top.shareus.bot.robot.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.constant.AlistConstant;
import top.shareus.bot.common.redis.service.RedisService;
import top.shareus.bot.robot.service.AlistService;

import java.io.File;
import java.util.HashMap;

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

//    public static void main(String[] args) {
//        ArrayList<String> pathList = new ArrayList() {{
////            add("/OneDrive/群文件/2022/2022.5");
////            add("/OneDrive/群文件/2022/06");
////            add("/OneDrive/群文件/2022/07");
////            add("/OneDrive/群文件/2022/08");
////            add("/OneDrive/群文件/2022/09");
////            add("/OneDrive/群文件/小说");
//        }};
//
//        ArchivedFileMapper mapper = MybatisPlusUtils.getMapper(ArchivedFileMapper.class);
//
//        pathList.forEach(path -> {
//            for (int i = 1; i < 50; i++) {
//                String body = ls(path, i);
//                Map map = JSONUtil.toBean(body, Map.class);
//                Map data = (Map) map.get("data");
////                Integer total = (Integer) data.get("total");
//                List<Map> content;
//                try {
//                    content = (List<Map>) data.get("content");
//                } catch (Exception e) {
//                    System.out.println(body);
//                    return;
//                }
//                if (content == null) {
//                    return;
//                }
//                content.forEach(c -> {
//                    String name = (String) c.get("name");
//                    ArchivedFile file = new ArchivedFile();
//                    file.setName(name);
//                    String modified = (String) c.get("modified");
//                    String format = LocalDateTimeUtil.format(LocalDateTime.parse(modified.substring(0, modified.length() - 1)), DatePattern.NORM_DATETIME_PATTERN);
//                    file.setArchiveDate(DateUtil.parse(format));
//                    file.setArchiveUrl(AlistConstant.DOMAIN + path + '/' + name);
//                    Integer size = (Integer) c.get("size");
//                    file.setSize(Long.valueOf(size));
//
//                    QueryWrapper<ArchivedFile> wrapper = new QueryWrapper<>();
//                    wrapper.eq("name", name);
//                    List<ArchivedFile> archivedFiles = mapper.selectList(wrapper);
//                    if (CollUtil.isEmpty(archivedFiles)) {
//                        mapper.insert(file);
//                    }
//                    System.out.println(file);
//                });
//            }
//
//        });
//    }
	
	/**
	 * 上传文件
	 *
	 * @param file 文件
	 *
	 * @return {@code String}
	 */
	@Override
	public String uploadFile(File file) {
		// 文件转二进制
		byte[] bytes = FileUtil.readBytes(file);
		String uploadPath = buildPathOfArchive(file.getName());
		// url编码路径
		String encodePath = URLEncodeUtil.encode(uploadPath);
		log.info("流：" + bytes.length + "\t编码串：" + encodePath);
		
		HttpResponse response = null;
		try {
			response = HttpRequest.put(AlistConstant.UPLOAD_FILE_API)
					.header("file-path", encodePath)
					.header("authorization", getAuthorization())
					.body(bytes)
					.execute().sync();
			
			log.info("上传文件 结束 " + response.body());
		} catch (Exception e) {
			System.out.println(e);
		}
		
		if (HttpStatus.HTTP_OK == response.getStatus()) {
			log.info(uploadPath + " 上传成功");
			return AlistConstant.DOMAIN + uploadPath;
		}
		
		throw new RuntimeException("Alist文件上传失败:" + uploadPath + "\t" + response.body());
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
			token = login().trim();
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
		
		HttpResponse response = HttpRequest.post(AlistConstant.LOGIN_API)
				.body(JSONUtil.toJsonPrettyStr(map), JSON)
				.execute().sync();
		
		if (HttpStatus.HTTP_OK == response.getStatus()) {
			String body = response.body();
			
			log.info("登录成功 " + body);
			return ReUtil.get(JWT_REGEX, body, 0).replace("\"", "");
		}
		
		throw new RuntimeException("Alist登录失败-获取token失败:" + response.body());
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
}
