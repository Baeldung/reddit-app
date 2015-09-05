package org.baeldung.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.baeldung.reddit.util.Dto;

public class ScheduledPostDto implements Dto {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Long id;

    private String title;

    private String subreddit;

    private String url;

    private boolean sendReplies;

    private String date;

    private int noOfAttempts;

    private int timeInterval;

    private int minScoreRequired;

    private int minTotalVotes;

    private boolean keepIfHasComments;

    private boolean deleteAfterLastAttempt;

    private String status;

    private List<SubmissionResponseDto> detailedStatus;

    public ScheduledPostDto() {
        super();
    }

    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(final String subreddit) {
        this.subreddit = subreddit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isSendReplies() {
        return sendReplies;
    }

    public void setSendReplies(final boolean sendReplies) {
        this.sendReplies = sendReplies;
    }

    public String getDate() {
        return date;
    }

    public Date getSubmissionDateConverted(final String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.date);
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setSubmissionDate(final Date date, final String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.date = dateFormat.format(date);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(final int noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(final int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getMinScoreRequired() {
        return minScoreRequired;
    }

    public void setMinScoreRequired(final int minScoreRequired) {
        this.minScoreRequired = minScoreRequired;
    }

    public int getMinTotalVotes() {
        return minTotalVotes;
    }

    public void setMinTotalVotes(final int minTotalVotes) {
        this.minTotalVotes = minTotalVotes;
    }

    public boolean isKeepIfHasComments() {
        return keepIfHasComments;
    }

    public void setKeepIfHasComments(final boolean keepIfHasComments) {
        this.keepIfHasComments = keepIfHasComments;
    }

    public boolean isDeleteAfterLastAttempt() {
        return deleteAfterLastAttempt;
    }

    public void setDeleteAfterLastAttempt(final boolean deleteAfterLastAttempt) {
        this.deleteAfterLastAttempt = deleteAfterLastAttempt;
    }

    public List<SubmissionResponseDto> getDetailedStatus() {
        return detailedStatus;
    }

    public void setDetailedStatus(final List<SubmissionResponseDto> detailedStatus) {
        this.detailedStatus = detailedStatus;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ScheduledPostDto [title=").append(title).append(", subreddit=").append(subreddit).append(", url=").append(url).append(", submissionDate=").append(date).append(", noOfAttempts=").append(noOfAttempts).append(", deleteAfterLastAttempt=")
                .append(deleteAfterLastAttempt).append("]");
        return builder.toString();
    }

}
