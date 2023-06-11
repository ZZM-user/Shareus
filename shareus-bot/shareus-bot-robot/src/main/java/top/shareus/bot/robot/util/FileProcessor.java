package top.shareus.bot.robot.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文件处理程序
 *
 * @author 17602
 * @date 2023/06/07
 */
@Slf4j
public class FileProcessor {
	
	public static final String DEFAULT_WATERMARK = """
												   ----------------------------分割线-------------------------
												     本文由深入海潮探海棠整理
												     有意者可加审核群：325459601
												     【附:文章源自网络，版权归原作者所有，不可用于收费】
												   ---------------------------分割线-------------------------
												     												    """;
	
	/**
	 * 插入默认水印
	 *
	 * @param file  文件
	 * @param times 次数
	 */
	public static void insertWatermark(File file, Integer times) {
		FileProcessor.insertWatermark(file, times, DEFAULT_WATERMARK);
	}
	
	/**
	 * 插入水印
	 *
	 * @param file       文件
	 * @param times      次数
	 * @param insertText 插入文本
	 */
	public static void insertWatermark(File file, Integer times, String insertText) {
		log.info("开始插入水印：{}", file.getName());
		
		try {
			String filePath = file.getAbsolutePath(); // 替换为你的文件路径
			
			if (isTxtFile(filePath)) {
				insertToText(times, insertText, filePath);
			} else if (isZipFile(filePath)) {
				insertToZip(times, insertText, filePath);
			} else {
				log.info("水印：未能处理的类型 {}", filePath);
			}
		} catch (Exception e) {
			log.error("{}->加水印处理失败：{}:{}", file.getAbsoluteFile(), e.getMessage(), e.getCause().getMessage());
		}
		
	}
	
	private static void insertToZip(Integer times, String insertText, String zipFilePath) {
		try {
			// 创建临时文件夹来解压缩
			Path tempFolder = Files.createTempDirectory("temp_unzip");
			
			// 解压缩压缩包文件到临时文件夹
			ZipUtil.unzip(zipFilePath, tempFolder.toString(), Charset.forName(getFileCharsetName(zipFilePath)));
			
			// 获取临时文件夹中的所有文件
			File[] files = new File(tempFolder.toString()).listFiles();
			
			if (files != null) {
				// 遍历每个文件并添加内容
				for (File file : files) {
					String absolutePath = file.getAbsolutePath();
					if (file.isFile() && isTxtFile(absolutePath)) {
						insertToText(times, insertText, absolutePath);
					}
				}
				
				// 将修改后的文件重新压缩为新的压缩包文件
				ZipUtil.zip(tempFolder.toString(), zipFilePath);
				
				log.debug("水印内容已成功添加到压缩包文件中。");
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 插入文本到文本文件
	 *
	 * @param times      插入次数
	 * @param insertText 插入的文本
	 * @param filePath   文件路径
	 */
	private static void insertToText(Integer times, String insertText, String filePath) {
		List<String> lines = readLines(filePath);
		
		boolean already = lines.parallelStream().anyMatch(DEFAULT_WATERMARK::contains);
		if (already) {
			return;
		}
		
		// 查找空行的索引
		List<Integer> emptyLineIndices = IntStream.range(0, lines.size())
				.filter(i -> lines.get(i).trim().isEmpty())
				.boxed()
				.collect(Collectors.toList());
		
		if (emptyLineIndices.size() < times) {
			log.debug("文件中的空行数量不足。");
			return;
		}
		
		Random random = new Random();
		
		// 随机插入文本
		for (int i = 0; i < times; i++) {
			int randomIndex = random.nextInt(emptyLineIndices.size());
			int insertIndex = emptyLineIndices.get(randomIndex);
			lines.add(insertIndex, insertText);
			emptyLineIndices.remove(randomIndex);
		}
		
		// 在头部和尾部添加文本
//		lines.add(0, insertText);
		lines.add(insertText);
		
		// 构建修改后的内容
		StringBuilder modifiedContent = new StringBuilder();
		for (String line : lines) {
			modifiedContent.append(line).append("\n");
		}
		
		// 将修改后的内容写回文件
		FileUtil.writeString(modifiedContent.toString(), filePath, StandardCharsets.UTF_8);
		
		System.out.println("文本已成功插入文件中。");
	}
	
	/**
	 * 读行
	 *
	 * @param filePath 文件路径
	 *
	 * @return {@link List}<{@link String}>
	 */
	private static List<String> readLines(String filePath) {
		try {
			String fileCharsetName = getFileCharsetName(filePath);
			List<String> lines = FileUtil.readLines(filePath, fileCharsetName);
			boolean ok = lines.parallelStream().filter(line -> line.length() > 0).noneMatch(line -> StrUtil.containsAny(line, "�", "拷锟"));
			if (ok) {
				return lines;
			}
		} catch (Exception e) {
			log.error("水印：获取文件编码错误{}", filePath);
		}
		
		log.error("编码检测：未知编码-{}", filePath);
		return new ArrayList<>();
	}
	
	/**
	 * 得到文件字符集名称
	 *
	 * @param filePath 文件路径
	 *
	 * @return {@link String}
	 *
	 * @throws Exception 异常
	 */
	private static String getFileCharsetName(String filePath) throws Exception {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == - 1) {
				//文件编码为 ANSI
				return charset;
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				//文件编码为 Unicode
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				//文件编码为 Unicode big endian
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				//文件编码为 UTF-8
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (! checked) {
				int loc = 0;
				while ((read = bis.read()) != - 1) {
					loc++;
					if (read >= 0xF0) {
						break;
					}
					if (0x80 <= read && read <= 0xBF) {
						// 单独出现BF如下的，也算是GBK
						break;
					}
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							// 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						} else {
							break;
						}
					} else if (0xE0 <= read && read <= 0xEF) {
						// 也有可能出错，可是概率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			log.error("解析文件编码错误：{}", e.getMessage());
			e.printStackTrace();
		}
		return charset;
	}
	
	// 判断文件是否为txt文件
	private static boolean isTxtFile(String filePath) {
		String extension = "";
		int index = filePath.lastIndexOf(".");
		if (index > 0) {
			extension = filePath.substring(index + 1);
		}
		
		return extension.equalsIgnoreCase("txt");
	}
	
	// 判断文件是否为压缩文件
	private static boolean isZipFile(String filePath) {
		
		String extension = "";
		int index = filePath.lastIndexOf(".");
		if (index > 0) {
			extension = filePath.substring(index + 1);
		}
		
		return StrUtil.equalsAnyIgnoreCase(extension, "zip", "rar", "7z");
	}
	
	
	public static void main(String[] args) {
//		File file = new File("C:\\Users\\17602\\Desktop\\叫妈妈 作者：故自山里来.txt");
//		FileProcessor.insertWatermark(file, 5);
		File zipFile = new File("C:\\Users\\17602\\Desktop\\压缩包.zip");
		insertWatermark(zipFile, 10);
	}
	
}
