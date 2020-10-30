package com.wosai.generate.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class User {
    /**
     * 用户ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户姓名
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 所属单位
     */
    @Column(name = "`company`")
    private String company;

    /**
     * 账号
     */
    @Column(name = "`account`")
    private String account;

    /**
     * 密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 密码文本（业主单位账号使用）
     */
    @Column(name = "`password_text`")
    private String passwordText;

    /**
     * 手机
     */
    @Column(name = "`phone`")
    private String phone;

    /**
     * 微信
     */
    @Column(name = "`wechar`")
    private String wechar;

    /**
     * 电子邮箱
     */
    @Column(name = "`email`")
    private String email;

    /**
     * 账号状态
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 头像
     */
    @Column(name = "`photo`")
    private String photo;

    /**
     * 职称
     */
    @Column(name = "`professional`")
    private String professional;

    /**
     * 职称职务
     */
    @Column(name = "`post`")
    private String post;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;

    /**
     * 角色ID
     */
    @Column(name = "`role_id`")
    private Integer roleId;

    /**
     * 所属编制单位id（仅编制单位账号）
     */
    @Column(name = "`third_id`")
    private Integer thirdId;

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
     * 获取用户ID
     *
     * @return id - 用户ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户ID
     *
     * @param id 用户ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户姓名
     *
     * @return name - 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     *
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所属单位
     *
     * @return company - 所属单位
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置所属单位
     *
     * @param company 所属单位
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取账号
     *
     * @return account - 账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号
     *
     * @param account 账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取密码文本（业主单位账号使用）
     *
     * @return password_text - 密码文本（业主单位账号使用）
     */
    public String getPasswordText() {
        return passwordText;
    }

    /**
     * 设置密码文本（业主单位账号使用）
     *
     * @param passwordText 密码文本（业主单位账号使用）
     */
    public void setPasswordText(String passwordText) {
        this.passwordText = passwordText;
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取微信
     *
     * @return wechar - 微信
     */
    public String getWechar() {
        return wechar;
    }

    /**
     * 设置微信
     *
     * @param wechar 微信
     */
    public void setWechar(String wechar) {
        this.wechar = wechar;
    }

    /**
     * 获取电子邮箱
     *
     * @return email - 电子邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮箱
     *
     * @param email 电子邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取账号状态
     *
     * @return status - 账号状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置账号状态
     *
     * @param status 账号状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取头像
     *
     * @return photo - 头像
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置头像
     *
     * @param photo 头像
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取职称
     *
     * @return professional - 职称
     */
    public String getProfessional() {
        return professional;
    }

    /**
     * 设置职称
     *
     * @param professional 职称
     */
    public void setProfessional(String professional) {
        this.professional = professional;
    }

    /**
     * 获取职称职务
     *
     * @return post - 职称职务
     */
    public String getPost() {
        return post;
    }

    /**
     * 设置职称职务
     *
     * @param post 职称职务
     */
    public void setPost(String post) {
        this.post = post;
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
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取所属编制单位id（仅编制单位账号）
     *
     * @return third_id - 所属编制单位id（仅编制单位账号）
     */
    public Integer getThirdId() {
        return thirdId;
    }

    /**
     * 设置所属编制单位id（仅编制单位账号）
     *
     * @param thirdId 所属编制单位id（仅编制单位账号）
     */
    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
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