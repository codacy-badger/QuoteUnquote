package com.github.jameshnsears.quoteunquote.listview;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.configure.fragment.content.ContentPreferences;
import com.github.jameshnsears.quoteunquote.database.DatabaseHelper;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Ignore

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ListViewProviderTest extends DatabaseHelper {
    @Test
    public void listView() {
        insertTestDataSet01();

        final Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WIDGET_ID);

        final Context mockContext = mock(Context.class);

        final ListViewProvider listViewProviderTestSpy = spy(new ListViewProvider(mockContext, intent));
        doReturn(this.quoteUnquoteModel).when(listViewProviderTestSpy).getQuoteUnquoteModel(mockContext);

        final ContentPreferences preferenceAppearanceSpy = spy(new ContentPreferences(-1, mockContext));
        doReturn(ContentSelection.ALL).when(preferenceAppearanceSpy).getContentSelection();
        listViewProviderTestSpy.contentPreferences = preferenceAppearanceSpy;

        listViewProviderTestSpy.onCreate();
        Assert.assertEquals("", 0, listViewProviderTestSpy.getCount());

        listViewProviderTestSpy.onDataSetChanged();
        Assert.assertEquals("", 1, listViewProviderTestSpy.getCount());
    }
}
