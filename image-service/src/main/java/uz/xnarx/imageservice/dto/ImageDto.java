package uz.xnarx.imageservice.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private Long id;

    private String name;

    private byte[] data;

}
