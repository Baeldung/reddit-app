package org.baeldung.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.classifier.RedditClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat dfHour = new SimpleDateFormat("HH");
    private static final int PAGE_SIZE = 2;

    @Autowired
    private PostRepository postReopsitory;

    @Autowired
    private RedditClassifier redditClassifier;

    @RequestMapping("/postSchedule")
    public final String showSchedulePostForm(final Model model) {
        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            model.addAttribute("msg", "Sorry, You do not have enought karma");
            return "submissionResponse";
        }
        return "schedulePostForm";
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public final String schedule(final Model model, @RequestParam final Map<String, String> formParams) throws ParseException {
        logger.info("User scheduling Post with these parameters: " + formParams.entrySet());
        final User user = getCurrentUser();
        final Post post = new Post();
        post.setUser(user);
        post.setSent(false);
        post.setTitle(formParams.get("title"));
        post.setSubreddit(formParams.get("sr"));
        post.setUrl(formParams.get("url"));
        post.setNoOfAttempts(Integer.parseInt(formParams.get("attempt")));
        post.setTimeInterval(Integer.parseInt(formParams.get("interval")));
        post.setMinScoreRequired(Integer.parseInt(formParams.get("score")));

        if (formParams.containsKey("sendreplies")) {
            post.setSendReplies(true);
        }
        post.setSubmissionDate(dateFormat.parse(formParams.get("date")));
        post.setSubmissionResponse("Not sent yet");
        if (post.getSubmissionDate().before(new Date())) {
            model.addAttribute("msg", "Invalid date");
            return "submissionResponse";
        }
        postReopsitory.save(post);

        return "redirect:scheduledPosts";
    }

    @RequestMapping("/scheduledPosts")
    public final String showScheduledPostsPage(final Model model) {
        final User user = getCurrentUser();
        final Page<Post> posts = postReopsitory.findByUser(user, new PageRequest(0, PAGE_SIZE));
        model.addAttribute("posts", posts);
        return "postListView";
    }

    @RequestMapping(value = "/predicatePostResponse", method = RequestMethod.POST)
    @ResponseBody
    public final String predicatePostResponse(@RequestParam(value = "title") final String title, @RequestParam(value = "domain") final String domain) {
        final int hour = Integer.parseInt(dfHour.format(new Date()));
        final int result = redditClassifier.classify(redditClassifier.convertPost(title, domain, hour));
        return (result == RedditClassifier.GOOD) ? "{Good Response}" : "{Bad response}";
    }

    @RequestMapping(value = "/deletePost/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("id") final Long id) {
        postReopsitory.delete(id);
    }

    @RequestMapping(value = "/editPost/{id}", method = RequestMethod.GET)
    public String showEditPostForm(final Model model, @PathVariable final Long id) {
        final Post post = postReopsitory.findOne(id);
        model.addAttribute("post", post);
        model.addAttribute("dateValue", dateFormat.format(post.getSubmissionDate()));
        return "editPostForm";
    }

    @RequestMapping(value = "/updatePost/{id}", method = RequestMethod.POST)
    public String updatePost(final Model model, @PathVariable("id") final Long id, @RequestParam final Map<String, String> formParams) throws ParseException {
        final Post post = postReopsitory.findOne(id);
        post.setTitle(formParams.get("title"));
        post.setSubreddit(formParams.get("sr"));
        post.setUrl(formParams.get("url"));

        post.setNoOfAttempts(Integer.parseInt(formParams.get("attempt")));
        post.setTimeInterval(Integer.parseInt(formParams.get("interval")));
        post.setMinScoreRequired(Integer.parseInt(formParams.get("score")));

        if (formParams.containsKey("sendreplies")) {
            post.setSendReplies(true);
        } else {
            post.setSendReplies(false);
        }
        post.setSubmissionDate(dateFormat.parse(formParams.get("date")));
        if (post.getSubmissionDate().before(new Date())) {
            model.addAttribute("msg", "Invalid date");
            return "submissionResponse";
        }
        postReopsitory.save(post);
        return "redirect:/posts";
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
