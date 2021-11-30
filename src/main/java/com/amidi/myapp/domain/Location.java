package com.amidi.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "location")
    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "proUsers", "pictures", "dishes", "location", "location", "dishes", "pictures", "proUsers", "clients" },
        allowSetters = true
    )
    private Set<Restaurant> restaurants = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "proUsers", "pictures", "dishes", "location", "location", "dishes", "pictures", "proUsers", "clients" },
        allowSetters = true
    )
    private Set<Restaurant> restaurants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Location country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Set<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.setLocation(null));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.setLocation(this));
        }
        this.restaurants = restaurants;
    }

    public Location restaurants(Set<Restaurant> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Location addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.setLocation(this);
        return this;
    }

    public Location removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.setLocation(null);
        return this;
    }

    public Set<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.setLocation(null));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.setLocation(this));
        }
        this.restaurants = restaurants;
    }

    public Location restaurants(Set<Restaurant> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Location addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.setLocation(this);
        return this;
    }

    public Location removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.setLocation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
