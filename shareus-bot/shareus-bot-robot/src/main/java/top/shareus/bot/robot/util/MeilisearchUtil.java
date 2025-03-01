package top.shareus.bot.robot.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.TaskInfo;
import top.shareus.bot.common.eumn.meilisearch.MeilisearchIndexEnums;

import java.util.List;

public class MeilisearchUtil {
	
	private final static Log log = LogFactory.getCurrentLogFactory().getLog(MeilisearchUtil.class);
	
	/**
	 * 添加文档
	 *
	 * @param meilisearchClient
	 * @param indexEnums
	 * @param objList
	 * @param <T>
	 */
	public static <T> void addDocuments(Client meilisearchClient, MeilisearchIndexEnums indexEnums, List<T> objList) {
		if (ObjectUtil.hasNull(meilisearchClient, indexEnums) || CollUtil.isEmpty(objList)) {
			return;
		}
		log.info("-----------------------------------------------------");
		
		JSONArray array = new JSONArray();
		
		List<JSONObject> objectList = objList.stream().map(JSONObject::new).toList();
		log.info("待添加文档数量: {}", objectList.size());
		
		array.put(objectList);
		String documents = array.getJSONArray(0).toString();
		
		Index index = meilisearchClient.index(indexEnums.getIndex());
		
		String primaryKey = indexEnums.getPrimaryKey();
		TaskInfo taskInfo;
		if (StrUtil.isBlank(primaryKey)) {
			taskInfo = index.addDocuments(documents);
		} else {
			taskInfo = index.addDocuments(documents, primaryKey);
		}
		
		log.info("添加文档任务ID: {}", taskInfo.getTaskUid());
		log.info("添加文档任务状态: {}", taskInfo.getStatus());
		log.info("添加文档任务信息：{}", JSONUtil.toJsonStr(taskInfo));
		
		log.info("添加文档任务结果：{}", JSONUtil.toJsonStr(index.getTask(taskInfo.getTaskUid())));
		log.info("-----------------------------------------------------");
	}
	
	/**
	 * 删除索引
	 *
	 * @param meilisearchClient
	 * @param indexEnums
	 */
	public static void delIndex(Client meilisearchClient, MeilisearchIndexEnums indexEnums) {
		if (ObjectUtil.hasNull(meilisearchClient, indexEnums)) {
			return;
		}
		meilisearchClient.deleteIndex(indexEnums.getIndex());
	}
	
	public static void createIndex(Client meilisearchClient, MeilisearchIndexEnums indexEnums) {
		if (ObjectUtil.hasNull(meilisearchClient, indexEnums)) {
			return;
		}
		meilisearchClient.createIndex(indexEnums.getIndex());
	}
	
	public static void refreshIndex(Client meilisearchClient) {
		Index index = meilisearchClient.index(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		meilisearchClient.deleteIndex(MeilisearchIndexEnums.ARCHIVED_FILE.getIndex());
		TaskInfo taskInfo = index.updateFilterableAttributesSettings(new String[]{"name", "delFlag", "enabled", "archiveDate"});
		taskInfo = index.updateSortableAttributesSettings(new String[]{"archiveDate"});
	}
}
