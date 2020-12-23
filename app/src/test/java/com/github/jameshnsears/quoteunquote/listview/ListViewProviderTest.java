package com.github.jameshnsears.quoteunquote.listview;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.DatabaseTestHelper;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Ignore

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ListViewProviderTest extends DatabaseTestHelper {
    @Test
    public void listView() {
        insertTestDataSet01();

        final Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WIDGET_ID);

        final Context mockContext = mock(Context.class);

        final ListViewProvider listViewProviderSpy = spy(new ListViewProvider(mockContext, intent));
        doReturn(this.quoteUnquoteModel).when(listViewProviderSpy).getQuoteUnquoteModel(mockContext);

        final PreferenceContent preferenceAppearanceSpy = spy(new PreferenceContent(-1, mockContext));
        doReturn(ContentSelection.ALL).when(preferenceAppearanceSpy).getContentSelection();
        listViewProviderSpy.preferenceContent = preferenceAppearanceSpy;

        listViewProviderSpy.onCreate();
        assertEquals("", 0, listViewProviderSpy.getCount());

        listViewProviderSpy.onDataSetChanged();
        assertEquals("", 1, listViewProviderSpy.getCount());
    }
}
