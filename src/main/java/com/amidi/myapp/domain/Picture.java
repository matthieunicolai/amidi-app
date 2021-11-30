package com.amidi.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "picture_name", nullable = false)
    private String pictureName;

    @NotNull
    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @NotNull
    @Column(name = "picture_alt", nullable = false)
    private String pictureAlt;

    @NotNull
    @Column(name = "is_logo", nullable = false)
    private Boolean isLogo;

    @NotNull
    @Column(name = "is_displayed", nullable = false)
    private Boolean isDisplayed;

    @ManyToOne
    @JsonIgnoreProperties(value = { "proUsers", "pictures", "dishes", "location", "clients" }, allowSetters = true)
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Picture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return this.pictureName;
    }

    public Picture pictureName(String pictureName) {
        this.setPictureName(pictureName);
        return this;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public Picture pictureUrl(String pictureUrl) {
        this.setPictureUrl(pictureUrl);
        return this;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureAlt() {
        return this.pictureAlt;
    }

    public Picture pictureAlt(String pictureAlt) {
        this.setPictureAlt(pictureAlt);
        return this;
    }

    public void setPictureAlt(String pictureAlt) {
        this.pictureAlt = pictureAlt;
    }

    public Boolean getIsLogo() {
        return this.isLogo;
    }

    public Picture isLogo(Boolean isLogo) {
        this.setIsLogo(isLogo);
        return this;
    }

    public void setIsLogo(Boolean isLogo) {
        this.isLogo = isLogo;
    }

    public Boolean getIsDisplayed() {
        return this.isDisplayed;
    }

    public Picture isDisplayed(Boolean isDisplayed) {
        this.setIsDisplayed(isDisplayed);
        return this;
    }

    public void setIsDisplayed(Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Picture restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Picture)) {
            return false;
        }
        return id != null && id.equals(((Picture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Picture{" +
            "id=" + getId() +
            ", pictureName='" + getPictureName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", pictureAlt='" + getPictureAlt() + "'" +
            ", isLogo='" + getIsLogo() + "'" +
            ", isDisplayed='" + getIsDisplayed() + "'" +
            "}";
    }
}
