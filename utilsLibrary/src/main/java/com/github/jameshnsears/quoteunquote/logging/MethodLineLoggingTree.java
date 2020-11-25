package com.github.jameshnsears.quoteunquote.logging;

import timber.log.Timber.DebugTree;

public final class MethodLineLoggingTree extends DebugTree {
    @Override
    protected String createStackElementTag(final StackTraceElement element) {
        return String.format(
                "%s.%s, %s",
                super.createStackElementTag(element),
                element.getMethodName(),
                element.getLineNumber());
    }
}
