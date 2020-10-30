package com.loyal.safe.model;

import com.loyal.safe.framework.mybatis.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "rk_person")
@Getter
@Setter
@ToString
public class RkPerson extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 名字
     */
    private String name;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 年龄 1婴幼儿 2 少年 3 青少年 4青年 5中年 6 老年
     */
    private Integer age;

    /**
     * 1房东信息 2 租客信息 3 房东成员 4 作战单位人员
     */
    private Integer type;

    /**
     * 返回时间
     */
    @Column(name = "back_date")
    private Date backDate;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 学历 1 初中及以下 2 高中 3大专 4本科 5研究生 6博士及以上
     */
    @Column(name = "record_type")
    private Integer recordType;

    /**
     * 街道id
     */
    @Column(name = "street_id")
    private String streetId;

    /**
     * 社区id
     */
    @Column(name = "community_id")
    private String communityId;

    /**
     * 小区id
     */
    @Column(name = "plot_id")
    private String plotId;

    /**
     * 楼道id
     */
    @Column(name = "corridor_id")
    private String corridorId;

    /**
     * 房源信息(租客才有)
     */
    @Column(name = "house_id")
    private String houseId;

    /**
     * 房间数
     */
    @Column(name = "floor_num")
    private Integer floorNum;

    /**
     * 租客数
     */
    @Column(name = "tenant_num")
    private Integer tenantNum;

    /**
     * 隔离时间
     */
    @Column(name = "isolation_date")
    private Date isolationDate;

    /**
     * 职务 
     */
    private Integer position;

    /**
     * 来源地
     */
    private String source;

    /**
     * 是否有健康码 1:有 2:无
     */
    @Column(name = "code_type")
    private Integer codeType;

    /**
     * 是否重点管控 1 是 2 否
     */
    @Column(name = "control_type")
    private Integer controlType;

    /**
     * 房东id(租客才有此信息）
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 账号id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 预警级别
     */
    @Column(name = "warn_level")
    private Integer warnLevel;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "id_card")
    private String idCard;

    /**
     * 是否楼栋长
     */
    @Column(name = "is_builder")
    private String isBuilder;

    /**
     * 房产地址
     */
    private String place;

    @Column(name = "relation_ship")
    private String relationShip;

    /**
     * 居住地
     */
    @Column(name = "where_come")
    private String whereCome;

    @Column(name = "zk_state")
    private String zkState;

    @Column(name = "age_num")
    private String ageNum;

    @Column(name = "cars_code")
    private String carsCode;

    @Column(name = "cars_num")
    private Integer carsNum;

    @Column(name = "trash_code")
    private String trashCode;

    @Column(name = "trash_num")
    private Integer trashNum;

    @Column(name = "code_number")
    private String codeNumber;

    private String national;

    @Column(name = "homehold_address")
    private String homeholdAddress;

    @Column(name = "now_address")
    private String nowAddress;

    @Column(name = "settle_info")
    private String settleInfo;

    private String editor;

    @Column(name = "police_station")
    private String policeStation;

    @Column(name = "village_committe")
    private String villageCommitte;

    @Column(name = "relation_people")
    private String relationPeople;

    @Column(name = "work_address")
    private String workAddress;

    @Column(name = "landlord_name")
    private String landlordName;

    @Column(name = "landlord_phone")
    private String landlordPhone;
}