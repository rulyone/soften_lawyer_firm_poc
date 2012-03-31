/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package controladores;

import entidades.Abono;
import entidades.Demanda;
import entidades.ProgramacionAbonos;
import entidades.Usuario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.jboss.seam.international.status.Messages;
import servicios.BusinessLogicException;
import servicios.Servicios;
import servicios.Util;

/**
 *
 * @author rulyone
 */
@Named
@ViewScoped
public class DemandasMB implements Serializable {

    @Inject Messages msgs;
    
    @EJB private Servicios servicios;
    
    private List<Demanda> demandas;
    
    private Integer vistaTabla;
    
    private String filtro;
    
    private Boolean filtrarPorRut = false;
    
    //INGRESAR DEMANDAS
    private String datosCSV;
    
    //INGRESAR PAGOS WEB
    private String pagosWeb;
    
    //EDITAR DEMANDA
    private Demanda demandaEditable;
    
    //PROGRAMAR ABONOS
    private Demanda demandaProgramacion;
    @Min(message="El número de abonos debe ser al menos 1.", value=1)
    @Max(message="El número de abonos debe ser menor o igual a 36.", value=36)
    private int numeroDeAbonos = 1;
    @NotNull(message="Debes seleccionar si se utilizará capital pagaré o sólo capital.")
    private Boolean utilizarCapitalPagare;
    @NotNull(message="El interés es obligatorio.")
    @Min(message="El interés debe ser mayor o igual a 0.", value=0)
    private BigDecimal interes;
    @NotNull(message="El valor de descuento es obligatorio (poner 0 en caso que no haya descuento).")
    @Min(0)
    @Max(100)
    private BigDecimal descuentoOfrecido = new BigDecimal("0.00");
    
    //REPROGRAMAR ABONOS
    private Demanda demandaReprogramacion;
    @NotNull
    private Boolean recalcularTotalAPagar;
    
    //EDITAR COSTAS
    private Demanda demandaEditableCostas;
    
    //CREAR USUARIO
    @NotNull(message="El rut es obligatorio")
    @Length(min=5, max=20, message="Rut debe contener entre 5 y 20 caracteres")
    private String rut;
    @NotNull(message="La password es obligatoria")
    @Length(min=5, message="La password debe tener al menos 5 caracteres")
    private String password;
    @NotNull(message="El nombre es obligatorio")
    @Length(min=10, max=255, message="El nombre completo debe tener al menos 10 y máximo 255 caracteres")
    private String nombreCompleto;
    @NotNull(message="El rol es obligatorio")
    private String rol;
    
    //PAGAR BONO
    private Demanda demandaPagoAbono;
    @NotNull
    private BigDecimal descuentoAplicado = new BigDecimal("0.00");
    @NotNull
    private BigInteger montoPagado;
    @NotNull
    private BigInteger montoSinDescuento;
    
    //DETALLES ABONOS
    private Demanda demandaDetalles;
    
    //BLOCKEAR/DESBLOCKEAR USUARIOS
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    
    //MOSTRAR ABONOS POR RESPONSABLE
    private List<Abono> abonos;
    private Usuario responsable;
    
    /**
     * Creates a new instance of DemandasMB
     */
    public DemandasMB() {
    }
    
    @PostConstruct
    private void init() {
        this.demandas = servicios.getDemandas();
        this.usuarios = servicios.getUsuarios();
        this.vistaTabla = 1;
    }
    
    public void buscar() {
        try {
            if (filtrarPorRut)
                this.demandas = servicios.getDemandasFiltradasPorRut(filtro);
            else
                this.demandas = servicios.getDemandasFiltradasPorNombre(filtro);
        } catch (BusinessLogicException ex) {            
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public String ingresarPagosWeb() {
        try {
            servicios.procesarAbonosWeb(pagosWeb);
            Util.addInfoMessage("Pagos webs ingresados satisfactoriamente.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
        return null;
    }
    
    public String ingresarDatosCSV() {
        List<Demanda> list;
        try {
            list = Util.parsearDemandasCSV(datosCSV);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
            return null;
        }

        if (list.isEmpty()) {
            Util.addErrorMessage("No se ha ingresado ninguna demanda.", null);
            return null;
        }
        
        try {
            servicios.ingresarDatosAdministrativos(list);
            demandas = servicios.getDemandas();
            Util.addInfoMessage("Demandas ingresadas satisfactoriamente.", null);
            return "/aplicacion.xhtml?faces-redirect=true";
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
            return null;
        }
    }
    
    public void editarCostas() {
        try {
            servicios.editarCostas(demandaEditableCostas.getId(), demandaEditableCostas.getCostas());
            demandas = servicios.getDemandas();
            Util.addInfoMessage("Costas editadas satisfactoriamente.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void editarDemanda() {
        try {
            servicios.modificarDatosJudiciales(demandaEditable.getId(), demandaEditable.getTribunal(), demandaEditable.getRol(), demandaEditable.getFechaIngresoTribunal(), demandaEditable.getFechaResolucion(), demandaEditable.getMandamiento(), demandaEditable.getReceptor(), demandaEditable.getNotificada(), demandaEditable.getObservacionNotificada(), demandaEditable.getEmbargo(), demandaEditable.getObservacionEmbargo());
            demandas = servicios.getDemandas();
            Util.addInfoMessage("Demanda editada.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void editarDatosAdministrativos() {
        try {
            servicios.modificarDatosAdministrativos(demandaEditable.getId(), demandaEditable.getCalleParticular(), demandaEditable.getDepartamentoParticular(), demandaEditable.getNumeroParticular(),demandaEditable.getComunaParticular(), demandaEditable.getCodigoAreaParticular(), demandaEditable.getTelefonoParticular(), demandaEditable.getCalleComercial(), demandaEditable.getDepartamentoComercial(), demandaEditable.getNumeroComercial(), demandaEditable.getComunaComercial(), demandaEditable.getCodigoAreaComercial(), demandaEditable.getTelefonoComercial(), demandaEditable.getCelular(),demandaEditable.getFechaDemandaCorte());
            demandas = servicios.getDemandas();
            Util.addInfoMessage("Demanda editada.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void programarAbonos() {
        try {
            servicios.programarAbonos(demandaProgramacion.getId(), numeroDeAbonos, utilizarCapitalPagare, interes, descuentoOfrecido.divide(new BigDecimal("100.00")));
            demandas = servicios.getDemandas();
            Util.addInfoMessage("Programación de abonos realizada satisfactoriamente.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void reprogramarAbonos() {
        try {
            if (!recalcularTotalAPagar) {
                ProgramacionAbonos ultimaProgra = demandaReprogramacion.getProgramaciones().get(demandaReprogramacion.getProgramaciones().size() - 1);
                System.out.println(ultimaProgra.getNumeroDeAbonos());
                System.out.println(ultimaProgra.getDescuentoOfrecido());
                System.out.println(utilizarCapitalPagare);
                System.out.println(ultimaProgra.getInteres());
                servicios.reprogramarAbonos(demandaReprogramacion.getId(), ultimaProgra.getNumeroDeAbonos(), ultimaProgra.getDescuentoOfrecido(), false, null, ultimaProgra.getInteres());
            }else{
                servicios.reprogramarAbonos(demandaReprogramacion.getId(), numeroDeAbonos, descuentoOfrecido.divide(new BigDecimal("100.00")), recalcularTotalAPagar, utilizarCapitalPagare, interes);
            }
            
            demandas = servicios.getDemandas();
            Util.addInfoMessage("ReProgramación de abonos realizada satisfactoriamente.", null);
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void pagarAbono() {
        //calcular montoPagado segun descuento aplicado.
        this.montoPagado = descuentoAplicado.divide(new BigDecimal("100.00"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(montoSinDescuento)).setScale(0).toBigIntegerExact();
    }
    
    public void calcularTotalAPagar() {
        if (montoSinDescuento != null && descuentoAplicado != null) {
            this.montoPagado = new BigDecimal(montoSinDescuento).subtract(descuentoAplicado.multiply(new BigDecimal(montoSinDescuento)).setScale(0, 2)).toBigIntegerExact();
        }
    }
    
    public void confirmarPagoAbono() {
        try {
            servicios.pagarAbono(demandaPagoAbono.getId(), descuentoAplicado, montoPagado, montoSinDescuento);
            demandas = servicios.getDemandas();         
            Util.addInfoMessage("Pago del abono realizado satisfactoriamente.", null);
            this.anularPago();
        } catch (BusinessLogicException ex) {
            Util.addErrorMessage(ex.getMessage(), null);
        }
    }
    
    public void anularPago() {
        descuentoAplicado = new BigDecimal("0.00");
        montoPagado = null;
        montoSinDescuento = null;
    }
    
    public String crearUsuario() {
        try {
            servicios.crearUsuario(rut, password, nombreCompleto, rol);
            this.usuarios = servicios.getUsuarios();
            msgs.info("Usuario creado satisfactoriamente.");
            //Util.addInfoMessage("Usuario creado satisfactoriamente.", null);
            return "/aplicacion.xhtml?faces-redirect=true";
        } catch (BusinessLogicException ex) {
            msgs.error(ex.getMessage());
            //Util.addErrorMessage(ex.getMessage(), null);
            return null;
        }
    }
    
    public String imprimirComprobante(Abono abono)  {
        Util.imprimirComprobante(abono);
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }
    
    public String bloquearUsuario(String rut) {
            try {
                this.servicios.bloquearUsuario(rut);
                Util.addInfoMessage("Usuario bloqueado exitosamente.", null);
                this.usuarios = servicios.getUsuarios();
            } catch (BusinessLogicException ex) {
                Util.addErrorMessage(ex.getMessage(), null);
            }
        return null;
    }
    
    public String desbloquearUsuario(String rut) {
            try {
                this.servicios.desbloquearUsuario(rut);
                Util.addInfoMessage("Usuario desbloqueado exitosamente.", null);
                this.usuarios = servicios.getUsuarios();
            } catch (BusinessLogicException ex) {
                Util.addErrorMessage(ex.getMessage(), null);
            }
        return null;
    }
    
    public String verHistorialAbonos(Usuario responsable) {
        this.responsable = responsable;
        this.abonos = this.servicios.getAbonosPorResponsable(responsable);
        return null;
    }
    
    public String cerrarHistorialAbonos() {
        this.responsable = null;
        return null;
    }

    public Demanda getDemandaDetalles() {
        return demandaDetalles;
    }

    public void setDemandaDetalles(Demanda demandaDetalles) {
        this.demandaDetalles = demandaDetalles;
    }

    public Demanda getDemandaPagoAbono() {
        return demandaPagoAbono;
    }

    public void setDemandaPagoAbono(Demanda demandaPagoAbono) {
        this.demandaPagoAbono = demandaPagoAbono;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public BigInteger getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigInteger montoPagado) {
        this.montoPagado = montoPagado;
    }

    public BigInteger getMontoSinDescuento() {
        return montoSinDescuento;
    }

    public void setMontoSinDescuento(BigInteger montoSinDescuento) {
        this.montoSinDescuento = montoSinDescuento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Demanda getDemandaProgramacion() {
        return demandaProgramacion;
    }

    public void setDemandaProgramacion(Demanda demandaProgramacion) {
        this.demandaProgramacion = demandaProgramacion;
    }

    public BigDecimal getDescuentoOfrecido() {
        return descuentoOfrecido;
    }

    public void setDescuentoOfrecido(BigDecimal descuentoOfrecido) {
        this.descuentoOfrecido = descuentoOfrecido;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public int getNumeroDeAbonos() {
        return numeroDeAbonos;
    }

    public void setNumeroDeAbonos(int numeroDeAbonos) {
        this.numeroDeAbonos = numeroDeAbonos;
    }

    public Boolean getUtilizarCapitalPagare() {
        return utilizarCapitalPagare;
    }

    public void setUtilizarCapitalPagare(Boolean utilizarCapitalPagare) {
        this.utilizarCapitalPagare = utilizarCapitalPagare;
    }

    public Demanda getDemandaEditable() {
        return demandaEditable;
    }

    public void setDemandaEditable(Demanda demandaEditable) {
        this.demandaEditable = demandaEditable;
    }

    public Integer getVistaTabla() {
        return vistaTabla;
    }

    public void setVistaTabla(Integer vistaTabla) {
        this.vistaTabla = vistaTabla;
    }
    
    public List<Demanda> getDemandas() {
        return demandas;
    }

    public void setDemandas(List<Demanda> demandas) {
        this.demandas = demandas;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Boolean getFiltrarPorRut() {
        return filtrarPorRut;
    }

    public void setFiltrarPorRut(Boolean filtrarPorRut) {
        this.filtrarPorRut = filtrarPorRut;
    }

    public String getDatosCSV() {
        return datosCSV;
    }

    public void setDatosCSV(String datosCSV) {
        this.datosCSV = datosCSV;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Demanda getDemandaReprogramacion() {
        return demandaReprogramacion;
    }

    public void setDemandaReprogramacion(Demanda demandaReprogramacion) {
        this.demandaReprogramacion = demandaReprogramacion;
    }

    public Boolean getRecalcularTotalAPagar() {
        return recalcularTotalAPagar;
    }

    public void setRecalcularTotalAPagar(Boolean recalcularTotalAPagar) {
        this.recalcularTotalAPagar = recalcularTotalAPagar;
    }

    public Demanda getDemandaEditableCostas() {
        return demandaEditableCostas;
    }

    public void setDemandaEditableCostas(Demanda demandaEditableCostas) {
        this.demandaEditableCostas = demandaEditableCostas;
    }

    public String getPagosWeb() {
        return pagosWeb;
    }

    public void setPagosWeb(String pagosWeb) {
        this.pagosWeb = pagosWeb;
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public void setAbonos(List<Abono> abonos) {
        this.abonos = abonos;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }
    
    
}
