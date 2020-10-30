package com.wosai.generate.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Role {
    /**
     * 角色ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 角色状态
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "`create_user`")
    private Integer createUser;

    /**
     * 创建时间
     */
    @Column(name = "`create_date`")
    private Date createDate;

    /**
     * 修改人
     */
    @Column(name = "`update_user`")
    private Integer updateUser;

    /**
     * 修改时间
     */
    @Column(name = "`update_date`")
    private Date updateDate;

    /**
     * 删除人
     */
    @Column(name = "`expire_user`")
    private Integer expireUser;

    /**
     * 删除时间
     */
    @Column(name = "`expire_date`")
    private Date expireDate;

    /**
     * 获取角色ID
     *
     * @return id - 角色ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置角色ID
     *
     * @param id 角色ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名
     *
     * @return name - 角色名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名
     *
     * @param name 角色名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取角色状态
     *
     * @return status - 角色状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置角色状态
     *
     * @param status 角色状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return create_user - 创建人
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建人
     *
     * @param createUser 创建人
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改人
     *
     * @return update_user - 修改人
     */
    public Integer getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置修改人
     *
     * @param updateUser 修改人
     */
    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 获取修改时间
     *
     * @return update_date - 修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改时间
     *
     * @param updateDate 修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取删除人
     *
     * @return expire_user - 删除人
     */
    public Integer getExpireUser() {
        return expireUser;
    }

    /**
     * 设置删除人
     *
     * @param expireUser 删除人
     */
    public void setExpireUser(Integer expireUser) {
        this.expireUser = expireUser;
    }

    /**
     * 获取删除时间
     *
     * @return expire_date - 删除时间
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * 设置删除时间
     *
     * @param expireDate 删除时间
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}