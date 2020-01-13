package com.shusheng.dbmaker.common;

import java.util.Iterator;
import java.util.List;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by mac on 16/9/6.
 */
public abstract class DBProperty {

    public abstract void create(Schema schema);

    public Entity getEntity(Schema schema, String className) {
        List list = schema.getEntities();
        Entity entity = null;
        Iterator it = list.iterator();

        while(it.hasNext() && entity == null) {
            Entity item = (Entity)it.next();
            if(item.getClassName().equals(className)) {
                entity = item;
            }
        }

        return entity;
    }

}
