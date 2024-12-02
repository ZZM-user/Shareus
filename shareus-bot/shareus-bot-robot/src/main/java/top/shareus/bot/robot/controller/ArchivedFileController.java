package top.shareus.bot.robot.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.shareus.bot.common.annotation.Cache;
import top.shareus.bot.common.domain.QMember;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;
import top.shareus.bot.robot.pojo.dto.SearchFileDTO;
import top.shareus.bot.robot.pojo.vo.ArchivedFileVO;
import top.shareus.bot.robot.pojo.vo.RankVO;
import top.shareus.bot.robot.service.ArchivedFileService;
import top.shareus.bot.robot.service.QMemberService;
import top.shareus.common.core.domain.R;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/archivedFile")
public class ArchivedFileController {
	
	@Resource
	private ArchivedFileService archivedFileService;
	
	@Resource
	private ArchivedFileMapper archivedFileMapper;
	
	@Resource
	private QMemberService qMemberService;
	
	@ApiOperation("最近文件列表")
	@GetMapping("/recent")
	@Cache(ttl = 30)
	public R<List<ArchivedFileVO>> recent() {
		List<ArchivedFileVO> list = archivedFileMapper.selectRecentFile(30);
		return R.ok(list);
	}
	
	@ApiOperation("搜索")
	@PostMapping("/search")
	public R<List<ArchivedFileVO>> search(@RequestBody @Validated SearchFileDTO dto) {
		List<ArchivedFileVO> list = archivedFileService.meiliSearch(dto.getKeyword())
				.stream()
				.map(ArchivedFileVO::new)
				.peek(x -> {
					// 获取用户头像和昵称
					QMember member = qMemberService.selectByQQ(x.getSender());
					if (ObjectUtil.isNotNull(member)) {
						x.setSender(member.getNickName());
					}
				})
				.toList();
		
		
		return R.ok(list);
	}
	
	@ApiOperation("贡献前十排行榜（实时）")
	@GetMapping("/contributionRank")
	@Cache(ttl = 1, unit = TimeUnit.HOURS)
	public R<List<RankVO>> rank() {
		// 查询 top 10 排名
		List<RankVO> list = archivedFileMapper.contributionRank();
		
		long totalContributionValue = list.stream()
				.mapToLong(map -> Convert.toLong(map.getContributionValue()))
				.sum();
		
		// 创建排名计数器
		AtomicInteger rankCounter = new AtomicInteger(1);
		
		// 将查询结果映射，并计算贡献百分比
		list.forEach(x -> {
			x.setRank(rankCounter.getAndIncrement());
			
			// 计算贡献百分比
			String contributionPercentage = Convert.toStr(Math.round(x.getContributionValue() * 100.0 / totalContributionValue));
			x.setContribution(contributionPercentage);
			
			if (StrUtil.isBlank(x.getUsername())) {
				x.setUsername("所有群友");
			}
		});
		
		list.removeIf(x -> {
			return NumberUtil.toBigDecimal(x.getContribution()).compareTo(BigDecimal.ONE) < 0;
		});
		
		return R.ok(list);
	}
}
