package com.amidi.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dish.
 */
@Entity
@Table(name = "dish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dish")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "dish_description")
    private String dishDescription;

    @NotNull
    @Column(name = "dish_price", nullable = false)
    private Float dishPrice;

    @NotNull
    @Column(name = "dish_date", nullable = false)
    private Instant dishDate;

    @NotNull
    @Column(name = "dish_picture_name", nullable = false)
    private String dishPictureName;

    @NotNull
    @Column(name = "dish_picture_url", nullable = false)
    private String dishPictureUrl;

    @Column(name = "dish_picture_alt")
    private String dishPictureAlt;

    @NotNull
    @Column(name = "is_daily_dish", nullable = false)
    private Boolean isDailyDish;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @OneToMany(mappedBy = "dish")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dish" }, allowSetters = true)
    private Set<DishTag> dishTags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "proUsers", "pictures", "dishes", "location", "clients" }, allowSetters = true)
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dish id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishName() {
        return this.dishName;
    }

    public Dish dishName(String dishName) {
        this.setDishName(dishName);
        return this;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDescription() {
        return this.dishDescription;
    }

    public Dish dishDescription(String dishDescription) {
        this.setDishDescription(dishDescription);
        return this;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public Float getDishPrice() {
        return this.dishPrice;
    }

    public Dish dishPrice(Float dishPrice) {
        this.setDishPrice(dishPrice);
        return this;
    }

    public void setDishPrice(Float dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Instant getDishDate() {
        return this.dishDate;
    }

    public Dish dishDate(Instant dishDate) {
        this.setDishDate(dishDate);
        return this;
    }

    public void setDishDate(Instant dishDate) {
        this.dishDate = dishDate;
    }

    public String getDishPictureName() {
        return this.dishPictureName;
    }

    public Dish dishPictureName(String dishPictureName) {
        this.setDishPictureName(dishPictureName);
        return this;
    }

    public void setDishPictureName(String dishPictureName) {
        this.dishPictureName = dishPictureName;
    }

    public String getDishPictureUrl() {
        return this.dishPictureUrl;
    }

    public Dish dishPictureUrl(String dishPictureUrl) {
        this.setDishPictureUrl(dishPictureUrl);
        return this;
    }

    public void setDishPictureUrl(String dishPictureUrl) {
        this.dishPictureUrl = dishPictureUrl;
    }

    public String getDishPictureAlt() {
        return this.dishPictureAlt;
    }

    public Dish dishPictureAlt(String dishPictureAlt) {
        this.setDishPictureAlt(dishPictureAlt);
        return this;
    }

    public void setDishPictureAlt(String dishPictureAlt) {
        this.dishPictureAlt = dishPictureAlt;
    }

    public Boolean getIsDailyDish() {
        return this.isDailyDish;
    }

    public Dish isDailyDish(Boolean isDailyDish) {
        this.setIsDailyDish(isDailyDish);
        return this;
    }

    public void setIsDailyDish(Boolean isDailyDish) {
        this.isDailyDish = isDailyDish;
    }

    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public Dish isAvailable(Boolean isAvailable) {
        this.setIsAvailable(isAvailable);
        return this;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Set<DishTag> getDishTags() {
        return this.dishTags;
    }

    public void setDishTags(Set<DishTag> dishTags) {
        if (this.dishTags != null) {
            this.dishTags.forEach(i -> i.setDish(null));
        }
        if (dishTags != null) {
            dishTags.forEach(i -> i.setDish(this));
        }
        this.dishTags = dishTags;
    }

    public Dish dishTags(Set<DishTag> dishTags) {
        this.setDishTags(dishTags);
        return this;
    }

    public Dish addDishTag(DishTag dishTag) {
        this.dishTags.add(dishTag);
        dishTag.setDish(this);
        return this;
    }

    public Dish removeDishTag(DishTag dishTag) {
        this.dishTags.remove(dishTag);
        dishTag.setDish(null);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Dish restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dish)) {
            return false;
        }
        return id != null && id.equals(((Dish) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dish{" +
            "id=" + getId() +
            ", dishName='" + getDishName() + "'" +
            ", dishDescription='" + getDishDescription() + "'" +
            ", dishPrice=" + getDishPrice() +
            ", dishDate='" + getDishDate() + "'" +
            ", dishPictureName='" + getDishPictureName() + "'" +
            ", dishPictureUrl='" + getDishPictureUrl() + "'" +
            ", dishPictureAlt='" + getDishPictureAlt() + "'" +
            ", isDailyDish='" + getIsDailyDish() + "'" +
            ", isAvailable='" + getIsAvailable() + "'" +
            "}";
    }
}
