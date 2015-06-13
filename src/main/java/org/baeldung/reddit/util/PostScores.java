package org.baeldung.reddit.util;

public class PostScores {

    private int score;
    private int upvoteRatio;
    private int noOfComments;

    public PostScores() {
        super();
    }

    public PostScores(final int score, final int upvoteRatio, final int noOfComments) {
        super();
        this.score = score;
        this.upvoteRatio = upvoteRatio;
        this.noOfComments = noOfComments;
    }

    //

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public int getUpvoteRatio() {
        return upvoteRatio;
    }

    public void setUpvoteRatio(final int upvoteRatio) {
        this.upvoteRatio = upvoteRatio;
    }

    public int getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(final int noOfComments) {
        this.noOfComments = noOfComments;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PostScore [score=").append(score).append(", upvoteRatio=").append(upvoteRatio).append(", noOfComments=").append(noOfComments).append("]");
        return builder.toString();
    }

}
