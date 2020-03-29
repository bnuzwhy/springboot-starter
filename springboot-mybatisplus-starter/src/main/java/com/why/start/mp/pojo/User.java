package com.why.start.mp.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String pwd;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    @Version//乐观锁Version注解
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    @TableLogic//逻辑删除注解
    private Integer deleted;
}
