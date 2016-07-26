package br.com.tribosapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Setting.
 */
@Entity
@Table(name = "setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "setting_id")
    private Long settingId;

    @Column(name = "setting_key")
    private String settingKey;

    @Column(name = "setting_value")
    private String settingValue;

    @ManyToOne
    private People people;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Setting setting = (Setting) o;
        if(setting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, setting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Setting{" +
            "id=" + id +
            ", settingId='" + settingId + "'" +
            ", settingKey='" + settingKey + "'" +
            ", settingValue='" + settingValue + "'" +
            '}';
    }
}
