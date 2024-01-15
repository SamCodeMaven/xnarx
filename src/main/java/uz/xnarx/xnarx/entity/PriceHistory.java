package uz.xnarx.xnarx.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Product product;

    private String store_name;

    private Integer price;

    private String product_name;

    private String product_link;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;
}
