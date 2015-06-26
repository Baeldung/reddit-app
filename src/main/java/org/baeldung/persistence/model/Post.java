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
public class Post implements IEntity {

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

    private boolean deleteAfterLastAttempt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post() {
        super();
    }

    //

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public boolean isDeleteAfterLastAttempt() {
        return deleteAfterLastAttempt;
    }

    public void setDeleteAfterLastAttempt(final boolean deleteAfterLastAttempt) {
        this.deleteAfterLastAttempt = deleteAfterLastAttempt;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Post [title=").append(title).append(", subreddit=").append(subreddit).append(", url=").append(url).append(", submissionDate=").append(submissionDate).append(", isSent=").append(isSent).append(", submissionResponse=")
                .append(submissionResponse).append(", redditID=").append(redditID).append(", noOfAttempts=").append(noOfAttempts).append(", deleteAfterLastAttempt=").append(deleteAfterLastAttempt).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (deleteAfterLastAttempt ? 1231 : 1237);
        result = (prime * result) + (isSent ? 1231 : 1237);
        result = (prime * result) + (keepIfHasComments ? 1231 : 1237);
        result = (prime * result) + minScoreRequired;
        result = (prime * result) + minUpvoteRatio;
        result = (prime * result) + noOfAttempts;
        result = (prime * result) + ((redditID == null) ? 0 : redditID.hashCode());
        result = (prime * result) + (sendReplies ? 1231 : 1237);
        result = (prime * result) + ((submissionDate == null) ? 0 : submissionDate.hashCode());
        result = (prime * result) + ((submissionResponse == null) ? 0 : submissionResponse.hashCode());
        result = (prime * result) + ((subreddit == null) ? 0 : subreddit.hashCode());
        result = (prime * result) + timeInterval;
        result = (prime * result) + ((title == null) ? 0 : title.hashCode());
        result = (prime * result) + ((url == null) ? 0 : url.hashCode());
        result = (prime * result) + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (deleteAfterLastAttempt != other.deleteAfterLastAttempt) {
            return false;
        }
        if (isSent != other.isSent) {
            return false;
        }
        if (keepIfHasComments != other.keepIfHasComments) {
            return false;
        }
        if (minScoreRequired != other.minScoreRequired) {
            return false;
        }
        if (minUpvoteRatio != other.minUpvoteRatio) {
            return false;
        }
        if (noOfAttempts != other.noOfAttempts) {
            return false;
        }
        if (redditID == null) {
            if (other.redditID != null) {
                return false;
            }
        } else if (!redditID.equals(other.redditID)) {
            return false;
        }
        if (sendReplies != other.sendReplies) {
            return false;
        }
        if (submissionDate == null) {
            if (other.submissionDate != null) {
                return false;
            }
        } else if (!submissionDate.equals(other.submissionDate)) {
            return false;
        }
        if (submissionResponse == null) {
            if (other.submissionResponse != null) {
                return false;
            }
        } else if (!submissionResponse.equals(other.submissionResponse)) {
            return false;
        }
        if (subreddit == null) {
            if (other.subreddit != null) {
                return false;
            }
        } else if (!subreddit.equals(other.subreddit)) {
            return false;
        }
        if (timeInterval != other.timeInterval) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

}