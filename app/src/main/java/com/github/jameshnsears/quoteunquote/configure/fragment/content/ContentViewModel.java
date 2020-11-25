package com.github.jameshnsears.quoteunquote.configure.fragment.content;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.github.jameshnsears.quoteunquote.BuildConfig;
import com.github.jameshnsears.quoteunquote.cloud.CloudFavouritesHelper;
import com.github.jameshnsears.quoteunquote.cloud.RequestSave;
import com.github.jameshnsears.quoteunquote.database.DatabaseRepository;
import com.github.jameshnsears.quoteunquote.database.quotation.AuthorPOJO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Single;
import timber.log.Timber;

public class ContentViewModel extends AndroidViewModel {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    public List<AuthorPOJO> authorPOJOList;
    protected DatabaseRepository databaseRepository;

    public ContentViewModel(final Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public Single<Integer> countAll() {
        return databaseRepository.countAll();
    }

    public Single<List<AuthorPOJO>> authors() {
        if (BuildConfig.USE_PROD_DB) {
            return databaseRepository.getAuthorsWithAtLeastFiveQuotations();
        } else {
            return databaseRepository.getAuthors();
        }
    }

    public List<String> authorsSorted(final List<AuthorPOJO> unsortedAuthorPOJOList) {
        this.authorPOJOList = unsortedAuthorPOJOList;

        Collections.sort(unsortedAuthorPOJOList);
        final ArrayList<String> authors = new ArrayList<>();
        for (final AuthorPOJO authorPOJO : unsortedAuthorPOJOList) {
            authors.add(authorPOJO.author);
        }
        return authors;
    }

    public int countAuthorQuotations(final String author) {
        for (final AuthorPOJO authorPOJO : this.authorPOJOList) {
            if (authorPOJO.author.equals(author)) {
                return authorPOJO.count;
            }
        }
        return -1;
    }

    public int authorsIndex(final String author) {
        int index = 0;
        for (final AuthorPOJO authorPOJO : this.authorPOJOList) {
            if (authorPOJO.author.equals(author)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Single<Integer> countFavourites() {
        return databaseRepository.countFavourites();
    }

    public Integer countQuotationWithText(final String text) {
        return databaseRepository.countQuotationsText(text);
    }

    public String getSavePayload() {
        final Future<String> future = executorService.submit(() -> {

            RequestSave requestSave = new RequestSave();
            requestSave.code = CloudFavouritesHelper.getLocalCode();
            requestSave.digests = new ArrayList<>(databaseRepository.getFavourites());

            Gson gson = new Gson();
            return gson.toJson(requestSave);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Timber.w(e.toString());
            Thread.currentThread().interrupt();
            return "";
        }
    }
}
