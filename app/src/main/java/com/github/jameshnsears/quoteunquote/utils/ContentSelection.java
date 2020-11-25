package com.github.jameshnsears.quoteunquote.utils;

import androidx.room.TypeConverter;

import java.util.Objects;

public enum ContentSelection {
    ALL(1),
    FAVOURITES(2),
    AUTHOR(3),
    SEARCH(4),
    REPORT(5);

    private final Integer code;

    ContentSelection(final Integer value) {
        this.code = value;
    }

    @TypeConverter
    public static ContentSelection getContentType(final Integer integer) {
        for (final ContentSelection contentSelection : values()) {
            if (Objects.equals(contentSelection.code, integer)) {
                return contentSelection;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getContentTypeInt(final ContentSelection contentSelection) {
        return contentSelection.code;
    }

    public Integer getContentType() {
        return code;
    }
}
