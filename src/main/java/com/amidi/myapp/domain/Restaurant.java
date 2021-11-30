package com.amidi.myapp.domain;

import com.amidi.myapp.domain.enumeration.RestaurationType;
import com.amidi.myapp.domain.enumeration.SubscriptionType;
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
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @NotNull
    @Column(name = "restaurant_address", nullable = false)
    private String restaurantAddress;

    @NotNull
    @Column(name = "restaurant_address_cmp", nullable = false)
    private String restaurantAddressCmp;

    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_type")
    private RestaurationType restaurantType;

    @NotNull
    @Column(name = "is_sub", nullable = false)
    private Boolean isSub;

    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_subscription")
    private SubscriptionType restaurantSubscription;

    @Column(name = "restaurant_sub_date")
    private Instant restaurantSubDate;

    @Column(name = "restaurant_description")
    private String restaurantDescription;

    @NotNull
    @Column(name = "restaurant_phone_number", nullable = false)
    private String restaurantPhoneNumber;

    @NotNull
    @Column(name = "restaurant_email", nullable = false)
    private String restaurantEmail;

    @Column(name = "restaurant_web_site")
    private String restaurantWebSite;

    @NotNull
    @Column(name = "restaurant_latitude", nullable = false)
    private Float restaurantLatitude;

    @NotNull
    @Column(name = "restaurant_longitude", nullable = false)
    private Float restaurantLongitude;

    @NotNull
    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated;

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurant", "restaurant" }, allowSetters = true)
    private Set<ProUser> proUsers = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurant", "restaurant" }, allowSetters = true)
    private Set<Picture> pictures = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dishTags", "restaurant", "restaurant" }, allowSetters = true)
    private Set<Dish> dishes = new HashSet<>();

    @ManyToOne
    @ManyToOne
    @JsonIgnoreProperties(value = { "restaurants", "restaurants" }, allowSetters = true)
    private Location location;

    @ManyToOne
    @ManyToOne
    @JsonIgnoreProperties(value = { "restaurants", "restaurants" }, allowSetters = true)
    private Location location;

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dishTags", "restaurant", "restaurant" }, allowSetters = true)
    private Set<Dish> dishes = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurant", "restaurant" }, allowSetters = true)
    private Set<Picture> pictures = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurant", "restaurant" }, allowSetters = true)
    private Set<ProUser> proUsers = new HashSet<>();

    @ManyToMany(mappedBy = "restaurants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurants" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public Restaurant restaurantName(String restaurantName) {
        this.setRestaurantName(restaurantName);
        return this;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return this.restaurantAddress;
    }

    public Restaurant restaurantAddress(String restaurantAddress) {
        this.setRestaurantAddress(restaurantAddress);
        return this;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantAddressCmp() {
        return this.restaurantAddressCmp;
    }

    public Restaurant restaurantAddressCmp(String restaurantAddressCmp) {
        this.setRestaurantAddressCmp(restaurantAddressCmp);
        return this;
    }

    public void setRestaurantAddressCmp(String restaurantAddressCmp) {
        this.restaurantAddressCmp = restaurantAddressCmp;
    }

    public RestaurationType getRestaurantType() {
        return this.restaurantType;
    }

    public Restaurant restaurantType(RestaurationType restaurantType) {
        this.setRestaurantType(restaurantType);
        return this;
    }

    public void setRestaurantType(RestaurationType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public Boolean getIsSub() {
        return this.isSub;
    }

    public Restaurant isSub(Boolean isSub) {
        this.setIsSub(isSub);
        return this;
    }

    public void setIsSub(Boolean isSub) {
        this.isSub = isSub;
    }

    public SubscriptionType getRestaurantSubscription() {
        return this.restaurantSubscription;
    }

    public Restaurant restaurantSubscription(SubscriptionType restaurantSubscription) {
        this.setRestaurantSubscription(restaurantSubscription);
        return this;
    }

    public void setRestaurantSubscription(SubscriptionType restaurantSubscription) {
        this.restaurantSubscription = restaurantSubscription;
    }

    public Instant getRestaurantSubDate() {
        return this.restaurantSubDate;
    }

    public Restaurant restaurantSubDate(Instant restaurantSubDate) {
        this.setRestaurantSubDate(restaurantSubDate);
        return this;
    }

    public void setRestaurantSubDate(Instant restaurantSubDate) {
        this.restaurantSubDate = restaurantSubDate;
    }

    public String getRestaurantDescription() {
        return this.restaurantDescription;
    }

    public Restaurant restaurantDescription(String restaurantDescription) {
        this.setRestaurantDescription(restaurantDescription);
        return this;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public String getRestaurantPhoneNumber() {
        return this.restaurantPhoneNumber;
    }

    public Restaurant restaurantPhoneNumber(String restaurantPhoneNumber) {
        this.setRestaurantPhoneNumber(restaurantPhoneNumber);
        return this;
    }

    public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
        this.restaurantPhoneNumber = restaurantPhoneNumber;
    }

    public String getRestaurantEmail() {
        return this.restaurantEmail;
    }

    public Restaurant restaurantEmail(String restaurantEmail) {
        this.setRestaurantEmail(restaurantEmail);
        return this;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantWebSite() {
        return this.restaurantWebSite;
    }

    public Restaurant restaurantWebSite(String restaurantWebSite) {
        this.setRestaurantWebSite(restaurantWebSite);
        return this;
    }

    public void setRestaurantWebSite(String restaurantWebSite) {
        this.restaurantWebSite = restaurantWebSite;
    }

    public Float getRestaurantLatitude() {
        return this.restaurantLatitude;
    }

    public Restaurant restaurantLatitude(Float restaurantLatitude) {
        this.setRestaurantLatitude(restaurantLatitude);
        return this;
    }

    public void setRestaurantLatitude(Float restaurantLatitude) {
        this.restaurantLatitude = restaurantLatitude;
    }

    public Float getRestaurantLongitude() {
        return this.restaurantLongitude;
    }

    public Restaurant restaurantLongitude(Float restaurantLongitude) {
        this.setRestaurantLongitude(restaurantLongitude);
        return this;
    }

    public void setRestaurantLongitude(Float restaurantLongitude) {
        this.restaurantLongitude = restaurantLongitude;
    }

    public Boolean getIsActivated() {
        return this.isActivated;
    }

    public Restaurant isActivated(Boolean isActivated) {
        this.setIsActivated(isActivated);
        return this;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Set<ProUser> getProUsers() {
        return this.proUsers;
    }

    public void setProUsers(Set<ProUser> proUsers) {
        if (this.proUsers != null) {
            this.proUsers.forEach(i -> i.setRestaurant(null));
        }
        if (proUsers != null) {
            proUsers.forEach(i -> i.setRestaurant(this));
        }
        this.proUsers = proUsers;
    }

    public Restaurant proUsers(Set<ProUser> proUsers) {
        this.setProUsers(proUsers);
        return this;
    }

    public Restaurant addProUser(ProUser proUser) {
        this.proUsers.add(proUser);
        proUser.setRestaurant(this);
        return this;
    }

    public Restaurant removeProUser(ProUser proUser) {
        this.proUsers.remove(proUser);
        proUser.setRestaurant(null);
        return this;
    }

    public Set<Picture> getPictures() {
        return this.pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        if (this.pictures != null) {
            this.pictures.forEach(i -> i.setRestaurant(null));
        }
        if (pictures != null) {
            pictures.forEach(i -> i.setRestaurant(this));
        }
        this.pictures = pictures;
    }

    public Restaurant pictures(Set<Picture> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public Restaurant addPicture(Picture picture) {
        this.pictures.add(picture);
        picture.setRestaurant(this);
        return this;
    }

    public Restaurant removePicture(Picture picture) {
        this.pictures.remove(picture);
        picture.setRestaurant(null);
        return this;
    }

    public Set<Dish> getDishes() {
        return this.dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        if (this.dishes != null) {
            this.dishes.forEach(i -> i.setRestaurant(null));
        }
        if (dishes != null) {
            dishes.forEach(i -> i.setRestaurant(this));
        }
        this.dishes = dishes;
    }

    public Restaurant dishes(Set<Dish> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public Restaurant addDish(Dish dish) {
        this.dishes.add(dish);
        dish.setRestaurant(this);
        return this;
    }

    public Restaurant removeDish(Dish dish) {
        this.dishes.remove(dish);
        dish.setRestaurant(null);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Restaurant location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Restaurant location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Set<Dish> getDishes() {
        return this.dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        if (this.dishes != null) {
            this.dishes.forEach(i -> i.setRestaurant(null));
        }
        if (dishes != null) {
            dishes.forEach(i -> i.setRestaurant(this));
        }
        this.dishes = dishes;
    }

    public Restaurant dishes(Set<Dish> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public Restaurant addDish(Dish dish) {
        this.dishes.add(dish);
        dish.setRestaurant(this);
        return this;
    }

    public Restaurant removeDish(Dish dish) {
        this.dishes.remove(dish);
        dish.setRestaurant(null);
        return this;
    }

    public Set<Picture> getPictures() {
        return this.pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        if (this.pictures != null) {
            this.pictures.forEach(i -> i.setRestaurant(null));
        }
        if (pictures != null) {
            pictures.forEach(i -> i.setRestaurant(this));
        }
        this.pictures = pictures;
    }

    public Restaurant pictures(Set<Picture> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public Restaurant addPicture(Picture picture) {
        this.pictures.add(picture);
        picture.setRestaurant(this);
        return this;
    }

    public Restaurant removePicture(Picture picture) {
        this.pictures.remove(picture);
        picture.setRestaurant(null);
        return this;
    }

    public Set<ProUser> getProUsers() {
        return this.proUsers;
    }

    public void setProUsers(Set<ProUser> proUsers) {
        if (this.proUsers != null) {
            this.proUsers.forEach(i -> i.setRestaurant(null));
        }
        if (proUsers != null) {
            proUsers.forEach(i -> i.setRestaurant(this));
        }
        this.proUsers = proUsers;
    }

    public Restaurant proUsers(Set<ProUser> proUsers) {
        this.setProUsers(proUsers);
        return this;
    }

    public Restaurant addProUser(ProUser proUser) {
        this.proUsers.add(proUser);
        proUser.setRestaurant(this);
        return this;
    }

    public Restaurant removeProUser(ProUser proUser) {
        this.proUsers.remove(proUser);
        proUser.setRestaurant(null);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeRestaurant(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addRestaurant(this));
        }
        this.clients = clients;
    }

    public Restaurant clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Restaurant addClient(Client client) {
        this.clients.add(client);
        client.getRestaurants().add(this);
        return this;
    }

    public Restaurant removeClient(Client client) {
        this.clients.remove(client);
        client.getRestaurants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", restaurantName='" + getRestaurantName() + "'" +
            ", restaurantAddress='" + getRestaurantAddress() + "'" +
            ", restaurantAddressCmp='" + getRestaurantAddressCmp() + "'" +
            ", restaurantType='" + getRestaurantType() + "'" +
            ", isSub='" + getIsSub() + "'" +
            ", restaurantSubscription='" + getRestaurantSubscription() + "'" +
            ", restaurantSubDate='" + getRestaurantSubDate() + "'" +
            ", restaurantDescription='" + getRestaurantDescription() + "'" +
            ", restaurantPhoneNumber='" + getRestaurantPhoneNumber() + "'" +
            ", restaurantEmail='" + getRestaurantEmail() + "'" +
            ", restaurantWebSite='" + getRestaurantWebSite() + "'" +
            ", restaurantLatitude=" + getRestaurantLatitude() +
            ", restaurantLongitude=" + getRestaurantLongitude() +
            ", isActivated='" + getIsActivated() + "'" +
            "}";
    }
}
