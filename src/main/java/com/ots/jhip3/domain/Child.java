package com.ots.jhip3.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ots.jhip3.domain.util.CustomDateTimeDeserializer;
import com.ots.jhip3.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Child.
 */
@Entity
@Table(name = "CHILD")
public class Child implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 30)        
    @Column(name = "first", length = 30)
    private String first;

    @Size(max = 30)        
    @Column(name = "last", length = 30)
    private String last;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "dob")
    private DateTime dob;
    
    @Column(name = "ssn")
    private String ssn;
    
    @Column(name = "casefk")
    private Integer casefk;

    @ManyToOne
    private Cwcase cwcase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public DateTime getDob() {
        return dob;
    }

    public void setDob(DateTime dob) {
        this.dob = dob;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Integer getCasefk() {
        return casefk;
    }

    public void setCasefk(Integer casefk) {
        this.casefk = casefk;
    }

    public Cwcase getCwcase() {
        return cwcase;
    }

    public void setCwcase(Cwcase cwcase) {
        this.cwcase = cwcase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Child child = (Child) o;

        if ( ! Objects.equals(id, child.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", first='" + first + "'" +
                ", last='" + last + "'" +
                ", dob='" + dob + "'" +
                ", ssn='" + ssn + "'" +
                ", casefk='" + casefk + "'" +
                '}';
    }
}
