package br.com.tribosapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "description")
    private String description;

    @Column(name = "period")
    private String period;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "tags")
    private String tags;

    @Column(name = "link")
    private String link;

    @Column(name = "latitude", precision = 10, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 2)
    private BigDecimal longitude;

    @ManyToOne
    private Picture picture;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_posts",
            joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "posts_id", referencedColumnName = "ID"))
    private Set<Post> posts = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_social_networks",
            joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "social_networks_id", referencedColumnName = "ID"))
    private Set<SocialNetwork> socialNetworks = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> people = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tribe> tribes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
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

    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(Set<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public Set<People> getPeople() {
        return people;
    }

    public void setPeople(Set<People> people) {
        this.people = people;
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
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventId='" + eventId + "'" +
                ", eventName='" + eventName + "'" +
                ", description='" + description + "'" +
                ", period='" + period + "'" +
                ", createdAt='" + createdAt + "'" +
                ", tags='" + tags + "'" +
                ", link='" + link + "'" +
                ", latitude='" + latitude + "'" +
                ", longitude='" + longitude + "'" +
                ", picture='" + picture + "'" +
                '}';
    }
}
