package kz.crypto.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "crypto_type")
public class CryptoType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String filePath;
    private String type;
    private Boolean active;
    private Long lastUpdated;

}
