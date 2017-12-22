package io.github.stekeblad.mynotesapp.model;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Database {

    private DaoSession session;

    private NoteItemDao noteDao;
    private List<NoteItem> noteCache;
    private HashMap<Long, Integer> idToIndexMap;

    @Inject
    Database(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "note-db");
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();
        session = new DaoMaster(db).newSession();
        noteDao = session.getNoteItemDao();
    }

    // Returns the Id of the inserted row
    public Long insert(String header, String desc) {
        if (noteCache == null) {
            reloadFromDatabase();
        }
        Long id = noteDao.insert(new NoteItem(null, header, desc)); // Id is generated automatically
        noteCache.add(new NoteItem(id, header, desc));
        idToIndexMap.put(id, noteCache.size() - 1);
        return id;
    }

    public void updateRow(Long id, String newHeader, String newDesc) throws Exception{
        if (noteCache == null) {
            reloadFromDatabase();
        }
        NoteItem newItem = new NoteItem(id, newHeader, newDesc);
        Integer index = idToIndexMap.get(newItem.getId());
        if (index != null) {
            noteDao.save(newItem);
            noteCache.set(index, newItem);
        } else {
            throw new Exception("Can't update non-existing element");
        }
    }

    public List<NoteItem> getAll() {
        if (noteCache == null) {
            reloadFromDatabase();
        }
        return noteCache;
    }

    public NoteItem getByIndex(int index) {
        if (noteCache == null) {
            reloadFromDatabase();
        }
        return noteCache.get(index);
    }

    public void deleteRow(int cacheIndex) {
        if (noteCache == null) {
            reloadFromDatabase();
        }
        NoteItem item = noteCache.get(cacheIndex);
        noteDao.delete(item);
        noteCache.remove(item);
        generateIndexMap();
    }

    public void clearDatabase() {
        noteDao.deleteAll();
        noteCache.clear();
        idToIndexMap.clear();
    }

    /**
     * Call then app receives a request from the OS to free memory
     */
    public void clearCaches() {
        if (noteCache != null) {
            noteCache.clear();
            idToIndexMap.clear();
        }
        noteCache = null;
        idToIndexMap = null;
    }

    private void reloadFromDatabase() {
        noteCache = noteDao.loadAll();
        generateIndexMap();
    }

    @SuppressLint("UseSparseArrays")
    private void generateIndexMap() {
        idToIndexMap = new HashMap<>();
        for (int i = 0; i < noteCache.size(); i++) {
            idToIndexMap.put(noteCache.get(i).getId(), i);
        }
    }
}
