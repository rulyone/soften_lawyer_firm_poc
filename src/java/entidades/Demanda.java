/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author rulyone
 */
@Entity
@Table(name="DEMANDAS")
@NamedQueries({
    @NamedQuery(name="Demanda.filtrarPorRut", query="SELECT d FROM Demanda d WHERE d.rut LIKE :rut"),
    @NamedQuery(name="Demanda.filtrarPorNombre", query="SELECT d FROM Demanda d WHERE d.nombreCompleto LIKE :nombre"),
    @NamedQuery(name="Demanda.filtrarPorRutExacto", query="SELECT d FROM Demanda d WHERE d.rut = :rut")
})
public class Demanda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    //Datos administrativos falabella
    private String numeroTarjeta;
    private String rut;
    private String nombreCompleto;
    private String sexo;
    
    private String calleParticular;
    private String departamentoParticular;
    private String numeroParticular;
    private String comunaParticular;
    private String codigoAreaParticular;
    private String telefonoParticular;
    
    private String calleComercial;
    private String departamentoComercial;
    private String numeroComercial;
    private String comunaComercial;
    private String codigoAreaComercial;
    private String telefonoComercial;
    
    private String celular;
    
    private BigInteger capital;
    private BigInteger capitalPagare;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimientoDeuda;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimientoPagare;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAsignacion;
    
    
    private String codigoAbogado = "abo00056";
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDemandaCorte;
    
    //Datos modificados por AMB abogados
    
    private String tribunal;
    private String rol;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngresoTribunal;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaResolucion;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date mandamiento;
    
    private String receptor;
    
    private String notificada;
        
    private String observacionNotificada;
    
    private String embargo;
    
    private String observacionEmbargo;
    
    private BigInteger costas = BigInteger.ZERO;
    
    private Boolean pagadoTotalmente = false;
    
    ////////////////////
    @OneToMany(mappedBy = "demanda")
    private List<ProgramacionAbonos> programaciones;
    
    //TRANSIENT
    private transient @Transient BigInteger totalPagado;
    
    public boolean getDeudaCancelada() {
        if (programaciones.isEmpty()) {
            return false;
        }
        totalPagado = this.getTotalPagado();
        ProgramacionAbonos prog = programaciones.get(programaciones.size() - 1);
        BigInteger totalAPagar = prog.getTotalAPagar(); //al agregar costas no sirve...
        if (totalPagado.compareTo(totalAPagar) >= 0) {
            return true;
        }
        return false;
        
    }
    
    public BigInteger getTotalPagado() {
        
        totalPagado = BigInteger.ZERO;
        if (programaciones.isEmpty()) {
            return totalPagado;
        }
        for (int i = 0; i < programaciones.size(); i++) {
            ProgramacionAbonos programacion = programaciones.get(i);
            List<Abono> abonos = programacion.getAbonos();
            for (int j = 0; j < abonos.size(); j++) {
                Abono abono = abonos.get(j);
                totalPagado = totalPagado.add(abono.getMontoSinDescuento());
            }
        }
        return totalPagado;
    }
    
    public ProgramacionAbonos getUltimaProgramacion() {
        if (programaciones.isEmpty()) {
            return null;
        }
        return programaciones.get(programaciones.size() - 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public Boolean getPagadoTotalmente() {
        return pagadoTotalmente;
    }

    public void setPagadoTotalmente(Boolean pagadoTotalmente) {
        this.pagadoTotalmente = pagadoTotalmente;
    }

    public String getCalleComercial() {
        return calleComercial;
    }

    public void setCalleComercial(String calleComercial) {
        this.calleComercial = calleComercial;
    }

    public String getCalleParticular() {
        return calleParticular;
    }

    public void setCalleParticular(String calleParticular) {
        this.calleParticular = calleParticular;
    }

    public BigInteger getCapital() {
        return capital;
    }

    public void setCapital(BigInteger capital) {
        this.capital = capital;
    }

    public BigInteger getCapitalPagare() {
        return capitalPagare;
    }

    public void setCapitalPagare(BigInteger capitalPagare) {
        this.capitalPagare = capitalPagare;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCodigoAbogado() {
        return codigoAbogado;
    }

    public void setCodigoAbogado(String codigoAbogado) {
        this.codigoAbogado = codigoAbogado;
    }

    public String getCodigoAreaComercial() {
        return codigoAreaComercial;
    }

    public void setCodigoAreaComercial(String codigoAreaComercial) {
        this.codigoAreaComercial = codigoAreaComercial;
    }

    public String getCodigoAreaParticular() {
        return codigoAreaParticular;
    }

    public void setCodigoAreaParticular(String codigoAreaParticular) {
        this.codigoAreaParticular = codigoAreaParticular;
    }

    public String getComunaComercial() {
        return comunaComercial;
    }

    public void setComunaComercial(String comunaComercial) {
        this.comunaComercial = comunaComercial;
    }

    public String getComunaParticular() {
        return comunaParticular;
    }

    public void setComunaParticular(String comunaParticular) {
        this.comunaParticular = comunaParticular;
    }

    public BigInteger getCostas() {
        return costas;
    }

    public void setCostas(BigInteger costas) {
        this.costas = costas;
    }

    public String getDepartamentoComercial() {
        return departamentoComercial;
    }

    public void setDepartamentoComercial(String departamentoComercial) {
        this.departamentoComercial = departamentoComercial;
    }

    public String getDepartamentoParticular() {
        return departamentoParticular;
    }

    public void setDepartamentoParticular(String departamentoParticular) {
        this.departamentoParticular = departamentoParticular;
    }

    public String getEmbargo() {
        return embargo;
    }

    public void setEmbargo(String embargo) {
        this.embargo = embargo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaDemandaCorte() {
        return fechaDemandaCorte;
    }

    public void setFechaDemandaCorte(Date fechaDemandaCorte) {
        this.fechaDemandaCorte = fechaDemandaCorte;
    }

    public Date getFechaIngresoTribunal() {
        return fechaIngresoTribunal;
    }

    public void setFechaIngresoTribunal(Date fechaIngresoTribunal) {
        this.fechaIngresoTribunal = fechaIngresoTribunal;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Date getMandamiento() {
        return mandamiento;
    }

    public void setMandamiento(Date mandamiento) {
        this.mandamiento = mandamiento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNotificada() {
        return notificada;
    }

    public void setNotificada(String notificada) {
        this.notificada = notificada;
    }

    public String getNumeroComercial() {
        return numeroComercial;
    }

    public void setNumeroComercial(String numeroComercial) {
        this.numeroComercial = numeroComercial;
    }

    public String getNumeroParticular() {
        return numeroParticular;
    }

    public void setNumeroParticular(String numeroParticular) {
        this.numeroParticular = numeroParticular;
    }

    public String getObservacionEmbargo() {
        return observacionEmbargo;
    }

    public void setObservacionEmbargo(String observacionEmbargo) {
        this.observacionEmbargo = observacionEmbargo;
    }

    public String getObservacionNotificada() {
        return observacionNotificada;
    }

    public void setObservacionNotificada(String observacionNotificada) {
        this.observacionNotificada = observacionNotificada;
    }

    public List<ProgramacionAbonos> getProgramaciones() {
        return programaciones;
    }

    public void setProgramaciones(List<ProgramacionAbonos> programaciones) {
        this.programaciones = programaciones;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefonoComercial() {
        return telefonoComercial;
    }

    public void setTelefonoComercial(String telefonoComercial) {
        this.telefonoComercial = telefonoComercial;
    }

    public String getTelefonoParticular() {
        return telefonoParticular;
    }

    public void setTelefonoParticular(String telefonoParticular) {
        this.telefonoParticular = telefonoParticular;
    }

    public String getTribunal() {
        return tribunal;
    }

    public void setTribunal(String tribunal) {
        this.tribunal = tribunal;
    }

    public Date getVencimientoDeuda() {
        return vencimientoDeuda;
    }

    public void setVencimientoDeuda(Date vencimientoDeuda) {
        this.vencimientoDeuda = vencimientoDeuda;
    }

    public Date getVencimientoPagare() {
        return vencimientoPagare;
    }

    public void setVencimientoPagare(Date vencimientoPagare) {
        this.vencimientoPagare = vencimientoPagare;
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
        if (!(object instanceof Demanda)) {
            return false;
        }
        Demanda other = (Demanda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Demanda[ id=" + id + " ]";
    }
    
}
