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
 * A SocialNetwork.
 */
@Entity
@Table(name = "social_network")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialNetwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "social_network_id")
    private Long socialNetworkId;

    @Column(name = "social_network_name")
    private String socialNetworkName;

    @Column(name = "description")
    private String description;

    @Column(name = "icon")
    private String icon;

    @ManyToMany(mappedBy = "socialNetworks")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> people = new HashSet<>();

    @ManyToMany(mappedBy = "socialNetworks")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocialNetworkId() {
        return socialNetworkId;
    }

    public void setSocialNetworkId(Long socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }

    public String getSocialNetworkName() {
        return socialNetworkName;
    }

    public void setSocialNetworkName(String socialNetworkName) {
        this.socialNetworkName = socialNetworkName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<People> getPeople() {
        return people;
    }

    public void setPeople(Set<People> people) {
        this.people = people;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialNetwork socialNetwork = (SocialNetwork) o;
        if(socialNetwork.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, socialNetwork.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SocialNetwork{" +
            "id=" + id +
            ", socialNetworkId='" + socialNetworkId + "'" +
            ", socialNetworkName='" + socialNetworkName + "'" +
            ", description='" + description + "'" +
            ", icon='" + icon + "'" +
            '}';
    }
}
