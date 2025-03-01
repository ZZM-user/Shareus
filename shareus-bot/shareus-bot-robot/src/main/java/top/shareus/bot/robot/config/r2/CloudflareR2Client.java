package top.shareus.bot.robot.config.r2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import javax.annotation.Resource;
import java.io.Closeable;
import java.net.URI;

/**
 * Client for interacting with Cloudflare R2 Storage using AWS SDK S3 compatibility
 */
@Slf4j
@Component
public class CloudflareR2Client implements Closeable {
	
	@Resource
	private S3Config s3Config;
	
	private volatile S3Client s3Client;
	
	/**
	 * Creates a new CloudflareR2Client with the provided configuration
	 */
	public S3Client getClient() {
		if (this.s3Client == null) {
			synchronized (this) {
				if (this.s3Client == null) {
					this.s3Client = buildS3Client(s3Config);
					log.info("Cloudflare R2 client created");
				}
			}
		}
		return this.s3Client;
	}
	
	
	/**
	 * Builds and configures the S3 client with R2-specific settings
	 */
	private static S3Client buildS3Client(S3Config config) {
		AwsBasicCredentials credentials = AwsBasicCredentials.create(
				config.getAccessKey(),
				config.getSecretKey()
		                                                            );
		
		S3Configuration serviceConfiguration = S3Configuration.builder()
				.pathStyleAccessEnabled(true)
				.build();
		
		return S3Client.builder()
				.endpointOverride(URI.create(config.getEndpoint()))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.region(Region.of("auto"))
				.serviceConfiguration(serviceConfiguration)
				.requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
				.responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED)
				.build();
	}
	
	@Override
	public void close() {
		if (s3Client != null) {
			s3Client.close();
		}
	}
}