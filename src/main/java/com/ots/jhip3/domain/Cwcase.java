package com.ots.jhip3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ots.jhip3.domain.util.CustomDateTimeDeserializer;
import com.ots.jhip3.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cwcase.
 */
@Entity
@Table(name = "CWCASE")
public class Cwcase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "createdate")
    private DateTime createdate;
    
    @Column(name = "casestatus")
    private String casestatus;

    @OneToMany(mappedBy = "cwcase")
    @JsonIgnore
    private Set<Child> childs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(DateTime createdate) {
        this.createdate = createdate;
    }

    public String getCasestatus() {
        return casestatus;
    }

    public void setCasestatus(String casestatus) {
        this.casestatus = casestatus;
    }

    public Set<Child> getChilds() {
        return childs;
    }

    public void setChilds(Set<Child> childs) {
        this.childs = childs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cwcase cwcase = (Cwcase) o;

        if ( ! Objects.equals(id, cwcase.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cwcase{" +
                "id=" + id +
                ", createdate='" + createdate + "'" +
                ", casestatus='" + casestatus + "'" +
                '}';
    }
}
