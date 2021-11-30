package com.amidi.myapp.domain;

import com.amidi.myapp.domain.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProUser.
 */
@Entity
@Table(name = "pro_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prouser")
public class ProUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pro_user_name", nullable = false)
    private String proUserName;

    @NotNull
    @Column(name = "pro_user_surname", nullable = false)
    private String proUserSurname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pro_user_role", nullable = false)
    private UserRole proUserRole;

    @NotNull
    @Column(name = "pro_user_login", nullable = false)
    private String proUserLogin;

    @NotNull
    @Column(name = "pro_user_pwd", nullable = false)
    private String proUserPwd;

    @NotNull
    @Column(name = "pro_user_email", nullable = false)
    private String proUserEmail;

    @NotNull
    @Column(name = "pro_user_phone_number", nullable = false)
    private String proUserPhoneNumber;

    @NotNull
    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated;

    @ManyToOne
    @ManyToOne
    @JsonIgnoreProperties(
        value = { "proUsers", "pictures", "dishes", "location", "location", "dishes", "pictures", "proUsers", "clients" },
        allowSetters = true
    )
    private Restaurant restaurant;

    @ManyToOne
    @ManyToOne
    @JsonIgnoreProperties(
        value = { "proUsers", "pictures", "dishes", "location", "location", "dishes", "pictures", "proUsers", "clients" },
        allowSetters = true
    )
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProUserName() {
        return this.proUserName;
    }

    public ProUser proUserName(String proUserName) {
        this.setProUserName(proUserName);
        return this;
    }

    public void setProUserName(String proUserName) {
        this.proUserName = proUserName;
    }

    public String getProUserSurname() {
        return this.proUserSurname;
    }

    public ProUser proUserSurname(String proUserSurname) {
        this.setProUserSurname(proUserSurname);
        return this;
    }

    public void setProUserSurname(String proUserSurname) {
        this.proUserSurname = proUserSurname;
    }

    public UserRole getProUserRole() {
        return this.proUserRole;
    }

    public ProUser proUserRole(UserRole proUserRole) {
        this.setProUserRole(proUserRole);
        return this;
    }

    public void setProUserRole(UserRole proUserRole) {
        this.proUserRole = proUserRole;
    }

    public String getProUserLogin() {
        return this.proUserLogin;
    }

    public ProUser proUserLogin(String proUserLogin) {
        this.setProUserLogin(proUserLogin);
        return this;
    }

    public void setProUserLogin(String proUserLogin) {
        this.proUserLogin = proUserLogin;
    }

    public String getProUserPwd() {
        return this.proUserPwd;
    }

    public ProUser proUserPwd(String proUserPwd) {
        this.setProUserPwd(proUserPwd);
        return this;
    }

    public void setProUserPwd(String proUserPwd) {
        this.proUserPwd = proUserPwd;
    }

    public String getProUserEmail() {
        return this.proUserEmail;
    }

    public ProUser proUserEmail(String proUserEmail) {
        this.setProUserEmail(proUserEmail);
        return this;
    }

    public void setProUserEmail(String proUserEmail) {
        this.proUserEmail = proUserEmail;
    }

    public String getProUserPhoneNumber() {
        return this.proUserPhoneNumber;
    }

    public ProUser proUserPhoneNumber(String proUserPhoneNumber) {
        this.setProUserPhoneNumber(proUserPhoneNumber);
        return this;
    }

    public void setProUserPhoneNumber(String proUserPhoneNumber) {
        this.proUserPhoneNumber = proUserPhoneNumber;
    }

    public Boolean getIsActivated() {
        return this.isActivated;
    }

    public ProUser isActivated(Boolean isActivated) {
        this.setIsActivated(isActivated);
        return this;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ProUser restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ProUser restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProUser)) {
            return false;
        }
        return id != null && id.equals(((ProUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProUser{" +
            "id=" + getId() +
            ", proUserName='" + getProUserName() + "'" +
            ", proUserSurname='" + getProUserSurname() + "'" +
            ", proUserRole='" + getProUserRole() + "'" +
            ", proUserLogin='" + getProUserLogin() + "'" +
            ", proUserPwd='" + getProUserPwd() + "'" +
            ", proUserEmail='" + getProUserEmail() + "'" +
            ", proUserPhoneNumber='" + getProUserPhoneNumber() + "'" +
            ", isActivated='" + getIsActivated() + "'" +
            "}";
    }
}
