package io.github.stekeblad.mynotesapp.addedit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.github.stekeblad.mynotesapp.R;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_ADD;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_EDIT;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_ADDEDIT;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_DESC;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_HEADER;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_ID;

@ActivityScoped
public class AddEditActivity extends DaggerAppCompatActivity implements AddEditContract.View{

    @Inject
    AddEditContract.Presenter mAddEditPresenter;

    private EditText textHeader;
    private EditText textDesc;
    private String origHeader;
    private String origDesc;
    private int addEdit_flag;
    private Long idForEdit;
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        mAddEditPresenter.takeView(this);

        textHeader = findViewById(R.id.NewNoteHeader);
        textDesc = findViewById(R.id.NewNoteDescription);
        progressBar = findViewById(R.id.progressBar);

        if (savedInstanceState != null) {
            textHeader.setText(savedInstanceState.getString("header"));
            textDesc.setText(savedInstanceState.getString("desc"));
            idForEdit = savedInstanceState.getLong("id");
            addEdit_flag = savedInstanceState.getInt("flag");
            origHeader = savedInstanceState.getString("origHeader");
            origDesc = savedInstanceState.getString("origDesc");
        } else { // activity just launched, load from intent
            Intent intent = getIntent();
            addEdit_flag = intent.getIntExtra(INTENT_KEY_ADDEDIT, 0);
            if (addEdit_flag == INTENT_EDIT) {
                origHeader = intent.getStringExtra(INTENT_KEY_TEXT_HEADER);
                textHeader.setText(origHeader);
                origDesc = intent.getStringExtra(INTENT_KEY_TEXT_DESC);
                textDesc.setText(origDesc);
                idForEdit = intent.getLongExtra(INTENT_KEY_TEXT_ID, -1);
            } else { // INTENT_ADD or unknown
                idForEdit = -1L; // do not leave it uninitialized
                origHeader = "";
                origDesc = "";
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddEditPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        mAddEditPresenter.dropView();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        outState.putString("header", textHeader.getText().toString());
        outState.putString("desc", textDesc.getText().toString());
        outState.putLong("id", idForEdit);
        outState.putInt("flag", addEdit_flag);
        outState.putString("origHeader", origHeader);
        outState.putString("origDesc", origDesc);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (textHeader.getText().toString().equals(origHeader) &&
                textDesc.getText().toString().equals(origDesc)) {
            super.onBackPressed(); // fields unchanged, nothing to save

        } else { // Ask for confirmation
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setTitle(R.string.dlg_back_title);
            if (addEdit_flag == INTENT_ADD) {
                dlgAlert.setMessage(R.string.dlg_back_msg_notSaved);
            } else { //editing
                dlgAlert.setMessage(R.string.dlg_back_msg_changesLost);
            }
            dlgAlert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setResult(Activity.RESULT_CANCELED);
                    AddEditActivity.super.onBackPressed();
                }
            });
            dlgAlert.setNegativeButton(R.string.no, null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void onClickCancel(View view) {
        onBackPressed();
    }

    public void onClickSave(View view) {
        if (textHeader.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.addEdit_headerReq, Toast.LENGTH_LONG).show();
            return;
        }
        if (addEdit_flag == INTENT_EDIT) {
            if (!origHeader.equals(textHeader.getText().toString()) ||
                    !origDesc.equals(textDesc.getText().toString())) {// if false, no changes to save, so why save.
                mAddEditPresenter.updateDB(idForEdit, textHeader.getText().toString(), textDesc.getText().toString());
            } else {
                onBackPressed();
            }
        } else {
            // INTENT_ADD or unknown state: add it, better create a new instead of updating compared
            // to ignoring instead of saving a new note
            mAddEditPresenter.addToDB(textHeader.getText().toString(), textDesc.getText().toString());
        }
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_TEXT_HEADER, textHeader.getText().toString());
        intent.putExtra(INTENT_KEY_TEXT_DESC, textDesc.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void finishActivityWithError(String errorMsg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle(R.string.error);
        dlgAlert.setMessage(errorMsg);
        dlgAlert.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }
}
