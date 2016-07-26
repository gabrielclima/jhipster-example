package br.com.tribosapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tribe.
 */
@Entity
@Table(name = "tribe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tribe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tribe_id")
    private Long tribeId;

    @Column(name = "tribe_name")
    private String tribeName;

    @Column(name = "tags")
    private String tags;

    @ManyToOne
    private Picture icon;

    @ManyToOne
    private Picture picture;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tribe_posts",
               joinColumns = @JoinColumn(name="tribes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="posts_id", referencedColumnName="ID"))
    private Set<Post> posts = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tribe_events",
               joinColumns = @JoinColumn(name="tribes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="events_id", referencedColumnName="ID"))
    private Set<Event> events = new HashSet<>();

    @ManyToMany(mappedBy = "tribes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> peoples = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTribeId() {
        return tribeId;
    }

    public void setTribeId(Long tribeId) {
        this.tribeId = tribeId;
    }

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Picture getIcon() {
        return icon;
    }

    public void setIcon(Picture picture) {
        this.icon = picture;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tribe tribe = (Tribe) o;
        if(tribe.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tribe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tribe{" +
            "id=" + id +
            ", tribeId='" + tribeId + "'" +
            ", tribeName='" + tribeName + "'" +
            ", tags='" + tags + "'" +
            '}';
    }
}
