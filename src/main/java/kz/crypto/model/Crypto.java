package kz.crypto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "crypto")
public class Crypto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long date;
    private String symbol;
    private Double price;
}
