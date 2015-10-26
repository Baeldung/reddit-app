package org.baeldung.web;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

import org.baeldung.persistence.model.Post;
import org.baeldung.web.dto.query.ScheduledPostDto;
import org.junit.Test;
import org.modelmapper.ModelMapper;

public class ScheduledPostDtoUnitTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void whenConvertPostEntityToScheduledPostDto_thenCorrect() {
        final Post post = new Post();
        post.setId(Long.valueOf(1));
        post.setTitle(randomAlphabetic(6));
        post.setSubreddit(randomAlphabetic(5));
        post.setUrl("www.test.com");

        final ScheduledPostDto postDto = modelMapper.map(post, ScheduledPostDto.class);
        assertEquals(post.getId(), postDto.getId());
        assertEquals(post.getTitle(), postDto.getTitle());
        assertEquals(post.getSubreddit(), postDto.getSubreddit());
        assertEquals(post.getUrl(), postDto.getUrl());
    }

    @Test
    public void whenConvertScheduledPostDtoToPostEntity_thenCorrect() {
        final ScheduledPostDto postDto = new ScheduledPostDto();
        postDto.setId(Long.valueOf(1));
        postDto.setTitle(randomAlphabetic(6));
        postDto.setSubreddit(randomAlphabetic(5));
        postDto.setUrl("www.test.com");

        final Post post = modelMapper.map(postDto, Post.class);
        assertEquals(postDto.getId(), post.getId());
        assertEquals(postDto.getTitle(), post.getTitle());
        assertEquals(postDto.getSubreddit(), post.getSubreddit());
        assertEquals(postDto.getUrl(), post.getUrl());
    }

}
