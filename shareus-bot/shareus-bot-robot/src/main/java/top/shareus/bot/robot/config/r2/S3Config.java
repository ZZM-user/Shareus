package top.shareus.bot.robot.config.r2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Configuration class for R2 credentials and endpoint
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "r2")
public class S3Config {
	
	private String accountId;
	private String accessKey;
	private String secretKey;
	private String endpoint;
	private String bucketName;
}