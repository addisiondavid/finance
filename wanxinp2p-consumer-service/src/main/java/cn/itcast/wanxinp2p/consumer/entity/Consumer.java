package cn.itcast.wanxinp2p.consumer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("consumer")
public class Consumer {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("ID")
    private Long id;

    @TableField("USERNAME")
    private String userName;

    @TableField("FULLNAME")
    private String fullName;

    @TableField("ID_NUMBER")
    private String idNumber;

    @TableField("USER_NO")
    private String userNo;

    @TableField("MOBILE")
    private String mobile;

    @TableField("USER_TYPE")
    private String userType;

    @TableField("ROLE")
    private String role;

    @TableField("AUTH_LIST")
    private String authList;

    @TableField("IS_BIND_CARD")
    private Integer isBindCard;

    @TableField("LOAN_AMOUNT")
    private BigDecimal loanAmount;

    @TableField("STATUS")
    private Integer status;

    @TableField("REQUEST_NO")
    private String requestNo;

}
