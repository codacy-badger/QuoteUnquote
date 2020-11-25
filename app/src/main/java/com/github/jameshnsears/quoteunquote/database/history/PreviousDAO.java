package com.github.jameshnsears.quoteunquote.database.history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import java.util.List;

@Dao
@TypeConverters({ContentSelection.class})
public interface PreviousDAO {
    @Insert
    void markAsPrevious(PreviousEntity previousEntity);

    @Query("SELECT COUNT(*) FROM PREVIOUS WHERE WIDGET_ID = :widgetId AND CONTENT_SELECTION = :contentSelection")
    int countPrevious(int widgetId, ContentSelection contentSelection);

    @Query("SELECT DIGEST FROM PREVIOUS WHERE WIDGET_ID = :widgetId AND CONTENT_SELECTION = :contentSelection ORDER BY NAVIGATION DESC")
    List<String> getPrevious(int widgetId, ContentSelection contentSelection);

    @Query("SELECT * FROM PREVIOUS WHERE WIDGET_ID = :widgetId AND CONTENT_SELECTION = :contentSelection ORDER BY NAVIGATION DESC LIMIT 1")
    PreviousEntity getNext(int widgetId, ContentSelection contentSelection);

    @Query("DELETE FROM PREVIOUS")
    void deletePrevious();

    @Query("DELETE FROM PREVIOUS WHERE WIDGET_ID = :widgetId")
    void deletePrevious(int widgetId);

    @Query("DELETE FROM PREVIOUS WHERE WIDGET_ID = :widgetId AND CONTENT_SELECTION = :contentSelection")
    void deletePrevious(int widgetId, ContentSelection contentSelection);

    @Query("DELETE FROM PREVIOUS WHERE WIDGET_ID = :widgetId AND CONTENT_SELECTION = :contentSelection AND DIGEST = :digest")
    void deletePrevious(int widgetId, ContentSelection contentSelection, String digest);
}
