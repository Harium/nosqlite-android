package com.harium.database.nosqlite;

import android.content.Context;

import com.harium.database.model.BaseDAO;

import java.util.Date;
import java.util.List;

public class Database implements BaseDAO<Data, Context> {

    public static Context context;
    private static SQLiteDatabaseAndroidModule module;

    public static void init() {
        module = new SQLiteDatabaseAndroidModule(context);
    }

    @Override
    public void init(Context connection) {
        module = new SQLiteDatabaseAndroidModule(connection);
    }

    @Override
    public List<Data> queryAll() {
        return module.queryAll();
    }

    @Override
    public int create(Data item) {
        item.setCreatedAt(new Date().getTime());
        item.setUpdatedAt(new Date().getTime());
        return module.create(item);
    }

    @Override
    public int update(Data item) {
        item.setUpdatedAt(new Date().getTime());
        return module.update(item);
    }

    @Override
    public int delete(Data model) {
        return module.delete(model);
    }

    public static long count() {
        return module.count();
    }

    public static void put(String key, String value) {
        Data data = new Data(key, value);
        module.createOrUpdate(data);
    }

    public static String get(String key) {
        Data value = module.queryData(key);
        if (value != null) {
            return value.getValue();
        }

        return null;
    }

    @Override
    public Class<Data> getKlass() {
        return Data.class;
    }

    public void dispose() {
        module.close();
    }

}
