package top.shareus.bot.common.utils;

import java.security.SecureRandom;

public class RandomStringGeneratorUtil {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
	
	/**
	 * 随机生成指定长度字符
	 *
	 * @param length
	 *
	 * @return
	 */
	public static String generateRandomString(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			char randomChar = CHARACTERS.charAt(randomIndex);
			sb.append(randomChar);
		}
		
		return sb.toString();
	}
	
}