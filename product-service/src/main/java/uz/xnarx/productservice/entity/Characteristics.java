package uz.xnarx.productservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "gsmarena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Characteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search", columnDefinition = "text")
    private String search;

    private String name;

    @Column(name = "details", columnDefinition = "text")
    private String details;

    @OneToMany(mappedBy = "gsmarena", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;
}
