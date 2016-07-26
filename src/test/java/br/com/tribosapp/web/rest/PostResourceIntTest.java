package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.Post;
import br.com.tribosapp.repository.PostRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class PostResourceIntTest {


    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;
    private static final String DEFAULT_POST_TITLE = "AAAAA";
    private static final String UPDATED_POST_TITLE = "BBBBB";
    private static final String DEFAULT_EXCERPT = "AAAAA";
    private static final String UPDATED_EXCERPT = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TAGS = "AAAAA";
    private static final String UPDATED_TAGS = "BBBBB";

    private static final Boolean DEFAULT_SHOW_COMMENTS = false;
    private static final Boolean UPDATED_SHOW_COMMENTS = true;
    private static final String DEFAULT_PERMALINK = "AAAAA";
    private static final String UPDATED_PERMALINK = "BBBBB";

    @Inject
    private PostRepository postRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPostMockMvc;

    private Post post;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostResource postResource = new PostResource();
        ReflectionTestUtils.setField(postResource, "postRepository", postRepository);
        this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        post = new Post();
        post.setPostId(DEFAULT_POST_ID);
        post.setPostTitle(DEFAULT_POST_TITLE);
        post.setExcerpt(DEFAULT_EXCERPT);
        post.setContent(DEFAULT_CONTENT);
        post.setStatus(DEFAULT_STATUS);
        post.setCreatedAt(DEFAULT_CREATED_AT);
        post.setUpdatedAt(DEFAULT_UPDATED_AT);
        post.setTags(DEFAULT_TAGS);
        post.setShowComments(DEFAULT_SHOW_COMMENTS);
        post.setPermalink(DEFAULT_PERMALINK);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post

        restPostMockMvc.perform(post("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(post)))
                .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = posts.get(posts.size() - 1);
        assertThat(testPost.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPost.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testPost.getExcerpt()).isEqualTo(DEFAULT_EXCERPT);
        assertThat(testPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPost.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPost.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPost.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPost.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testPost.isShowComments()).isEqualTo(DEFAULT_SHOW_COMMENTS);
        assertThat(testPost.getPermalink()).isEqualTo(DEFAULT_PERMALINK);
    }

    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the posts
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
                .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
                .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE.toString())))
                .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
                .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
                .andExpect(jsonPath("$.[*].showComments").value(hasItem(DEFAULT_SHOW_COMMENTS.booleanValue())))
                .andExpect(jsonPath("$.[*].permalink").value(hasItem(DEFAULT_PERMALINK.toString())));
    }

    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.postTitle").value(DEFAULT_POST_TITLE.toString()))
            .andExpect(jsonPath("$.excerpt").value(DEFAULT_EXCERPT.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.showComments").value(DEFAULT_SHOW_COMMENTS.booleanValue()))
            .andExpect(jsonPath("$.permalink").value(DEFAULT_PERMALINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = new Post();
        updatedPost.setId(post.getId());
        updatedPost.setPostId(UPDATED_POST_ID);
        updatedPost.setPostTitle(UPDATED_POST_TITLE);
        updatedPost.setExcerpt(UPDATED_EXCERPT);
        updatedPost.setContent(UPDATED_CONTENT);
        updatedPost.setStatus(UPDATED_STATUS);
        updatedPost.setCreatedAt(UPDATED_CREATED_AT);
        updatedPost.setUpdatedAt(UPDATED_UPDATED_AT);
        updatedPost.setTags(UPDATED_TAGS);
        updatedPost.setShowComments(UPDATED_SHOW_COMMENTS);
        updatedPost.setPermalink(UPDATED_PERMALINK);

        restPostMockMvc.perform(put("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPost)))
                .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeUpdate);
        Post testPost = posts.get(posts.size() - 1);
        assertThat(testPost.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testPost.getExcerpt()).isEqualTo(UPDATED_EXCERPT);
        assertThat(testPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPost.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPost.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPost.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPost.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testPost.isShowComments()).isEqualTo(UPDATED_SHOW_COMMENTS);
        assertThat(testPost.getPermalink()).isEqualTo(UPDATED_PERMALINK);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Get the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
