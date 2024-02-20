package top.shareus.bot.common.constant;

/**
 * Alist 常量
 *
 * @author 17602
 * @date 2022/11/19
 */
public class AlistConstant {
	
	/**
	 * 源站
	 */
	public static final String DOMAIN = "https://pan.shareus.top";
	// public static final String DOMAIN = "http://124.220.67.51:5244";
	
	/**
	 * 用户名
	 */
	public static final String USERNAME = "admin";
	
	/**
	 * 密码
	 */
	public static final String PASSWORD = "ZJL20010516";
	
	/**
	 * 登录api
	 */
	public static final String LOGIN_API = DOMAIN + "/api/auth/login";
	
	/**
	 * 登录token redis Key值
	 */
	public static final String AUTH_REDIS_KEY = "alist-auth-token";
	
	/**
	 * 群文件元数据密码 redis Key值
	 */
	public static final String GROUP_META_PWD_REDIS_KEY = "group_meta_pwd";
	
	/**
	 * 登录token redis 有效期
	 * 默认 48h，缩小一定时间为 47
	 */
	public static final Long AUTH_REDIS_EXPIRE = 45 * 3600L;
	
	/**
	 * 上传文件api
	 */
	public static final String UPLOAD_FILE_API = DOMAIN + "/api/fs/put";
	
	/**
	 * Alist 上传源路径
	 */
	public static final String UPLOAD_ALIST_PATH_DOMAIN = "/OneDrive/群文件/";
	
	/**
	 * 新建目录 api
	 */
	public static final String MKDIR_API = DOMAIN + "/api/fs/mkdir";
	public static final String LS_API = DOMAIN + "/api/fs/list";
	
	/**
	 * 更新meta资源信息
	 */
	public static final String ADMIN_META_UPDATE = DOMAIN + "/api/admin/meta/update";
}
