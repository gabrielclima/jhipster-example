package br.com.tribosapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "tags")
    private String tags;

    @Column(name = "show_comments")
    private Boolean showComments;

    @Column(name = "permalink")
    private String permalink;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "posts")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> authors = new HashSet<>();

    @ManyToMany(mappedBy = "posts")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @ManyToMany(mappedBy = "likedPosts")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> peoples = new HashSet<>();

    @ManyToMany(mappedBy = "posts")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tribe> tribes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean isShowComments() {
        return showComments;
    }

    public void setShowComments(Boolean showComments) {
        this.showComments = showComments;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<People> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<People> people) {
        this.authors = people;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<People> getPeoples() {
        return peoples;
    }

    public void setPeoples(Set<People> people) {
        this.peoples = people;
    }

    public Set<Tribe> getTribes() {
        return tribes;
    }

    public void setTribes(Set<Tribe> tribes) {
        this.tribes = tribes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        if(post.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", postId='" + postId + "'" +
            ", postTitle='" + postTitle + "'" +
            ", excerpt='" + excerpt + "'" +
            ", content='" + content + "'" +
            ", status='" + status + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            ", tags='" + tags + "'" +
            ", showComments='" + showComments + "'" +
            ", permalink='" + permalink + "'" +
            '}';
    }
}
