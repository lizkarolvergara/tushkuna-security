package pe.edu.idat.tushkunaapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpedido;

    private Integer mesaNumero;
    private String mozo;
    private String descripcion;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fecha;
}