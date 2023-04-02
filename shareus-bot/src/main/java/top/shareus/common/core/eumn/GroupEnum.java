package top.shareus.common.core.eumn;

import lombok.Getter;
import top.shareus.common.core.constant.GroupsConstant;

import java.util.List;

/**
 * 群组枚举
 *
 * @author zhaojl
 * @date 2023/02/25
 */
public enum GroupEnum {
    /**
     * 管理组
     */
    ADMIN_GROUP(GroupsConstant.ADMIN_GROUPS),
    
    /**
     * 资源组
     */
    RES_GROUP(GroupsConstant.RES_GROUPS),
    
    /**
     * 聊天组
     */
    CHAT_GROUP(GroupsConstant.CHAT_GROUPS),
    
    /**
     * 测试组
     */
    TEST_GROUP(GroupsConstant.TEST_GROUPS);
    
    @Getter
    private List<Long> groupList;
    
    GroupEnum(List<Long> groupList) {
        this.groupList = groupList;
    }
}
