package com.example.android.yournews;

/**
 * This Class is the main for Story data retrieved from the Guardian web site using this Query link:
 * https://content.guardianapis.com/search?q=debate%20AND%20(economy%20OR%20immigration%20education)&tag=politics/politics&from-date=2018-01-01&to-date=2018-06-01&page-size=100&show-fields=trailText,headline,thumbnail,shortUrl&api-key=test
 * Which will retrieve data :-
 * size=100 story
 * from-date=2018-01-01 to-date=2018-06-01
 * with these fields:
 * Section name
 * publishing date "with exact time"
 * author
 * story headline
 * trail text for the story
 * short URL for the story on the guardian website
 * thumbnail URL
 */

public class Story {
    private String mSectionName;
    private String mDate;
    private String mAuthor;
    private String mHeadline;
    private String mTrailText;
    private String mShortUrl;
    private String mThumbnailUrl;

    public Story(String sectionName, String date, String author, String headline, String trailText, String shortUrl, String thumbnailUrl) {
        mSectionName = sectionName;
        mDate = date;
        mAuthor = author;
        mHeadline = headline;
        mTrailText = trailText;
        mShortUrl = shortUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getTrailText() {
        return mTrailText;
    }

    public String getShortUrl() {
        return mShortUrl;
    }

    public String getThumbnailUrl() { return mThumbnailUrl; }
}
