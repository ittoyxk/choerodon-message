package io.choerodon.notify.infra.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Mr.Wang
 * Date: 2019/12/4
 */
public enum TargetUserType {
    /**
     * 报告人
     */
    REPORTER("reporter"),
    /**
     * 经办人
     */
    ASSIGNEE("assignee"),
    /**
     * 指定用户
     */
    SELECTED_USERS("selectedUser"),
    /**
     * 创建者
     */
    CREATOR("creator"),
    /**
     * 应用服务权限拥有者
     */
    APPLICATION_SERVICE_PERMISSION_OWNER(" applicationServicePermissionOwner"),
    /**
     * 代码提交者
     */
    CODE_SUBMITTEDER("codeSubmitteder"),
    /**
     * 实例部署人员
     */
    INSTANCE_DEPLOYER("instanceDeployer"),
    /**
     * 流水线触发者
     */
    ASSEMBLY_LINE_TRIGGERS("assemblyLineTriggers");

    private String typeName;

    TargetUserType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static TargetUserType fromTypeName(String typeName) {
        for (TargetUserType type : TargetUserType.values()) {
            if (type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public static Map<String, String> nameMapping = new HashMap<>(8);

    static {
        nameMapping.put(TargetUserType.APPLICATION_SERVICE_PERMISSION_OWNER.getTypeName(), "应用服务权限拥有者");
        nameMapping.put(TargetUserType.ASSEMBLY_LINE_TRIGGERS.getTypeName(), "流水线触发者");
        nameMapping.put(TargetUserType.ASSIGNEE.getTypeName(), "经办人");
        nameMapping.put(TargetUserType.CODE_SUBMITTEDER.getTypeName(), "代码提交者");
        nameMapping.put(TargetUserType.CREATOR.getTypeName(), "创建者");
        nameMapping.put(TargetUserType.INSTANCE_DEPLOYER.getTypeName(), "实例部署者");
        nameMapping.put(TargetUserType.REPORTER.getTypeName(), "报告人");
        nameMapping.put(TargetUserType.SELECTED_USERS.getTypeName(), "指定用户");
    }

}

