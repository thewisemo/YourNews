package com.example.android.yournews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StoryAdapter extends ArrayAdapter<Story> {
    private static final String DATE_SEPARATOR = "T";
    private static final String TRIAL_TEXT_SYNTAX1 = "<strong>Letters: ";

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     * @param context     The current context. Used to inflate the layout file.
     * @param stories A List of AndroidFlavor objects to display in a list
     */
    public StoryAdapter(MainActivity context, ArrayList<Story> stories) {
        super(context, 0, stories);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link Story} object located at this position in the list
        Story currentStory = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID sectionName
        TextView sectionNameTextView = listItemView.findViewById(R.id.sectionName);
        // Change the section name text color using the getSectionNameColor helper method
        assert currentStory != null;
        sectionNameTextView.setTextColor(getSectionNameColor(currentStory.getSectionName()));
        // Get the section name value from the current Story object and
        // set this text on the section name TextView
        sectionNameTextView.setText(currentStory.getSectionName());
        ///// DATE SPLIT /////
        String originalDateValue = currentStory.getDate();
        String date;
        String time;
        if (originalDateValue.contains(DATE_SEPARATOR)) {
            String[] parts = originalDateValue.split(DATE_SEPARATOR);
            date = parts[0];
            time = parts[1];
            time = time.substring(0, time.length() - 4);
        } else {
            time = getContext().getString(R.string.not_defined);
            date = originalDateValue;
        }
        ///// Setting date & time TVs /////
        TextView dateTv = listItemView.findViewById(R.id.dateTv);
        dateTv.setText(date);
        TextView timeTv = listItemView.findViewById(R.id.timeTv);
        timeTv.setText(time);
        // Find the TextView in the list_item.xml layout with the ID headline
        TextView headlineTextView = listItemView.findViewById(R.id.headline);
        headlineTextView.setText(currentStory.getHeadline());

        ////// TRIAL TXT WEB SYNTAX SPLIT //////
        String originalTrialTextValue = currentStory.getTrailText();
        String trialText;
        if (originalTrialTextValue.contains(TRIAL_TEXT_SYNTAX1)) {
            String[] parts = originalTrialTextValue.split(TRIAL_TEXT_SYNTAX1);
            trialText = parts[1];
        } else {
            trialText = originalTrialTextValue;
        }
        // Find the TextView in the list_item.xml layout with the ID trailText
        TextView trailTextTextView = listItemView.findViewById(R.id.trailText);
        trailTextTextView.setText(trialText);

        // Find the TextView in the list_item.xml layout with the ID shortUrl
        TextView shortUrlTextView = listItemView.findViewById(R.id.shortUrl);
        shortUrlTextView.setText(currentStory.getShortUrl());

        // Find the ImageView in the list_item.xml layout with ID story_thumb
        ImageView storyThumbnail = listItemView.findViewById(R.id.story_thumb);
        // Glide to get the image in the thumbnail string URL
        Glide.with(getContext())
                .asBitmap()
                .load(currentStory.getThumbnailUrl())
                .into(storyThumbnail);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
    // Helper method to change the section name text color
    private int getSectionNameColor (String sectionName) {
        int storySectionNameColorRID;
        switch (sectionName) {
            case "":
            case "Business":
                storySectionNameColorRID = R.color.business;
                break;
            case "UK news":
                storySectionNameColorRID = R.color.ukNews;
                break;
            case "Politics":
                storySectionNameColorRID = R.color.politics;
                break;
            case "News":
                storySectionNameColorRID = R.color.news;
                break;
            case "Opinion":
                storySectionNameColorRID = R.color.opinion;
                break;
            case "Education":
                storySectionNameColorRID = R.color.education;
                break;
            case "Media":
                storySectionNameColorRID = R.color.media;
                break;
            case "WorldNews":
                storySectionNameColorRID = R.color.worldNews;
                break;
            case "Science":
                storySectionNameColorRID = R.color.science;
                break;
            case "Global development":
                storySectionNameColorRID = R.color.globalDevelopment;
                break;
            case "Higher Education Network":
                storySectionNameColorRID = R.color.higherEducationNetwork;
                break;
            default:
                storySectionNameColorRID = R.color.sectionName;
                break;
        }
        return ContextCompat.getColor(getContext(), storySectionNameColorRID);
    }
}
