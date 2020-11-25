package com.github.jameshnsears.quoteunquote.database.history;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

@Entity(tableName = "previous")
@TypeConverters({ContentSelection.class})
public class PreviousEntity {
    @NonNull
    @ColumnInfo(name = "widget_id")
    public final int widgetId;

    @NonNull
    @ColumnInfo(name = "content_selection")
    public final ContentSelection contentSelection;

    @NonNull
    @ColumnInfo(name = "digest")
    public final String digest;

    @PrimaryKey(autoGenerate = true)
    public int navigation;

    public PreviousEntity(final int widgetId, final ContentSelection contentSelection, final String digest) {
        this.widgetId = widgetId;
        this.contentSelection = contentSelection;
        this.digest = digest;
    }
}
