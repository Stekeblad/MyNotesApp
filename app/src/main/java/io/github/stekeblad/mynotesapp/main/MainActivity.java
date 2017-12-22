package io.github.stekeblad.mynotesapp.main;

import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.github.stekeblad.mynotesapp.R;
import io.github.stekeblad.mynotesapp.addedit.AddEditActivity;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;
import io.github.stekeblad.mynotesapp.details.DetailsActivity;
import io.github.stekeblad.mynotesapp.model.NoteItem;

import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_ADD;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_ADDEDIT;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_DESC;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_HEADER;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_ID;

@ActivityScoped
public class MainActivity extends DaggerAppCompatActivity implements MainContract.View, ComponentCallbacks2 {

    @Inject
    MainContract.Presenter mMainPresenter;

    private ListView noteList;
    private ArrayAdapter<String> noteAdapter;
    private ArrayList<String> arrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        mMainPresenter.takeView(this);

        arrayList = new ArrayList<>();
        // getApplicationContext makes the text in the listView white on Android version 4.3 and 6.0.1
        // and probably between and below (down to minSDK-version)
        noteAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, arrayList);
        noteList = findViewById(R.id.NotesList);
        noteList.setAdapter(noteAdapter);
        mMainPresenter.loadNoteItems();

        ListView noteList = findViewById(R.id.NotesList);
        noteList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                NoteItem selected = mMainPresenter.getNote(pos);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(INTENT_KEY_TEXT_HEADER, selected.getHeader());
                intent.putExtra(INTENT_KEY_TEXT_DESC, selected.getDescription());
                intent.putExtra(INTENT_KEY_TEXT_ID, selected.getId());
                startActivity(intent);
            }
        });


        noteList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int pos, long id) {
                final String selected = parent.getItemAtPosition(pos).toString();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);
                dlgAlert.setMessage(getString(R.string.dlg_delete_msg) + " \"" + selected + "\"?");
                dlgAlert.setTitle(R.string.dlg_delete_title);
                dlgAlert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainPresenter.deleteNoteItem(pos);
                    }
                });
                dlgAlert.setNegativeButton(R.string.cancel, null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return true;
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPresenter.takeView(this);
        // Activity do not know if a new note was added or if a existing was updated
        //or nothing changed at all. So all notes need to be updated.
        mMainPresenter.loadNoteItems();
    }

    @Override
    public void onDestroy() {
        mMainPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbarMain_addNew:
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra(INTENT_KEY_ADDEDIT, INTENT_ADD);
                startActivity(intent);
                break;
            case R.id.toolbarMain_delAll:
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setTitle(R.string.dlg_deleteAll_title);
                dlgAlert.setMessage(R.string.dlg_deleteAll_msg);
                dlgAlert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainPresenter.deleteAll();
                    }
                });
                dlgAlert.setNegativeButton(R.string.no, null);
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
                break;
            default:
                    Toast.makeText(this, "Not implemented: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called by the OS when the device is getting low on memory and want the app to free some
     * if possible
     * @param level how critical the situation is
     */
    public void onTrimMemory(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                mMainPresenter.freeMemory();
                break;
                // I think the following are the less important levels
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
            default:
                // not important enough for this app to free its memory
                break;
        }
    }

    @Override
    public void setNoteList(List<String> list) {
        noteAdapter.clear();
        noteAdapter.addAll(list);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeFromNoteList(int index) {
        noteAdapter.remove(arrayList.get(index));
        noteAdapter.notifyDataSetChanged();
    }
}
