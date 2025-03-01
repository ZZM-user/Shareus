package top.shareus.bot.robot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.MatchingStrategy;
import com.meilisearch.sdk.model.Searchable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.eumn.meilisearch.MeilisearchIndexEnums;
import top.shareus.bot.robot.config.MeilisearchConfig;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;
import top.shareus.bot.robot.service.ArchivedFileService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 归档文件服务
 *
 * @author zhaojl
 * @date 2023/01/24
 */
@Slf4j
@Service
public class ArchivedFileServiceImpl extends ServiceImpl<ArchivedFileMapper, ArchivedFile> implements ArchivedFileService {
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	@Resource
	private MeilisearchConfig meilisearchConfig;
	
	
	@Override
	public List<ArchivedFile> findBookInfoByName(String bookName) {
		if (StrUtil.isBlank(bookName)) {
			return null;
		}
		
		List<ArchivedFile> archivedFiles = archivedFileMapper.selectBookByNameFullText(bookName);
		if (CollUtil.isEmpty(archivedFiles)) {
			log.info("查不到相关内容");
			return null;
		}
		
		log.info("查询到名为：" + bookName + " 的书目，共：" + archivedFiles.size() + " 条\n" + Arrays.toString(archivedFiles.toArray()));
		return archivedFiles;
	}
	
	@Override
	public List<ArchivedFile> meiliSearch(String query) {
		if (StrUtil.isBlank(query)) {
			return null;
		}
		
		query = StrUtil.trim(query);
		
		Client meilisearchClient = meilisearchConfig.getClient();
		Index index = meilisearchClient.index(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		SearchRequest searchRequest = SearchRequest.builder()
				.limit(10)
				.page(1)
				.hitsPerPage(10)
				.sort(new String[]{"archiveDate:desc"})
				.showRankingScore(true)
				.attributesToSearchOn(new String[]{"name"})
				.matchingStrategy(MatchingStrategy.FREQUENCY)
				.rankingScoreThreshold(0.2)
				.filterArray(new String[][]{new String[]{"enabled = 0", "enabled = 0"}})
				.q(query)
				.build();
		
		Searchable searchable = index.search(searchRequest);
		ArrayList<HashMap<String, Object>> searchableHits = searchable.getHits();
		if (searchableHits.isEmpty()) {
			log.info("查不到相关内容: {}", query);
			return null;
		}
		
		List<ArchivedFile> archivedFiles = BeanUtil.copyToList(searchableHits, ArchivedFile.class);
		log.info("查询到名为：{} 的书目，共：{} 条\n {}", query, archivedFiles.size(), Arrays.toString(archivedFiles.toArray()));
		return archivedFiles;
	}
}
