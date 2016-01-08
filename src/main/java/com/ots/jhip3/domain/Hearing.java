package com.ots.jhip3.domain;

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

import com.ots.jhip3.domain.enumeration.Statuscode;

/**
 * A Hearing.
 */
@Entity
@Table(name = "HEARING")
public class Hearing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "county_code")
    private Integer countyCode;
    
    @Column(name = "hearing_type")
    private String hearingType;
    
    @Column(name = "court_dept")
    private String courtDept;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "date")
    private DateTime date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Statuscode status;
    
    @Column(name = "caseworker")
    private String caseworker;
    
    @Column(name = "cwcaseid")
    private Integer cwcaseid;
    
    @Lob
    @Column(name = "doc")
    private byte[] doc;
    
    @Lob
    @Column(name = "image")
    private byte[] image;
    
    @Column(name = "summary")
    private String summary;
    
    @Column(name = "attendee_first")
    private String attendeeFirst;
    
    @Column(name = "attendee_last")
    private String attendeeLast;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "time")
    private DateTime time;
    
    @Column(name = "language")
    private String language;

    @ManyToOne
    private Cwcase cwcase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(Integer countyCode) {
        this.countyCode = countyCode;
    }

    public String getHearingType() {
        return hearingType;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    public String getCourtDept() {
        return courtDept;
    }

    public void setCourtDept(String courtDept) {
        this.courtDept = courtDept;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Statuscode getStatus() {
        return status;
    }

    public void setStatus(Statuscode status) {
        this.status = status;
    }

    public String getCaseworker() {
        return caseworker;
    }

    public void setCaseworker(String caseworker) {
        this.caseworker = caseworker;
    }

    public Integer getCwcaseid() {
        return cwcaseid;
    }

    public void setCwcaseid(Integer cwcaseid) {
        this.cwcaseid = cwcaseid;
    }

    public byte[] getDoc() {
        return doc;
    }

    public void setDoc(byte[] doc) {
        this.doc = doc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAttendeeFirst() {
        return attendeeFirst;
    }

    public void setAttendeeFirst(String attendeeFirst) {
        this.attendeeFirst = attendeeFirst;
    }

    public String getAttendeeLast() {
        return attendeeLast;
    }

    public void setAttendeeLast(String attendeeLast) {
        this.attendeeLast = attendeeLast;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

        Hearing hearing = (Hearing) o;

        if ( ! Objects.equals(id, hearing.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hearing{" +
                "id=" + id +
                ", countyCode='" + countyCode + "'" +
                ", hearingType='" + hearingType + "'" +
                ", courtDept='" + courtDept + "'" +
                ", date='" + date + "'" +
                ", status='" + status + "'" +
                ", caseworker='" + caseworker + "'" +
                ", cwcaseid='" + cwcaseid + "'" +
                ", doc='" + doc + "'" +
                ", image='" + image + "'" +
                ", summary='" + summary + "'" +
                ", attendeeFirst='" + attendeeFirst + "'" +
                ", attendeeLast='" + attendeeLast + "'" +
                ", time='" + time + "'" +
                ", language='" + language + "'" +
                '}';
    }
}
