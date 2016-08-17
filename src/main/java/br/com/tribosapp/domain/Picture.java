package br.com.tribosapp.domain;

import br.com.tribosapp.security.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

import br.com.tribosapp.domain.enumeration.PictureType;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FOLDER = "C:\\wamp\\www\\tribos\\";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "picture_title")
    private String pictureTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "file")
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PictureType type;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "pictures")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<People> authors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
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

    public Set<People> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<People> people) {
        this.authors = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Picture picture = (Picture) o;
        if (picture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Picture{" + "id=" + id + ", pictureTitle='" + pictureTitle + "'" + ", description='" + description + "'"
                + ", file='" + file + "'" + ", type='" + type + "'" + ", createdAt='" + createdAt + "'"
                + ", updatedAt='" + updatedAt + "'" + '}';
    }

    public static String generateFilename(String originalFilename) {
        StringBuilder filename = new StringBuilder();
        String separator = "_";

        filename.append(System.currentTimeMillis());
        filename.append(separator);
        filename.append(UUID.randomUUID());
        filename.append(".");
        filename.append(FilenameUtils.getExtension(originalFilename));

        return filename.toString();
    }
}
