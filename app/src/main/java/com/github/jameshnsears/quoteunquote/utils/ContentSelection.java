package com.github.jameshnsears.quoteunquote.utils;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.Objects;

public enum ContentSelection {
    ALL(1),
    FAVOURITES(2),
    AUTHOR(3),
    SEARCH(4),
    REPORT(5);

    @NonNull
    private final Integer code;

    ContentSelection(@NonNull final Integer value) {
        this.code = value;
    }

    @TypeConverter
    @NonNull
    public static ContentSelection getContentType(@NonNull final Integer integer) {
        for (final ContentSelection contentSelection : values()) {
            if (Objects.equals(contentSelection.code, integer)) {
                return contentSelection;
            }
        }
        return null;
    }

    @TypeConverter
    @NonNull
    public static Integer getContentTypeInt(@NonNull final ContentSelection contentSelection) {
        return contentSelection.code;
    }

    @NonNull
    public Integer getContentType() {
        return code;
    }
}
