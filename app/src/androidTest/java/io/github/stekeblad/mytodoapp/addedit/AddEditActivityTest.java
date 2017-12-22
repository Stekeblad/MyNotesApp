package io.github.stekeblad.mytodoapp.addedit;

import android.os.Looper;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.stekeblad.mytodoapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static io.github.stekeblad.mytodoapp.resources.Constants.INTENT_KEY_TEXT_HEADER;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

// Failed attempt to writing my own test

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEditActivityTest {

    private AddEditActivity realAddEditActivity;
    private static final String PACKAGE_NAME = "io.github.stekeblad.mytodoapp";
    private static final String TEST_STRING = "type this text in field";

    @Rule
    public ActivityTestRule<AddEditActivity> mActivityRule =
            new ActivityTestRule<>(AddEditActivity.class);

    @Rule
    public IntentsTestRule<AddEditActivity> mIntentsRule =
            new IntentsTestRule<>(AddEditActivity.class);


    @Before
    public void prepare() {
        Looper.prepare();
        realAddEditActivity = new AddEditActivity();
    }

    @After
    public void finish() {

    }

    @Test
    public void textEnteredInField_askForConfirmationOnBack() {
        onView(withId(R.id.NewTodoHeader)).perform(typeText(TEST_STRING), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());
        intended(allOf(
                toPackage(PACKAGE_NAME),
                hasExtra(INTENT_KEY_TEXT_HEADER, TEST_STRING)));
    }

    @Test
    public void progressBarReactsOnShowHide() {
        ViewInteraction prog = onView(
                withId(R.id.progressBar)
        );
        prog.check(matches(not(isDisplayed())));

        realAddEditActivity.showProgressbar();

        ViewInteraction prog2 = onView(
                withId(R.id.progressBar)
        );
        prog2.check(matches(isDisplayed()));

        realAddEditActivity.hideProgressbar();

        ViewInteraction prog3 = onView(
                withId(R.id.progressBar)
        );
        prog3.check(matches(not(isDisplayed())));
    }
}
