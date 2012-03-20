/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rulyone
 */
@Entity
@Table(name="ABONOS")
public class Abono implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigInteger montoSinDescuento;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigInteger montoPagado;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    @Max(1)
    private BigDecimal descuentoAplicado;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date fecha;
    
    @ManyToOne
    @NotNull
    private ProgramacionAbonos programacionAbonos;
    
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Usuario responsable;
    
    @NotNull
    private Boolean pagoWeb;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPagoWeb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPagoWeb() {
        return fechaPagoWeb;
    }

    public void setFechaPagoWeb(Date fechaPagoWeb) {
        this.fechaPagoWeb = fechaPagoWeb;
    }

    public Boolean getPagoWeb() {
        return pagoWeb;
    }

    public void setPagoWeb(Boolean pagoWeb) {
        this.pagoWeb = pagoWeb;
    }

    public BigInteger getMontoSinDescuento() {
        return montoSinDescuento;
    }

    public void setMontoSinDescuento(BigInteger montoSinDescuento) {
        this.montoSinDescuento = montoSinDescuento;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigInteger montoPagado) {
        this.montoPagado = montoPagado;
    }

    public ProgramacionAbonos getProgramacionAbonos() {
        return programacionAbonos;
    }

    public void setProgramacionAbonos(ProgramacionAbonos programacionAbonos) {
        this.programacionAbonos = programacionAbonos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Abono)) {
            return false;
        }
        Abono other = (Abono) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Abono[ id=" + id + " ]";
    }
    
}
