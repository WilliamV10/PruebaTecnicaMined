package com.tramites.tramites.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa a un ciudadano registrado en el sistema gubernamental.
 * Contiene información personal y de identificación del ciudadano.
 */
@Entity
@Table(name = "ciudadano", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ciudadano_documento", 
                         columnNames = {"tipo_documento_id", "numero_documento"}),
        @UniqueConstraint(name = "uk_ciudadano_correo", 
                         columnNames = {"correo"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_active = true")
public class Ciudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_ciudadano_tipo_documento"))
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 30)
    private String numeroDocumento;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
