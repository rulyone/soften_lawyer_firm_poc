/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rulyone
 */
@Entity
@Table(name="PROGRAMACIONES_ABONOS")
public class ProgramacionAbonos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(36)
    private int numeroDeAbonos;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private BigInteger montoPorAbono;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigInteger montoUltimoAbono;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigInteger totalAPagar;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    @Max(1)
    private BigDecimal descuentoOfrecido;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date fecha;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal interes;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Demanda demanda;
    
    @OneToMany(mappedBy = "programacionAbonos")
    private List<Abono> abonos;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Usuario responsable;
    
    @NotNull
    private Boolean utilizarCapitalPagare;
    
    @NotNull
    @Min(1)
    private Integer mesesMoroso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMesesMoroso() {
        return mesesMoroso;
    }

    public void setMesesMoroso(Integer mesesMoroso) {
        this.mesesMoroso = mesesMoroso;
    }

    public Boolean getUtilizarCapitalPagare() {
        return utilizarCapitalPagare;
    }

    public void setUtilizarCapitalPagare(Boolean utilizarCapitalPagare) {
        this.utilizarCapitalPagare = utilizarCapitalPagare;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigInteger getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigInteger totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public BigInteger getMontoUltimoAbono() {
        return montoUltimoAbono;
    }

    public void setMontoUltimoAbono(BigInteger montoUltimoAbono) {
        this.montoUltimoAbono = montoUltimoAbono;
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public void setAbonos(List<Abono> abonos) {
        this.abonos = abonos;
    }

    public Demanda getDemanda() {
        return demanda;
    }

    public void setDemanda(Demanda demanda) {
        this.demanda = demanda;
    }

    public BigDecimal getDescuentoOfrecido() {
        return descuentoOfrecido;
    }

    public void setDescuentoOfrecido(BigDecimal descuentoOfrecido) {
        this.descuentoOfrecido = descuentoOfrecido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getMontoPorAbono() {
        return montoPorAbono;
    }

    public void setMontoPorAbono(BigInteger montoPorAbono) {
        this.montoPorAbono = montoPorAbono;
    }

    public int getNumeroDeAbonos() {
        return numeroDeAbonos;
    }

    public void setNumeroDeAbonos(int numeroDeAbonos) {
        this.numeroDeAbonos = numeroDeAbonos;
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
        if (!(object instanceof ProgramacionAbonos)) {
            return false;
        }
        ProgramacionAbonos other = (ProgramacionAbonos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ProgramacionAbonos[ id=" + id + " ]";
    }
    
}
