package com.alloy.cloud.common.core.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class BaseEntity implements Serializable {
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;//修改时间
    private Integer isDelete;//0-正常，1-删除
    private Integer objVersion; //版本号
}
