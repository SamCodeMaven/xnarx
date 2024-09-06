package uz.xnarx.productservice.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicsDto {

    private Long id;

    private String name;

    private String details;
}
