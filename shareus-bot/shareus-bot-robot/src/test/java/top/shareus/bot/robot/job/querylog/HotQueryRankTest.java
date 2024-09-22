package top.shareus.bot.robot.job.querylog;

import cn.hutool.core.bean.BeanUtil;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.MatchingStrategy;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TaskInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.eumn.meilisearch.MeilisearchIndexEnums;
import top.shareus.bot.robot.config.MeilisearchConfig;
import top.shareus.bot.robot.service.ArchivedFileService;
import top.shareus.bot.robot.util.MeilisearchUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class HotQueryRankTest {
	
	@Resource
	private MeilisearchConfig meilisearchConfig;
	
	@Resource
	private ArchivedFileService archivedFileService;
	
	@Test
	void execute() {
		
		
		// TaskInfo taskInfo = index.updateFilterableAttributesSettings(new String[]{"name", "delFlag", "enabled", "archiveDate"});
		// taskInfo = index.updateSortableAttributesSettings(new String[]{"archiveDate"});
		// index.getTask(taskInfo.getTaskUid());
		
		// meilisearchClient.deleteIndex(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		
		// List<ArchivedFile> archivedFileList = archivedFileService.list();
		// MeilisearchUtil.addDocuments(meilisearchClient, MeilisearchIndexEnums.ARCHIVED_FILE, archivedFileList);
		
		Client meilisearchClient = meilisearchConfig.getClient();
		Index index = meilisearchClient.index(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		meilisearchClient.deleteIndex(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		TaskInfo taskInfo = index.updateFilterableAttributesSettings(new String[]{"name", "delFlag", "enabled", "archiveDate"});
		taskInfo = index.updateSortableAttributesSettings(new String[]{"archiveDate"});
		List<ArchivedFile> archivedFileList = archivedFileService.list();
		MeilisearchUtil.addDocuments(meilisearchClient, MeilisearchIndexEnums.ARCHIVED_FILE, archivedFileList);
		
		SearchRequest searchRequest = SearchRequest.builder()
				.limit(10)
				.page(1)
				.hitsPerPage(10)
				.sort(new String[]{"archiveDate:desc"})
				.showRankingScore(true)
				.attributesToSearchOn(new String[]{"name"})
				.matchingStrategy(MatchingStrategy.FREQUENCY)
				.rankingScoreThreshold(0.2)
				.filterArray(new String[][]{new String[]{"delFlag = 0", "enabled = 0"}})
				.q("臣服四部文集 冷笑对")
				.build();
		
		Searchable searchable = index.search(searchRequest);
		ArrayList<HashMap<String, Object>> searchableHits = searchable.getHits();
		List<ArchivedFile> copyToList = BeanUtil.copyToList(searchableHits, ArchivedFile.class);
		copyToList.size();
	}
}