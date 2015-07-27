package org.baeldung.web;

import org.baeldung.persistence.model.Post;

public class SimplePostDto {

    private long id;
    private String title;
    private String submissionDate;
    private String submissionResponse;
    private int noOfAttempts;

    public SimplePostDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SimplePostDto(final long id, final String title, final String submissionDate, final String submissionResponse, final int noOfAttempts) {
        super();
        this.id = id;
        this.title = title;
        this.submissionDate = submissionDate;
        this.submissionResponse = submissionResponse;
        this.noOfAttempts = noOfAttempts;
    }

    public SimplePostDto(final Post post, final String date) {
        super();
        this.id = post.getId();
        this.title = post.getTitle();
        this.submissionDate = date;
        this.submissionResponse = post.getSubmissionResponse();
        this.noOfAttempts = post.getNoOfAttempts();
    }

    //

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(final String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSubmissionResponse() {
        return submissionResponse;
    }

    public void setSubmissionResponse(final String submissionResponse) {
        this.submissionResponse = submissionResponse;
    }

    public int getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(final int noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SimplePost [id=").append(id).append(", title=").append(title).append(", submissionDate=").append(submissionDate).append(", submissionResponse=").append(submissionResponse).append(", noOfAttempts=").append(noOfAttempts).append("]");
        return builder.toString();
    }
}
