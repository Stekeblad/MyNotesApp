package io.github.stekeblad.mynotesapp.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.github.stekeblad.mynotesapp.R;
import io.github.stekeblad.mynotesapp.addedit.AddEditActivity;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_EDIT;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_ADDEDIT;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_DESC;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_HEADER;
import static io.github.stekeblad.mynotesapp.resources.Constants.INTENT_KEY_TEXT_ID;

@ActivityScoped
public class DetailsActivity extends DaggerAppCompatActivity implements DetailsContract.View{

    @Inject
    DetailsContract.Presenter mDetailsPresenter;

    private TextView viewHeader;
    private EditText viewDesc;
    private Long idForEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDetailsPresenter.takeView(this);

        Toolbar mToolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(mToolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            System.err.println("Could not set HomeAsUp(true)");
        }
        getSupportActionBar().setElevation(1);

        viewHeader = findViewById(R.id.ViewHeader);
        viewDesc = findViewById(R.id.ViewDesc);

        if (savedInstanceState != null) {
            viewHeader.setText(savedInstanceState.getString("header"));
            viewDesc.setText(savedInstanceState.getString("desc"));
            idForEdit = savedInstanceState.getLong("id");
        } else {
            Intent intent = getIntent();
            viewHeader.setText(intent.getStringExtra(INTENT_KEY_TEXT_HEADER));
            viewDesc.setText(intent.getStringExtra(INTENT_KEY_TEXT_DESC));
            idForEdit = intent.getLongExtra(INTENT_KEY_TEXT_ID, -1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDetailsPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        mDetailsPresenter.dropView();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("header", viewHeader.getText().toString());
        outState.putString("desc", viewDesc.getText().toString());
        outState.putLong("id", idForEdit);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbarDetails_edit:
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra(INTENT_KEY_ADDEDIT, INTENT_EDIT);
                intent.putExtra(INTENT_KEY_TEXT_HEADER, viewHeader.getText().toString());
                intent.putExtra(INTENT_KEY_TEXT_DESC, viewDesc.getText().toString());
                intent.putExtra(INTENT_KEY_TEXT_ID, idForEdit);
                startActivityForResult(intent, INTENT_EDIT);
                break;
            case R.id.homeAsUp: // Intended to get here when the back arrow in toolbar was clicked
                onBackPressed(); // but it sets item to null and jumps to default
                break;
            default:
                //Toast.makeText(this, "Not implemented: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INTENT_EDIT:
                    String header = data.getStringExtra(INTENT_KEY_TEXT_HEADER);
                    String desc = data.getStringExtra(INTENT_KEY_TEXT_DESC);
                    viewHeader.setText(header);
                    viewDesc.setText(desc);
                    break;
            }
        }// else if resultCode == Activity.RESULT_CANCELED {do nothing}
        // else unknown code, do nothing
    }

}
