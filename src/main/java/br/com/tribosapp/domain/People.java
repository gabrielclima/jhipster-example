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

import br.com.tribosapp.domain.enumeration.Gender;

/**
 * A People.
 */
@Entity
@Table(name = "people")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "people_id")
    private Long peopleId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "preffered_tribes")
    private String prefferedTribes;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "people_social_network",
               joinColumns = @JoinColumn(name="people_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="social_networks_id", referencedColumnName="ID"))
    private Set<SocialNetwork> socialNetworks = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "people_event",
               joinColumns = @JoinColumn(name="people_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="events_id", referencedColumnName="ID"))
    private Set<Event> events = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "people_liked_posts",
               joinColumns = @JoinColumn(name="people_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="liked_posts_id", referencedColumnName="ID"))
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "people")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "people_tribes",
               joinColumns = @JoinColumn(name="people_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tribes_id", referencedColumnName="ID"))
    private Set<Tribe> tribes = new HashSet<>();

    @OneToMany(mappedBy = "people")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Setting> settings = new HashSet<>();

    @ManyToOne
    private Post posts;

    @ManyToOne
    private Picture pictures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Long peopleId) {
        this.peopleId = peopleId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPrefferedTribes() {
        return prefferedTribes;
    }

    public void setPrefferedTribes(String prefferedTribes) {
        this.prefferedTribes = prefferedTribes;
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

    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(Set<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Post> posts) {
        this.likedPosts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tribe> getTribes() {
        return tribes;
    }

    public void setTribes(Set<Tribe> tribes) {
        this.tribes = tribes;
    }

    public Set<Setting> getSettings() {
        return settings;
    }

    public void setSettings(Set<Setting> settings) {
        this.settings = settings;
    }

    public Post getPosts() {
        return posts;
    }

    public void setPosts(Post post) {
        this.posts = post;
    }

    public Picture getPictures() {
        return pictures;
    }

    public void setPictures(Picture picture) {
        this.pictures = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        People people = (People) o;
        if(people.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, people.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "People{" +
            "id=" + id +
            ", peopleId='" + peopleId + "'" +
            ", birthDate='" + birthDate + "'" +
            ", gender='" + gender + "'" +
            ", prefferedTribes='" + prefferedTribes + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            '}';
    }
}
