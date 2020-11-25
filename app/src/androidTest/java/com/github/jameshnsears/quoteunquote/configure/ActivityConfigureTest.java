package com.github.jameshnsears.quoteunquote.configure;

import android.appwidget.AppWidgetManager;
import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.BuildConfig;
import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.FragmentAppearance;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.FragmentContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.event.FragmentEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ActivityConfigureTest {
    @Rule
    public ActivityScenarioRule activityRule = new ActivityScenarioRule(ActivityConfigure.class);

    private ActivityConfigure activityConfigure;

    @Before
    public void setUp() {
        final Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        activityConfigure = activityRule.launchActivity(intent);
    }

    @Test
    public void fragmentAppearance() {
        final FragmentAppearance fragmentAppearance = (FragmentAppearance)
                activityConfigure.getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderAppearance);

        assertEquals("", 2, fragmentAppearance.fragmentAppearanceBinding.seekBarTransparency.getProgress());
        assertEquals("", 15, fragmentAppearance.fragmentAppearanceBinding.spinnerColour.getAdapter().getCount());
        assertEquals("", 8, fragmentAppearance.fragmentAppearanceBinding.spinnerSize.getAdapter().getCount());
        assertFalse("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchFirst.isChecked());
        assertTrue("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchPrevious.isChecked());
        assertTrue("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchReport.isChecked());
        assertTrue("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchToggleFavourite.isChecked());
        assertTrue("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchShare.isChecked());
        assertTrue("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchNextRandom.isChecked());
        assertFalse("", fragmentAppearance.fragmentAppearanceBinding.toolbarSwitchNextSequential.isChecked());

    }

    @Test
    public void fragmentContent() throws InterruptedException {
        final FragmentContent fragmentContent = (FragmentContent)
                activityConfigure.getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderContent);

        Thread.sleep(2500);

        if (BuildConfig.USE_PROD_DB) {
            assertEquals("", "All Quotations: 14220", fragmentContent.fragmentContentBinding.radioButtonAll.getText().toString());
            assertEquals("", "Author: 802", fragmentContent.fragmentContentBinding.radioButtonAuthor.getText().toString());
            assertEquals("", "Favourites: 0", fragmentContent.fragmentContentBinding.radioButtonFavourites.getText().toString());
            assertEquals("", "Text Search: 0", fragmentContent.fragmentContentBinding.radioButtonKeywords.getText().toString());
        } else {
            assertEquals("", "All Quotations: 7", fragmentContent.fragmentContentBinding.radioButtonAll.getText().toString());
            assertEquals("", "Author: 1", fragmentContent.fragmentContentBinding.radioButtonAuthor.getText().toString());
            assertEquals("", "Favourites: 0", fragmentContent.fragmentContentBinding.radioButtonFavourites.getText().toString());
            assertEquals("", "Search: 0", fragmentContent.fragmentContentBinding.radioButtonKeywords.getText().toString());
        }
        assertTrue("", fragmentContent.fragmentContentBinding.radioButtonAll.isChecked());
        assertFalse("", fragmentContent.fragmentContentBinding.radioButtonAuthor.isChecked());

        assertFalse("", fragmentContent.fragmentContentBinding.radioButtonFavourites.isChecked());
        assertTrue("", fragmentContent.fragmentContentBinding.buttonReceive.isEnabled());
        assertFalse("", fragmentContent.fragmentContentBinding.buttonSend.isEnabled());

        assertFalse("", fragmentContent.fragmentContentBinding.radioButtonKeywords.isChecked());
    }

    @Test
    public void fragmentEvent() {
        final FragmentEvent fragmentEvent = (FragmentEvent)
                activityConfigure.getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderEvent);

        assertFalse("", fragmentEvent.fragmentEventBinding.checkBoxDailyAt.isChecked());
        assertFalse("", fragmentEvent.fragmentEventBinding.checkBoxDeviceUnlock.isChecked());
    }

    @Test
    public void fragmentContentAuthor() throws InterruptedException {
        final FragmentContent fragmentContent = (FragmentContent)
                activityConfigure.getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderContent);

        fragmentContent.fragmentContentBinding.radioButtonAuthor.setEnabled(true);
        Thread.sleep(250);

        if (BuildConfig.USE_PROD_DB) {
            assertEquals("", "/r/quotes/top", fragmentContent.fragmentContentBinding.spinnerAuthors.getAdapter().getItem(0).toString());
        } else {
            assertEquals("", "a1", fragmentContent.fragmentContentBinding.spinnerAuthors.getAdapter().getItem(0).toString());
        }
    }
}
