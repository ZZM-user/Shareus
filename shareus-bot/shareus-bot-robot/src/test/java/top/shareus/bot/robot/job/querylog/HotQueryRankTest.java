package top.shareus.bot.robot.job.querylog;

import net.mamoe.mirai.mock.MockBotFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HotQueryRankTest {
	
	@Autowired
	private HotQueryRank hotQueryRank;
	
	@Test
	void execute() {
		MockBotFactory.initialize();
		hotQueryRank.execute();
	}
}