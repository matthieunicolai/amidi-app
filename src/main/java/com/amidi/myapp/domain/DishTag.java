package com.amidi.myapp.domain;

import com.amidi.myapp.domain.enumeration.FoodType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DishTag.
 */
@Entity
@Table(name = "dish_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dishtag")
public class DishTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dish_tag", nullable = false)
    private FoodType dishTag;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dishTags", "restaurant" }, allowSetters = true)
    private Dish dish;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DishTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodType getDishTag() {
        return this.dishTag;
    }

    public DishTag dishTag(FoodType dishTag) {
        this.setDishTag(dishTag);
        return this;
    }

    public void setDishTag(FoodType dishTag) {
        this.dishTag = dishTag;
    }

    public Dish getDish() {
        return this.dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public DishTag dish(Dish dish) {
        this.setDish(dish);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DishTag)) {
            return false;
        }
        return id != null && id.equals(((DishTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DishTag{" +
            "id=" + getId() +
            ", dishTag='" + getDishTag() + "'" +
            "}";
    }
}
