package com.github.jameshnsears.quoteunquote.database;

import android.content.Context;

import com.github.jameshnsears.quoteunquote.database.history.AbstractHistoryDatabase;
import com.github.jameshnsears.quoteunquote.database.history.FavouriteDAO;
import com.github.jameshnsears.quoteunquote.database.history.FavouriteEntity;
import com.github.jameshnsears.quoteunquote.database.history.PreviousDAO;
import com.github.jameshnsears.quoteunquote.database.history.PreviousEntity;
import com.github.jameshnsears.quoteunquote.database.history.ReportedDAO;
import com.github.jameshnsears.quoteunquote.database.history.ReportedEntity;
import com.github.jameshnsears.quoteunquote.database.quotation.AbstractQuotationDatabase;
import com.github.jameshnsears.quoteunquote.database.quotation.AuthorPOJO;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationDAO;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

public class DatabaseRepository {
    public static final String DEFAULT_QUOTATION_DIGEST = "1624c314";
    protected final SecureRandom secureRandom = new SecureRandom();
    protected AbstractQuotationDatabase abstractQuotationDatabase;
    protected QuotationDAO quotationDAO;
    protected AbstractHistoryDatabase abstractHistoryDatabase;
    protected PreviousDAO previousDAO;
    protected FavouriteDAO favouriteDAO;
    protected ReportedDAO reportedDAO;

    public DatabaseRepository() {
    }

    public DatabaseRepository(final Context context) {
        abstractQuotationDatabase = AbstractQuotationDatabase.getDatabase(context);
        quotationDAO = abstractQuotationDatabase.quotationsDAO();
        abstractHistoryDatabase = AbstractHistoryDatabase.getDatabase(context);
        previousDAO = abstractHistoryDatabase.contentDAO();
        favouriteDAO = abstractHistoryDatabase.favouritesDAO();
        reportedDAO = abstractHistoryDatabase.reportedDAO();
    }

    public Single<Integer> countAll() {
        return quotationDAO.countAll();
    }

    public int countPrevious(final int widgetId, final ContentSelection contentSelection) {
        return previousDAO.countPrevious(widgetId, contentSelection);
    }

    public int countPrevious(final int widgetId, final ContentSelection contentSelection, final String criteria) {
        List<String> digestsPrevious;
        List<String> availableDigests;

        if (contentSelection == ContentSelection.AUTHOR) {
            digestsPrevious = previousDAO.getPrevious(widgetId, ContentSelection.AUTHOR);
            availableDigests = quotationDAO.getAuthors(criteria);
        } else {
            digestsPrevious = previousDAO.getPrevious(widgetId, ContentSelection.SEARCH);
            availableDigests = quotationDAO.getQuotationText("%" + criteria + "%");
        }

        int countDigestInPrevious = 0;
        for (final String digest : availableDigests) {
            if (digestsPrevious.contains(digest)) {
                countDigestInPrevious++;
            }
        }

        return countDigestInPrevious;
    }

    public Single<Integer> countFavourites() {
        return favouriteDAO.countFavourites();
    }

    public QuotationEntity getNext(final int widgetId, final ContentSelection contentSelection) {
        return getQuotation(previousDAO.getNext(widgetId, contentSelection).digest);
    }

    public List<String> getPrevious(final int widgetId, final ContentSelection contentSelection) {
        final List<String> previousOrdered = previousDAO.getPrevious(widgetId, contentSelection);
        logDigests(previousOrdered);

        return previousOrdered;
    }

    public List<String> getFavourites() {
        final List<String> favouriteQuotations = favouriteDAO.getFavourites();
        logDigests(favouriteQuotations);

        return favouriteQuotations;
    }

    public Single<List<AuthorPOJO>> getAuthorsWithAtLeastFiveQuotations() {
        return quotationDAO.authorsWithAtLeastFiveQuotations();
    }

    public Single<List<AuthorPOJO>> getAuthors() {
        return quotationDAO.authors();
    }

    public Integer countQuotationsText(final String text) {
        if ("".equals(text)) {
            return 0;
        } else {
            return quotationDAO.countQuotationsText("%" + text + "%");
        }
    }

    public QuotationEntity getQuotation(final String digest) {
        return quotationDAO.getQuotation(digest);
    }

    public void markAsPrevious(final int widgetId, final ContentSelection contentSelection, final String digest) {
        Timber.d("%d: contentType=%d; digest=%s", widgetId, contentSelection.getContentType(), digest);

        previousDAO.markAsPrevious(new PreviousEntity(widgetId, contentSelection, digest));
    }

    public void markAsFavourite(final String digest) {
        Timber.d("digest=%s", digest);

        if (favouriteDAO.countIsFavourite(digest) == 0 && quotationDAO.getQuotation(digest) != null) {
            favouriteDAO.markAsFavourite(new FavouriteEntity(digest));
        }
    }

    public void markAsReported(final String digest) {
        if (reportedDAO.countIsReported(digest) == 0) {
            Timber.d("digest=%s", digest);
            reportedDAO.markAsReported(new ReportedEntity(digest));
        }
    }

    public QuotationEntity getNext(final int widgetId, final ContentSelection contentSelection, final String searchString, final boolean randomNext)
            throws NoNextQuotationAvailableException {
        Timber.d("%d: contentType=%d; searchString=%s", widgetId, contentSelection.getContentType(), searchString);

        List<String> availableQuotations = new ArrayList<>();

        switch (contentSelection) {
            case ALL:
                availableQuotations
                        = quotationDAO.getAll(
                        getPrevious(widgetId, contentSelection));
                break;

            case FAVOURITES:
                availableQuotations
                        = favouriteDAO.getFavourites(getPrevious(widgetId, contentSelection));
                break;

            case AUTHOR:
                availableQuotations
                        = quotationDAO.getAuthors(
                        searchString, getPrevious(widgetId, contentSelection));
                break;

            case SEARCH:
                availableQuotations
                        = quotationDAO.getQuotationText(
                        "%" + searchString + "%", getPrevious(widgetId, contentSelection));
                break;

            default:
                Timber.e(contentSelection.getContentType().toString());
                break;
        }

        if (availableQuotations.isEmpty()) {
            throw new NoNextQuotationAvailableException(contentSelection);
        }

        if (randomNext) {
            return getQuotation(availableQuotations.get(getRandomIndex(availableQuotations)));
        }

        return getQuotation(availableQuotations.get(0));
    }

    public int getRandomIndex(final List<String> availableNextQuotations) {
        return secureRandom.nextInt(availableNextQuotations.size());
    }

    private void logDigests(final List<String> digests) {
        int index = 0;
        for (final String digest : digests) {
            Timber.d("index=%d, digest=%s", index, digest);
            index += 1;
        }
    }

    public void deleteFavourite(final int widgetId, final String digest) {
        Timber.d("%d: digest=%s", widgetId, digest);
        favouriteDAO.deleteFavourite(digest);
        previousDAO.deletePrevious(widgetId, ContentSelection.FAVOURITES, digest);
    }

    public void deleteFavourites() {
        favouriteDAO.deleteFavourites();
    }

    public void deletePrevious(final int widgetId, final ContentSelection contentSelection) {
        Timber.d("%d: contentType=%d", widgetId, contentSelection.getContentType());
        previousDAO.deletePrevious(widgetId, contentSelection);
    }

    public void deletePrevious() {
        previousDAO.deletePrevious();
    }

    public void deletePrevious(final int widgetId) {
        Timber.d("widgetId=%d", widgetId);
        previousDAO.deletePrevious(widgetId);
    }

    public void deleteReported() {
        reportedDAO.deleteReported();
    }

    public Integer countIsFavourite(final String digest) {
        return favouriteDAO.countIsFavourite(digest);
    }

    public Integer countIsReported(final String digest) {
        return reportedDAO.countIsReported(digest);
    }
}
