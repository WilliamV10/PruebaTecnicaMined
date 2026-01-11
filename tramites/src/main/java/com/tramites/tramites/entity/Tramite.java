package com.tramites.tramites.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un trámite realizado por un ciudadano
 * ante la institución gubernamental.
 */
@Entity
@Table(name = "tramite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_active = true")
public class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudadano_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_tramite_ciudadano"))
    private Ciudadano ciudadano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_tramite_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_tramite_tipo_tramite"))
    private TipoTramite tipoTramite;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.fechaSolicitud = LocalDateTime.now();
        this.isActive = true;
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
