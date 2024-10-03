package uz.xnarx.productservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false,name = "product_name")
    private String productName;


    @Column(name = "product_image")
    private String productImage;

    @Column(name = "category_name")
    private String categoryName;

    private Boolean isActive;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PriceHistory> priceHistory;

    @ManyToOne
    @JoinColumn(name = "gsmarena_id")
    @JsonBackReference
    private Characteristics gsmarena;
}
