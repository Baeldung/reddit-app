package org.baeldung.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Preference implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // can be null for users who don't want notifications
    private String email;

    private String subreddit;

    private boolean sendReplies;

    private int noOfAttempts;

    private int timeInterval;

    private int minScoreRequired;

    private int minUpvoteRatio;

    private boolean keepIfHasComments;

    private boolean deleteAfterLastAttempt;

    private String timezone;

    public Preference() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(final String subreddit) {
        this.subreddit = subreddit;
    }

    public boolean isSendReplies() {
        return sendReplies;
    }

    public void setSendReplies(final boolean sendReplies) {
        this.sendReplies = sendReplies;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Preference [id=").append(id).append(", email=").append(email).append(", subreddit=").append(subreddit).append(", sendReplies=").append(sendReplies).append(", noOfAttempts=").append(noOfAttempts).append(", timeInterval=")
                .append(timeInterval).append(", minScoreRequired=").append(minScoreRequired).append(", timezone=").append(timezone).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (deleteAfterLastAttempt ? 1231 : 1237);
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        result = (prime * result) + (keepIfHasComments ? 1231 : 1237);
        result = (prime * result) + minScoreRequired;
        result = (prime * result) + minUpvoteRatio;
        result = (prime * result) + noOfAttempts;
        result = (prime * result) + (sendReplies ? 1231 : 1237);
        result = (prime * result) + ((subreddit == null) ? 0 : subreddit.hashCode());
        result = (prime * result) + timeInterval;
        result = (prime * result) + ((timezone == null) ? 0 : timezone.hashCode());
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
        final Preference other = (Preference) obj;
        if (deleteAfterLastAttempt != other.deleteAfterLastAttempt) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
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
        if (sendReplies != other.sendReplies) {
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
        if (timezone == null) {
            if (other.timezone != null) {
                return false;
            }
        } else if (!timezone.equals(other.timezone)) {
            return false;
        }
        return true;
    }

}
