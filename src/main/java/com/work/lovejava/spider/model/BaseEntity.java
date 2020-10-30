package com.work.lovejava.spider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by LW on 2018/2/28
 */
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "expire_date")
    private Date expireDate;
}
