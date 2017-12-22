package io.github.stekeblad.mytodoapp.model;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Database {

    private DaoSession session;

    private TodoItemDao todoDao;
    private List<TodoItem> todoCache;
    private HashMap<Long, Integer> idToIndexMap;

    @Inject
    Database(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "todo-db");
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();
        session = new DaoMaster(db).newSession();
        todoDao = session.getTodoItemDao();
    }

    // Returns the Id of the inserted row
    public Long insert(String header, String desc) {
        if (todoCache == null) {
            reloadFromDatabase();
        }
        Long id = todoDao.insert(new TodoItem(null, header, desc)); // Id is generated automatically
        todoCache.add(new TodoItem(id, header, desc));
        idToIndexMap.put(id, todoCache.size() - 1);
        return id;
    }

    public void updateRow(Long id, String newHeader, String newDesc) throws Exception{
        if (todoCache == null) {
            reloadFromDatabase();
        }
        TodoItem newItem = new TodoItem(id, newHeader, newDesc);
        Integer index = idToIndexMap.get(newItem.getId());
        if (index != null) {
            todoDao.save(newItem);
            todoCache.set(index, newItem);
        } else {
            throw new Exception("Can't update non-existing element");
        }
    }

    public List<TodoItem> getAll() {
        if (todoCache == null) {
            reloadFromDatabase();
        }
        return todoCache;
    }

    public TodoItem getByIndex(int index) {
        if (todoCache == null) {
            reloadFromDatabase();
        }
        return todoCache.get(index);
    }

    public void deleteRow(int cacheIndex) {
        if (todoCache == null) {
            reloadFromDatabase();
        }
        TodoItem item = todoCache.get(cacheIndex);
        todoDao.delete(item);
        todoCache.remove(item);
        generateIndexMap();
    }

    public void clearDatabase() {
        todoDao.deleteAll();
        todoCache.clear();
        idToIndexMap.clear();
    }

    /**
     * Call then app receives a request from the OS to free memory
     */
    public void clearCaches() {
        if (todoCache != null) {
            todoCache.clear();
            idToIndexMap.clear();
        }
        todoCache = null;
        idToIndexMap = null;
    }

    private void reloadFromDatabase() {
        todoCache = todoDao.loadAll();
        generateIndexMap();
    }

    @SuppressLint("UseSparseArrays")
    private void generateIndexMap() {
        idToIndexMap = new HashMap<>();
        for (int i = 0; i < todoCache.size(); i++) {
            idToIndexMap.put(todoCache.get(i).getId(), i);
        }
    }
}
