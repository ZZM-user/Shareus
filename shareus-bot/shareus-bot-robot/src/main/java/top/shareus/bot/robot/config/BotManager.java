package top.shareus.bot.robot.config;

import cn.hutool.core.util.ObjectUtil;
import com.kasukusakura.mlss.resolver.SakuraLoginSolver;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Component;
import top.shareus.common.core.exception.mirai.bot.BotException;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

/**
 * 机器人管理
 *
 * @author 17602
 * @date 2022/8/27 13:58
 */
@Slf4j
@Component
public class BotManager {
	private volatile static Bot BOT = null;
	
	/**
	 * 创建机器人
	 *
	 * @return {@link Bot}
	 */
	public static Bot createBot(BotConfig botConfig) {
		log.info("开始登录机器人……");
		if (ObjectUtil.isNotNull(BOT)) {
			return BOT;
		}
		
		BOT = BotFactory.INSTANCE.newBot(botConfig.getAccount(), botConfig.getPassword(), new BotConfiguration() {
			{
				// 设置心跳检测
				setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);
				// 使用平板协议登录
				setProtocol(MiraiProtocol.ANDROID_PHONE);
				// 指定设备信息文件路径，文件不存在将自动生成一个默认的，存在就读取
				fileBasedDeviceInfo("device.json");
				// 更多操作自己看代码补全吧
				setAutoReconnectOnForceOffline(true);
//				setHighwayUploadCoroutineCount()
				setLoginSolver(SakuraLoginSolver.Default);
			}
		});
		
		log.info("开始升级协议……");
		FixProtocolVersion.update();
		
		BOT.login();
		if (BOT.isOnline()) {
			log.info("{}-机器人上线成功！", BOT.getId());
		} else {
			log.error("{}-机器人上线失败！", BOT.getId());
			throw new BotException("机器人登录状态异常！");
		}
		return BOT;
	}
	
	/**
	 * 获取机器人
	 *
	 * @return {@link Bot}
	 */
	public static Bot getBot() {
		synchronized (BotManager.class) {
			if (ObjectUtil.isNull(BOT)) {
				log.error("没有机器人正在运行！");
				throw new BotException("没有机器人正在运行！");
			}
			return BOT;
		}
	}
}
