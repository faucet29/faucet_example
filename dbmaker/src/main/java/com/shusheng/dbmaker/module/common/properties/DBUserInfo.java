package com.shusheng.dbmaker.module.common.properties;

import com.shusheng.dbmaker.common.DBProperty;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by mac on 16/9/6.
 */
public class DBUserInfo extends DBProperty {

    @Override
    public void create(Schema schema) {
        Entity entity = schema.addEntity("DBUserInfo");
        entity.implementsSerializable();
//        entity.implementsInterface("ModelUtils.SoreModelMethod");
//        entity.setSuperclass("BasicRequest");
        entity.setConstructors(false);
//        entity.addIdProperty().autoincrement();
        entity.addLongProperty("account_uid").primaryKey().getProperty();
        entity.addLongProperty("fid");//好友id
        entity.addLongProperty("qyt_account");
        entity.addStringProperty("username").index();//人员名称 不可更改
        entity.addStringProperty("avatar"); //头像
        entity.addIntProperty("sex"); //性别
        entity.addStringProperty("birthday"); //出生年月日
        entity.addStringProperty("education"); //学历 1无 2小学 3初中 4高中 5中专 6大专 7本科 8硕士研究生 9博士研究生
        entity.addStringProperty("work_date");//参加工作时间
        entity.addStringProperty("emergency_contact_person");//紧急联系人
        entity.addStringProperty("emergency_contact_mobile");//紧急联系电话
        entity.addIntProperty("is_marry"); //是否已婚
        entity.addIntProperty("children_num"); //有几个孩子
        entity.addStringProperty("first_letter").index(); //姓名首字母
        entity.addStringProperty("header_letters").index(); //名字的全部首字母
        entity.addStringProperty("pinyin").index(); //名字全拼
        entity.addStringProperty("realname").index(); //名字全拼
        entity.addStringProperty("real_first_letter").index();
        entity.addStringProperty("real_header_letters").index();
        entity.addStringProperty("real_pinyin").index();
        entity.addIntProperty("blood_type");
        entity.addIntProperty("is_show");
        entity.addLongProperty("action_time");
        entity.addIntProperty("relate_type");//1-待通过, 2-拒绝通过, 3-已通过_即好友, 4-已解除_即删除,5-拉黑
        entity.addLongProperty("re_id");
        entity.addLongProperty("user_id");
        entity.addIntProperty("is_delete"); //是否删除
        entity.addIntProperty("is_me");//1.单方加好友 2.双方互加过
        entity.addLongProperty("friend_account_uid");//被加人的

        Entity messageEntity = schema.addEntity("DBValidationMessage");
        messageEntity.implementsSerializable();
        messageEntity.setConstructors(false);
        messageEntity.addIdProperty().autoincrement();// 添加ID-自增
        messageEntity.addStringProperty("message");
        Property fid = messageEntity.addLongProperty("foreign_id").getProperty();//外键
        entity.addToMany(messageEntity, fid).setName("message");// 一对多

        Entity companyUserEntity = schema.addEntity("DBCompanyUserInfo");
        companyUserEntity.implementsSerializable();
        companyUserEntity.setConstructors(false);
//        companyUserEntity.addIdProperty().autoincrement();// 添加ID-自增
        companyUserEntity.addLongProperty("uid").primaryKey();
        companyUserEntity.addLongProperty("company_id");
        Property foreign_id = companyUserEntity.addLongProperty("account_uid").getProperty();//account_uid为外键
        companyUserEntity.addStringProperty("title"); //职务
        companyUserEntity.addStringProperty("homeplace_province"); //出生地 省份
        companyUserEntity.addStringProperty("homeplace_city");//出生地 城市
        companyUserEntity.addStringProperty("homeplace_county");
        companyUserEntity.addLongProperty("salary");
        companyUserEntity.addStringProperty("hire_date");//入职时间
        companyUserEntity.addStringProperty("fire_date");//离职时间
        companyUserEntity.addStringProperty("fire_reason"); //离职原因
        companyUserEntity.addStringProperty("probation_end_time");
        companyUserEntity.addStringProperty("max");
        companyUserEntity.addStringProperty("guide_step");
        companyUserEntity.addStringProperty("push_info");
        companyUserEntity.addStringProperty("description");
        companyUserEntity.addStringProperty("last_device_id");
        companyUserEntity.addStringProperty("org_version");
        companyUserEntity.addStringProperty("source");
        companyUserEntity.addStringProperty("induction_nums");
        companyUserEntity.addStringProperty("untreated_type");//未处理类型 0正常 1未分配直属上级 2未分配部门 3 直属上级跟部门均未分配
        companyUserEntity.addLongProperty("position");
        companyUserEntity.addIntProperty("boss_type"); //是否为boss
        companyUserEntity.addIntProperty("is_admin"); //是否超级管理员 0-否 1-是 一个公司有且只有一个
        companyUserEntity.addIntProperty("is_leader"); //是否是主管
        companyUserEntity.addIntProperty("is_manager");
        companyUserEntity.addIntProperty("dimission_step");
        companyUserEntity.addIntProperty("is_delete"); //是否删除
        companyUserEntity.addLongProperty("update_time");
        companyUserEntity.addLongProperty("insert_time");
        companyUserEntity.addLongProperty("delete_time");
        entity.addToMany(companyUserEntity, foreign_id).setName("companyUser");// 一对多

        Entity companyUserTelEntity = schema.addEntity("DBCompanyUserTelInfo");
        companyUserTelEntity.implementsSerializable();
        companyUserTelEntity.setConstructors(false);
        companyUserTelEntity.addIdProperty().autoincrement();// 添加ID-自增
        companyUserTelEntity.addStringProperty("tel");
        Property tel_f_id = companyUserTelEntity.addLongProperty("foreign_id").getProperty();
        companyUserEntity.addToMany(companyUserTelEntity, tel_f_id).setName("tel");// 一对多

        Entity companyUserEmailEntity = schema.addEntity("DBCompanyUserEmailInfo");
        companyUserEmailEntity.implementsSerializable();
        companyUserEmailEntity.setConstructors(false);
        companyUserEmailEntity.addIdProperty().autoincrement();// 添加ID-自增
        companyUserEmailEntity.addStringProperty("email");
        Property email_f_id = companyUserEmailEntity.addLongProperty("foreign_id").getProperty();
        companyUserEntity.addToMany(companyUserEmailEntity, email_f_id).setName("email_info");// 一对多
    }
}
