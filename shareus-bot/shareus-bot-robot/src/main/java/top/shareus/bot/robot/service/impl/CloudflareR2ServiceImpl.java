package top.shareus.bot.robot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import top.shareus.bot.common.constant.AlistConstant;
import top.shareus.bot.robot.config.r2.CloudflareR2Client;
import top.shareus.bot.robot.config.r2.S3Config;
import top.shareus.bot.robot.service.CloudflareR2Service;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@Service
public class CloudflareR2ServiceImpl implements CloudflareR2Service {
	
	@Resource
	private S3Config s3Config;
	@Resource
	private CloudflareR2Client cloudflareR2Client;
	
	@Override
	public String uploadFile(File file) {
		String datePath = DateUtil.format(DateUtil.date(), "yyyy/MM/dd");
		// 构建文件的完整路径：datePath + 文件名
		String key = datePath + "/" + file.getName();
		
		S3Client client = cloudflareR2Client.getClient();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(s3Config.getBucketName())
				.key(key)
				.build();
		PutObjectResponse putObjectResponse = client.putObject(putObjectRequest, file.toPath());
		if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
			String alistPath = AlistConstant.DOMAIN + AlistConstant.UPLOAD_ALIST_PATH_DOMAIN + key;
			log.info("上传文件成功，文件路径：{}", alistPath);
			return alistPath;
		}
		log.error("上传文件失败，文件：{}, 反馈:{}", key, JSONUtil.toJsonStr(putObjectResponse));
		return null;
	}
}
