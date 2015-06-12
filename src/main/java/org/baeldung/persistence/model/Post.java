package org.baeldung.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subreddit;

    @Column(nullable = false)
    private String url;

    private boolean sendReplies;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submissionDate;

    private boolean isSent;

    private String submissionResponse;

    private String redditID;

    private int noOfAttempts;

    private int timeInterval;

    private int minScoreRequired;

    private int minUpvoteRatio;

    private boolean keepIfHasComments;

    private boolean deleteIfConsumeAttempts;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //

    public Post() {
        super();
    }

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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(final Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(final boolean isSent) {
        this.isSent = isSent;
    }

    public String getSubmissionResponse() {
        return submissionResponse;
    }

    public void setSubmissionResponse(final String submissionResponse) {
        this.submissionResponse = submissionResponse;
    }

    public String getRedditID() {
        return redditID;
    }

    public void setRedditID(final String redditID) {
        this.redditID = redditID;
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

    public int getMinUpvoteRatio() {
        return minUpvoteRatio;
    }

    public void setMinUpvoteRatio(final int minUpvoteRatio) {
        this.minUpvoteRatio = minUpvoteRatio;
    }

    public boolean isKeepIfHasComments() {
        return keepIfHasComments;
    }

    public void setKeepIfHasComments(final boolean keepIfHasComments) {
        this.keepIfHasComments = keepIfHasComments;
    }

    public boolean isDeleteIfConsumeAttempts() {
        return deleteIfConsumeAttempts;
    }

    public void setDeleteIfConsumeAttempts(final boolean deleteIfConsumeAttempts) {
        this.deleteIfConsumeAttempts = deleteIfConsumeAttempts;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Post [title=").append(title).append(", subreddit=").append(subreddit).append(", url=").append(url).append(", submissionDate=").append(submissionDate).append(", isSent=").append(isSent).append(", submissionResponse=")
                .append(submissionResponse).append(", redditID=").append(redditID).append(", noOfAttempts=").append(noOfAttempts).append(", deleteIfConsumeAttempts=").append(deleteIfConsumeAttempts).append("]");
        return builder.toString();
    }

}