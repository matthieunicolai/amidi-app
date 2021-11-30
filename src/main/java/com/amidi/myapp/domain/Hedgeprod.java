package com.amidi.myapp.domain;

import com.amidi.myapp.domain.enumeration.HedgeprodRole;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Hedgeprod.
 */
@Entity
@Table(name = "hedgeprod")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hedgeprod")
public class Hedgeprod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "h_name", nullable = false)
    private String hName;

    @NotNull
    @Column(name = "h_surname", nullable = false)
    private String hSurname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "h_role", nullable = false)
    private HedgeprodRole hRole;

    @NotNull
    @Column(name = "h_email", nullable = false)
    private String hEmail;

    @Column(name = "h_phone_number")
    private String hPhoneNumber;

    @NotNull
    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hedgeprod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String gethName() {
        return this.hName;
    }

    public Hedgeprod hName(String hName) {
        this.sethName(hName);
        return this;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethSurname() {
        return this.hSurname;
    }

    public Hedgeprod hSurname(String hSurname) {
        this.sethSurname(hSurname);
        return this;
    }

    public void sethSurname(String hSurname) {
        this.hSurname = hSurname;
    }

    public HedgeprodRole gethRole() {
        return this.hRole;
    }

    public Hedgeprod hRole(HedgeprodRole hRole) {
        this.sethRole(hRole);
        return this;
    }

    public void sethRole(HedgeprodRole hRole) {
        this.hRole = hRole;
    }

    public String gethEmail() {
        return this.hEmail;
    }

    public Hedgeprod hEmail(String hEmail) {
        this.sethEmail(hEmail);
        return this;
    }

    public void sethEmail(String hEmail) {
        this.hEmail = hEmail;
    }

    public String gethPhoneNumber() {
        return this.hPhoneNumber;
    }

    public Hedgeprod hPhoneNumber(String hPhoneNumber) {
        this.sethPhoneNumber(hPhoneNumber);
        return this;
    }

    public void sethPhoneNumber(String hPhoneNumber) {
        this.hPhoneNumber = hPhoneNumber;
    }

    public Boolean getIsActivated() {
        return this.isActivated;
    }

    public Hedgeprod isActivated(Boolean isActivated) {
        this.setIsActivated(isActivated);
        return this;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hedgeprod)) {
            return false;
        }
        return id != null && id.equals(((Hedgeprod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hedgeprod{" +
            "id=" + getId() +
            ", hName='" + gethName() + "'" +
            ", hSurname='" + gethSurname() + "'" +
            ", hRole='" + gethRole() + "'" +
            ", hEmail='" + gethEmail() + "'" +
            ", hPhoneNumber='" + gethPhoneNumber() + "'" +
            ", isActivated='" + getIsActivated() + "'" +
            "}";
    }
}
