package com.faucet.greendao.bean;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import com.faucet.greendao.dao.DaoSession;
import org.greenrobot.greendao.DaoException;

import com.faucet.greendao.dao.DBCompanyUserEmailInfoDao;
import com.faucet.greendao.dao.DBCompanyUserInfoDao;
import com.faucet.greendao.dao.DBCompanyUserTelInfoDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "DBCOMPANY_USER_INFO".
 */
@Entity(active = true)
public class DBCompanyUserInfo implements java.io.Serializable {

    @Id
    private Long uid;
    private Long company_id;
    private Long account_uid;
    private String title;
    private String homeplace_province;
    private String homeplace_city;
    private String homeplace_county;
    private Long salary;
    private String hire_date;
    private String fire_date;
    private String fire_reason;
    private String probation_end_time;
    private String max;
    private String guide_step;
    private String push_info;
    private String description;
    private String last_device_id;
    private String org_version;
    private String source;
    private String induction_nums;
    private String untreated_type;
    private Long position;
    private Integer boss_type;
    private Integer is_admin;
    private Integer is_leader;
    private Integer is_manager;
    private Integer dimission_step;
    private Integer is_delete;
    private Long update_time;
    private Long insert_time;
    private Long delete_time;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient DBCompanyUserInfoDao myDao;

    @ToMany(joinProperties = {
        @JoinProperty(name = "uid", referencedName = "foreign_id")
    })
    private List<DBCompanyUserTelInfo> tel;

    @ToMany(joinProperties = {
        @JoinProperty(name = "uid", referencedName = "foreign_id")
    })
    private List<DBCompanyUserEmailInfo> email_info;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END


    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDBCompanyUserInfoDao() : null;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public Long getAccount_uid() {
        return account_uid;
    }

    public void setAccount_uid(Long account_uid) {
        this.account_uid = account_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHomeplace_province() {
        return homeplace_province;
    }

    public void setHomeplace_province(String homeplace_province) {
        this.homeplace_province = homeplace_province;
    }

    public String getHomeplace_city() {
        return homeplace_city;
    }

    public void setHomeplace_city(String homeplace_city) {
        this.homeplace_city = homeplace_city;
    }

    public String getHomeplace_county() {
        return homeplace_county;
    }

    public void setHomeplace_county(String homeplace_county) {
        this.homeplace_county = homeplace_county;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public String getFire_date() {
        return fire_date;
    }

    public void setFire_date(String fire_date) {
        this.fire_date = fire_date;
    }

    public String getFire_reason() {
        return fire_reason;
    }

    public void setFire_reason(String fire_reason) {
        this.fire_reason = fire_reason;
    }

    public String getProbation_end_time() {
        return probation_end_time;
    }

    public void setProbation_end_time(String probation_end_time) {
        this.probation_end_time = probation_end_time;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getGuide_step() {
        return guide_step;
    }

    public void setGuide_step(String guide_step) {
        this.guide_step = guide_step;
    }

    public String getPush_info() {
        return push_info;
    }

    public void setPush_info(String push_info) {
        this.push_info = push_info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_device_id() {
        return last_device_id;
    }

    public void setLast_device_id(String last_device_id) {
        this.last_device_id = last_device_id;
    }

    public String getOrg_version() {
        return org_version;
    }

    public void setOrg_version(String org_version) {
        this.org_version = org_version;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInduction_nums() {
        return induction_nums;
    }

    public void setInduction_nums(String induction_nums) {
        this.induction_nums = induction_nums;
    }

    public String getUntreated_type() {
        return untreated_type;
    }

    public void setUntreated_type(String untreated_type) {
        this.untreated_type = untreated_type;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Integer getBoss_type() {
        return boss_type;
    }

    public void setBoss_type(Integer boss_type) {
        this.boss_type = boss_type;
    }

    public Integer getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Integer is_admin) {
        this.is_admin = is_admin;
    }

    public Integer getIs_leader() {
        return is_leader;
    }

    public void setIs_leader(Integer is_leader) {
        this.is_leader = is_leader;
    }

    public Integer getIs_manager() {
        return is_manager;
    }

    public void setIs_manager(Integer is_manager) {
        this.is_manager = is_manager;
    }

    public Integer getDimission_step() {
        return dimission_step;
    }

    public void setDimission_step(Integer dimission_step) {
        this.dimission_step = dimission_step;
    }

    public Integer getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Integer is_delete) {
        this.is_delete = is_delete;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public Long getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Long insert_time) {
        this.insert_time = insert_time;
    }

    public Long getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Long delete_time) {
        this.delete_time = delete_time;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<DBCompanyUserTelInfo> getTel() {
        if (tel == null) {
            __throwIfDetached();
            DBCompanyUserTelInfoDao targetDao = daoSession.getDBCompanyUserTelInfoDao();
            List<DBCompanyUserTelInfo> telNew = targetDao._queryDBCompanyUserInfo_Tel(uid);
            synchronized (this) {
                if(tel == null) {
                    tel = telNew;
                }
            }
        }
        return tel;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetTel() {
        tel = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<DBCompanyUserEmailInfo> getEmail_info() {
        if (email_info == null) {
            __throwIfDetached();
            DBCompanyUserEmailInfoDao targetDao = daoSession.getDBCompanyUserEmailInfoDao();
            List<DBCompanyUserEmailInfo> email_infoNew = targetDao._queryDBCompanyUserInfo_Email_info(uid);
            synchronized (this) {
                if(email_info == null) {
                    email_info = email_infoNew;
                }
            }
        }
        return email_info;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetEmail_info() {
        email_info = null;
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
