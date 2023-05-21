package com.dmitriykravchuk.project.yourcomforent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "housing")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Housing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToOne(mappedBy = "housing", cascade = CascadeType.ALL)
    private Location location;

    @Column(name = "title")
    private String title;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;

    @Column(name = "max_amount_people")
    private Integer maxAmountPeople;

    @JsonIgnore
    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
