package kz.crypto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "crypto_config")
public class CryptoConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "crypto_name")
    private String symbol;
}
