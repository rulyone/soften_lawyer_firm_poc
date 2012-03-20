/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import entidades.Abono;
import entidades.Demanda;
import entidades.ProgramacionAbonos;
import entidades.Usuario;
import entidades.fachadas.AbonoFacade;
import entidades.fachadas.DemandaFacade;
import entidades.fachadas.ProgramacionAbonosFacade;
import entidades.fachadas.UsuarioFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Principal;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 *
 * @author rulyone
 */
@Stateless
@LocalBean
@DeclareRoles({"ADMIN","NORMAL"})
public class Servicios  {
    
    @Resource
    SessionContext ctx;

    @EJB private UsuarioFacade userFac;
    @EJB private DemandaFacade demFac;
    @EJB private ProgramacionAbonosFacade prograFac;
    @EJB private AbonoFacade aboFac;
    
    @PermitAll
    public void actualizarUltimoAcceso(String rut) {
        Usuario usuario = userFac.getUsuarioPorRut(rut);
        usuario.setFechaUltimoAcceso(new Date());
        userFac.edit(usuario);
    }
    
    @RolesAllowed("ADMIN")    
    public void crearUsuario(String rut, String password, String nombreCompleto, String rol) throws BusinessLogicException {
        
        if (userFac.getUsuarioPorRut(rut) != null) {
            throw new BusinessLogicException("RUT ya está en la base de datos.");
        }
        
        Usuario usuario = new Usuario();
        usuario.setFechaCreacion(Calendar.getInstance().getTime());
        usuario.setFechaUltimoAcceso(null);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setPassword(servicios.Util.hashPassword(password));
        usuario.setRut(rut);
        usuario.setBloqueado(false);
        usuario.setRol(rol);
        
        userFac.create(usuario);
        
    }

    @RolesAllowed("ADMIN")
    public void bloquearUsuario(String rut) throws BusinessLogicException {
        Usuario u = userFac.getUsuarioPorRut(rut);
        if (u == null) {
            throw new BusinessLogicException("RUT no encontrado.");
        }
        if (u.getBloqueado()) {
            throw new BusinessLogicException("El usuario (rut: " + u.getRut() + ") ya estaba bloqueado.");
        }
        if (u.getRol().equals("ADMIN")) {
            throw new BusinessLogicException("No puedes bloquear a un ADMIN.");
        }
        u.setBloqueado(true);
        userFac.edit(u);
    }
    
    @RolesAllowed("ADMIN")
    public void desbloquearUsuario(String rut) throws BusinessLogicException {
        Usuario u = userFac.getUsuarioPorRut(rut);
        if (u == null) {
            throw new BusinessLogicException("RUT no encontrado.");
        }
        if (!u.getBloqueado()) {
            throw new BusinessLogicException("El usuario (rut: " + u.getRut() + ") no estaba bloqueado.");
        }        
        u.setBloqueado(false);
        userFac.edit(u);
    }
    
    @RolesAllowed("ADMIN")
    public void ingresarDatosAdministrativos(List<Demanda> demandas) throws BusinessLogicException {
        for (int i = 0; i < demandas.size(); i++) {
            Demanda demanda = demandas.get(i);
            demFac.create(demanda);
        }
    }

    @RolesAllowed("ADMIN")
    public void ingresarDatosAdministrativos(
            String numeroTarjeta,
            String rut, 
            String nombreCompleto, 
            String sexo, 
            String calleParticular, 
            String departamentoParticular, 
            String numeroParticular, 
            String comunaParticular, 
            String codigoAreaParticular, 
            String telefonoParticular, 
            String calleComercial, 
            String departamentoComercial, 
            String numeroComercial, 
            String comunaComercial, 
            String codigoAreaComercial, 
            String telefonoComercial, 
            String celular, 
            BigInteger capital, 
            BigInteger capitalPagare, 
            Date vencimientoDeuda, 
            Date vencimientoPagare, 
            Date fechaAsignacion,
            Date fechaDemandaCorte) throws BusinessLogicException {
        
        Demanda d = new Demanda();
        d.setNumeroTarjeta(numeroTarjeta);
        d.setRut(rut);
        d.setNombreCompleto(nombreCompleto);
        d.setSexo(sexo);
        d.setCalleParticular(calleParticular);
        d.setDepartamentoParticular(departamentoParticular);
        d.setNumeroParticular(numeroParticular);
        d.setComunaParticular(comunaParticular);
        d.setCodigoAreaParticular(codigoAreaParticular);
        d.setTelefonoParticular(telefonoParticular);
        d.setCalleComercial(calleComercial);
        d.setDepartamentoComercial(departamentoComercial);
        d.setNumeroComercial(numeroComercial);
        d.setComunaComercial(comunaComercial);
        d.setCodigoAreaComercial(codigoAreaComercial);
        d.setTelefonoComercial(telefonoComercial);
        d.setCelular(celular);
        d.setCapital(capital);
        d.setCapitalPagare(capitalPagare);
        d.setVencimientoDeuda(vencimientoDeuda);
        d.setVencimientoPagare(vencimientoPagare);
        d.setFechaAsignacion(fechaAsignacion);
        d.setFechaDemandaCorte(fechaDemandaCorte);
        
        demFac.create(d);
        
    }

    @RolesAllowed({"ADMIN","NORMAL"})
    public void modificarDatosJudiciales(
            Long idDemanda,
            String tribunal,
            String rol, 
            Date fechaIngresoTribunal, 
            Date fechaResolucion, 
            Date mandamiento, 
            String receptor, 
            String notificada, 
            String observacionNotificada, 
            String embargo, 
            String observacionEmbargo) throws BusinessLogicException {
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
        d.setTribunal(tribunal);
        d.setRol(rol);
        d.setFechaIngresoTribunal(fechaIngresoTribunal);
        d.setFechaResolucion(fechaResolucion);
        d.setMandamiento(mandamiento);
        d.setReceptor(receptor);
        d.setNotificada(notificada);
        d.setObservacionNotificada(observacionNotificada);
        d.setEmbargo(embargo);
        d.setObservacionEmbargo(observacionEmbargo);
        
        demFac.edit(d);
        
    }

    @RolesAllowed({"ADMIN","NORMAL"})
    public void modificarDatosAdministrativos(
            Long idDemanda,
            String calleParticular,
            String departamentoParticular,
            String numeroParticular,
            String comunaParticular,
            String codigoAreaParticular,
            String telefonoParticular,
    
            String calleComercial,
            String departamentoComercial,
            String numeroComercial,
            String comunaComercial,
            String codigoAreaComercial,
            String telefonoComercial,
    
            String celular,
            
            Date fechaDemandaCorte) throws BusinessLogicException {
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        d.setCalleParticular(calleParticular);    
        d.setDepartamentoParticular(departamentoParticular);
        d.setNumeroParticular(numeroParticular);
        d.setComunaParticular(comunaParticular);
        d.setCodigoAreaParticular(codigoAreaParticular);
        d.setTelefonoParticular(telefonoParticular);
        
        d.setCalleComercial(calleComercial);
        d.setDepartamentoComercial(departamentoComercial);
        d.setNumeroComercial(numeroComercial);
        d.setComunaComercial(comunaComercial);
        d.setCodigoAreaComercial(codigoAreaComercial);
        d.setTelefonoComercial(telefonoComercial);
        
        d.setCelular(celular);
        
        d.setFechaDemandaCorte(fechaDemandaCorte);
        
        demFac.edit(d);
        
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    public void editarCostas(Long idDemanda, BigInteger costas) throws BusinessLogicException {
        //si la demanda ya tenía programación, se le agregan las costas al último abono, y al totalAPagar
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
        if (costas.compareTo(BigInteger.ZERO) < 0) {
            throw new BusinessLogicException("Las costas no pueden ser negativas.");
        }
                
        if (!d.getProgramaciones().isEmpty()) {
            if (costas.compareTo(d.getCostas()) < 0) {
                throw new BusinessLogicException("Imposible poner costas menores a las ya ingresadas cuando ya existe una programación.");
            }
            //TIENE PROGRAMACIONES, por ende modificar totalAPagar y último abono
            //Al total a pagar le restamos las costas antiguas y al ultimoabono tambien,
            //y luego agregamos las nuevas costas al totalAPagar y al ultimo abono.
            ProgramacionAbonos ultimaProgramacion = d.getProgramaciones().get(d.getProgramaciones().size() - 1);            
            ultimaProgramacion.setTotalAPagar(ultimaProgramacion.getTotalAPagar().subtract(d.getCostas()).add(costas));
            ultimaProgramacion.setMontoUltimoAbono(ultimaProgramacion.getMontoUltimoAbono().subtract(d.getCostas()).add(costas));
            
            prograFac.edit(ultimaProgramacion);
        }
        
        
        if (d.getPagadoTotalmente()) {
            if (costas.compareTo(d.getCostas()) > 0) {
                d.setPagadoTotalmente(false);
            }
        }
        d.setCostas(costas);
        demFac.edit(d);        
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    public void programarAbonos(
            Long idDemanda,
            int numeroDeAbonos,
            boolean utilizarCapitalPagare,
            BigDecimal interes,
            BigDecimal descuentoOfrecido) throws BusinessLogicException {
        
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
        if (!d.getProgramaciones().isEmpty()) {
            throw new BusinessLogicException("Demanda ya tenía programaciones anteriormente, utilizar la función REPROGRAMAR ABONOS.");
        }
        
        if (numeroDeAbonos < 1 || numeroDeAbonos > 36) {
            throw new BusinessLogicException("El número de abonos debe ser entre 1 y 36.");
        }
        
        if (!interes.equals(new BigDecimal("0.020")) && !interes.equals(new BigDecimal("0.025")) && !interes.equals(new BigDecimal("0.030"))) {
            throw new BusinessLogicException("El interés debe ser de un 2%, 2.5% o 3%.");
        }
        
        if (descuentoOfrecido != null && descuentoOfrecido.compareTo(new BigDecimal("0.00")) < 0) {
            throw new BusinessLogicException("El descuento ofrecido debe ser mayor o igual a cero (0).");
        }
        
        BigInteger capital;
        
        if (utilizarCapitalPagare) {
            capital = d.getCapitalPagare();
        }else{
            capital = d.getCapital();
        }
        
        int mesesMoroso = this.calcularMesesMorosos(d.getVencimientoPagare()); 
        
        BigInteger[] montos = this.calcularMontos(capital, interes, mesesMoroso, numeroDeAbonos, d.getCostas(), false, null);
        
        BigInteger montoPorAbono = montos[0];
        BigInteger montoUltimoAbono = montos[1];
        BigInteger totalAPagar = montos[2];
        
        System.out.println("meses moroso: " + mesesMoroso);
        
        ProgramacionAbonos pa = new ProgramacionAbonos();
        pa.setDemanda(d);
        pa.setDescuentoOfrecido(descuentoOfrecido);
        pa.setFecha(Calendar.getInstance().getTime());
        pa.setMontoPorAbono(montoPorAbono);
        pa.setMontoUltimoAbono(montoUltimoAbono);
        pa.setNumeroDeAbonos(numeroDeAbonos);
        pa.setTotalAPagar(totalAPagar);
        pa.setResponsable(responsable);
        pa.setInteres(interes);
        pa.setUtilizarCapitalPagare(utilizarCapitalPagare);
        pa.setMesesMoroso(mesesMoroso);
        
        prograFac.create(pa);
        
        d.getProgramaciones().add(pa);
        //d.setCostas(costas);
        
        demFac.edit(d);
        
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    private BigInteger[] calcularMontos(BigInteger capital, BigDecimal interes, int mesesMoroso ,int numeroDeAbonos, BigInteger costas, boolean restarLoPagado, BigInteger totalPagado) {
        BigDecimal _capital = new BigDecimal(capital);
        BigDecimal _interes = interes;
        BigDecimal _mesesMoroso = new BigDecimal(mesesMoroso);
        BigDecimal _numeroDeAbonos = new BigDecimal(numeroDeAbonos);  
        
        BigInteger[] montos = new BigInteger[3];
        
        BigDecimal totalAPagar = _capital.multiply(_interes).multiply(_mesesMoroso).add(_capital).add(new BigDecimal(costas));
        BigDecimal totalAPagarRedondeado = totalAPagar.setScale(0, RoundingMode.HALF_UP);
        
        if (restarLoPagado) {
            totalAPagarRedondeado.subtract(new BigDecimal(totalPagado));
            totalAPagarRedondeado = totalAPagar.setScale(0, RoundingMode.HALF_UP);
        }
        
        BigDecimal[] totalPorMes = totalAPagarRedondeado.divideAndRemainder(_numeroDeAbonos);
        //el totalPorMes[0] tiene el valor de los primeros abonos, y el último tiene lo q sobra, por ende 
        //sumamos un totalPorMes[0] + totalPorMes[1] para el montoUltimoAbono.
        montos[0] = totalPorMes[0].toBigIntegerExact();
        totalPorMes[1] = totalPorMes[1].setScale(0, RoundingMode.HALF_UP);
        montos[1] = totalPorMes[0].toBigIntegerExact().add(totalPorMes[1].toBigIntegerExact());
        montos[2] = totalAPagarRedondeado.toBigIntegerExact();
                
        return montos;
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    private int calcularMesesMorosos(Date vencimientoPagareDate) throws BusinessLogicException {
        Calendar now = Calendar.getInstance();
        Calendar vencimientoPagare = Calendar.getInstance();
        vencimientoPagare.setTime(vencimientoPagareDate);
        if (vencimientoPagare.after(now)) {
            throw new BusinessLogicException("ERROR, La fecha de vencimiento pagaré es posterior a la fecha actual.");
        }
        
        Calendar v = Calendar.getInstance();
        v.setTime(vencimientoPagare.getTime());
        
        int dif = 0;
        while (v.before(now)) {
            v.add(Calendar.MONTH, 1);
            dif++;
        }
        
        return dif;
    }

    @RolesAllowed({"ADMIN","NORMAL"})
    public void reprogramarAbonos(
            Long idDemanda,
            int numeroDeAbonos,
            BigDecimal descuentoOfrecido,
            Boolean recalcularTotalAPagar,
            Boolean utilizarCapitalPagare,
            BigDecimal interes) throws BusinessLogicException {
        
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
        if (d.getDeudaCancelada()) {
            throw new BusinessLogicException("La deuda ya fue cancelada.");
        }
        
        if (numeroDeAbonos < 1 || numeroDeAbonos > 36) {
            throw new BusinessLogicException("El número de abonos debe ser entre 1 y 36.");
        }
        
        if (d.getProgramaciones().isEmpty()) {
            throw new BusinessLogicException("Demanda no tenía programaciones anteriormente, utilizar la función PROGRAMAR ABONOS.");
        }
        List<ProgramacionAbonos> programaciones = d.getProgramaciones();
        
        int sumaNumeroDeAbonos = numeroDeAbonos;
        BigInteger totalPagado = BigInteger.ZERO;
        for (int i = 0; i < programaciones.size(); i++) {
            ProgramacionAbonos p = programaciones.get(i);
            List<Abono> abonos = p.getAbonos();
            int size = abonos.size();
            sumaNumeroDeAbonos += size;
            for (int j = 0; j < size; j++) {
                Abono abono = abonos.get(j);
                totalPagado = totalPagado.add(abono.getMontoSinDescuento());
            }
        }
        
        if (sumaNumeroDeAbonos < 1 || sumaNumeroDeAbonos > 36) {
            throw new BusinessLogicException("El número de abonos debe ser entre 1 y 36, incluyendo los abonos ya hechos.");
        }
        
        if (descuentoOfrecido.compareTo(new BigDecimal("0.00")) < 0) {
            throw new BusinessLogicException("El descuento ofrecido debe ser mayor o igual a cero (0).");
        }
        
        BigInteger montoPorAbono = null;
        BigInteger montoUltimoAbono = null;
        BigInteger totalAPagar = null;
        int mesesMoroso = 0;
        
        if (recalcularTotalAPagar) {
            BigInteger capital;
            if (utilizarCapitalPagare) {
                capital = d.getCapitalPagare();
            }else{
                capital = d.getCapital();
            }

             mesesMoroso = this.calcularMesesMorosos(d.getVencimientoPagare());

            BigInteger[] montos = this.calcularMontos(capital, interes, mesesMoroso, numeroDeAbonos, d.getCostas(), true, totalPagado);

            montoPorAbono = montos[0];
            montoUltimoAbono = montos[1];
            totalAPagar = montos[2];

        }else{
            //utilizarCapitalPagare puede sr null.
            ProgramacionAbonos ultima = programaciones.get(programaciones.size() - 1);
            BigInteger capital;
            utilizarCapitalPagare = ultima.getUtilizarCapitalPagare();
            if (ultima.getUtilizarCapitalPagare()) {
                capital = d.getCapitalPagare();
            }else{
                capital = d.getCapital();
            }
            
            mesesMoroso = ultima.getMesesMoroso();
            
            BigInteger[] montos = this.calcularMontos(capital, ultima.getInteres(), mesesMoroso, numeroDeAbonos, d.getCostas(), true, totalPagado);
            
            //totalAPagar = programaciones.get(0).getTotalAPagar().subtract(totalPagado);
        
            //montos = totalAPagar.divideAndRemainder(new BigInteger(String.valueOf(numeroDeAbonos)));
            montoPorAbono = montos[0];
            montoUltimoAbono = montos[1];
            totalAPagar = montos[2];
        
        }
        
//        if (montoPorAbono.compareTo(BigInteger.ZERO) <= 0) {
//            throw new BusinessLogicException("La deuda total ya fué cancelada.");
//        }
        
        
        ProgramacionAbonos pa = new ProgramacionAbonos();
        
        pa.setDemanda(d);
        System.out.println("dsctoofrecido: " + descuentoOfrecido);
        pa.setDescuentoOfrecido(descuentoOfrecido);
        pa.setFecha(Calendar.getInstance().getTime());
        System.out.println("montoporabono: " + montoPorAbono);
        pa.setMontoPorAbono(montoPorAbono);    
        System.out.println("montoultimoabono: "+ montoUltimoAbono);
        pa.setMontoUltimoAbono(montoUltimoAbono);
        System.out.println("numeroabonos: " + numeroDeAbonos);
        pa.setNumeroDeAbonos(numeroDeAbonos); 
        System.out.println("totalapagar: " + totalAPagar);
        pa.setTotalAPagar(totalAPagar);
        pa.setResponsable(responsable);
        System.out.println("interes: " + interes);
        pa.setInteres(interes);
        pa.setUtilizarCapitalPagare(utilizarCapitalPagare);
        pa.setMesesMoroso(mesesMoroso);        
        
        prograFac.create(pa);
        
        
        d.getProgramaciones().add(pa);
        
        demFac.edit(d);
        
    }

    @RolesAllowed({"ADMIN","NORMAL"})
    public void pagarAbono(Long idDemanda, BigDecimal descuentoAplicado, BigInteger montoPagado, BigInteger montoSinDescuento) throws BusinessLogicException {
        
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
        if (d.getPagadoTotalmente()) {
            throw new BusinessLogicException("La deuda ya fue cancelada totalmente. Imposible realizar otro abono.");
        }
        
        List<ProgramacionAbonos> programaciones = d.getProgramaciones();
        if (programaciones.isEmpty()) {
            throw new BusinessLogicException("No hay programaciones pactadas para realizar el pago del abono.");
        }
        
        BigInteger totalPagado = BigInteger.ZERO;
        //la ULTIMA programacion tiene el totalAPagar calculado.
        BigInteger totalAPagar = programaciones.get(programaciones.size() - 1).getTotalAPagar(); 
        for (int i = 0; i < programaciones.size(); i++) {
            ProgramacionAbonos p = programaciones.get(i);
            List<Abono> abonos = p.getAbonos();
            for (int j = 0; j < abonos.size(); j++) {
                Abono abono = abonos.get(j);
                totalPagado = totalPagado.add(abono.getMontoSinDescuento());
            }
        }
        BigInteger restante = totalAPagar.subtract(totalPagado);
        boolean pagadoTotalmente = false;
        if (restante.subtract(montoSinDescuento).compareTo(BigInteger.ZERO) < 0) {
            throw new BusinessLogicException("El pago excede la deuda total, debe ser menor.");
        }else if(restante.subtract(montoSinDescuento).compareTo(BigInteger.ZERO) == 0) {
            pagadoTotalmente = true;
        }
        
        ProgramacionAbonos ultimaProgramacion = programaciones.get(programaciones.size() - 1);
        
        Abono abono = new Abono();
        abono.setFecha(Calendar.getInstance().getTime());
        abono.setDescuentoAplicado(descuentoAplicado);
        abono.setMontoPagado(montoPagado);
        abono.setMontoSinDescuento(montoSinDescuento);
        abono.setResponsable(responsable); 
        abono.setProgramacionAbonos(ultimaProgramacion);
        abono.setPagoWeb(false);
        
        aboFac.create(abono);
        
        ultimaProgramacion.getAbonos().add(abono);
        
        prograFac.edit(ultimaProgramacion);

        if (pagadoTotalmente) {
            d.setPagadoTotalmente(true);
            demFac.edit(d);
        }
        
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    public void procesarAbonosWeb(String pagosWeb) throws BusinessLogicException {        
        
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        List<Object[]> abonosWeb = Util.parsearPagosWeb(pagosWeb);
        
        for (int i = 0; i < abonosWeb.size(); i++) {
            Object[] abonoBruto = abonosWeb.get(i);
            
            String rut = (String) abonoBruto[0];
            Date fechaPagoWeb = (Date) abonoBruto[1];
            BigInteger monto = (BigInteger) abonoBruto[2];
            
            List<Demanda> demandas = demFac.filtrarPorRutExacto(rut);
            if (demandas.isEmpty()) {
                throw new BusinessLogicException("El rut '" + rut + "' no está registrado dentro de la base de datos, imposible ingresar los pagos webs de forma automática.");
            }else if (demandas.size() > 1) {
                throw new BusinessLogicException("El rut '" + rut + "' tiene más de 1 demanda en la base de datos, imposible ingresar los pagos web de forma automática.");
            }
            Demanda d = demandas.get(0);
            
            if (d.getProgramaciones().isEmpty()) {
                throw new BusinessLogicException("El rut '" + rut + "' no tiene programaciones aún, primero realizar programación, imposible ingresar los pagos web de forma automática.");
            }
            
            this.pagarAbonoWeb(d.getId(), monto, fechaPagoWeb);
            
        }
        
    }
    
    @RolesAllowed({"ADMIN","NORMAL"})
    private void pagarAbonoWeb(Long idDemanda, BigInteger montoPagado, Date fechaPagoWeb) throws BusinessLogicException {
        
        Principal principal = ctx.getCallerPrincipal();
        Usuario responsable = userFac.getUsuarioPorRut(principal.getName());
        if(responsable == null)
            throw new BusinessLogicException("Debes estar logeado para usar esta caracteristica.");
        
        Demanda d = demFac.find(idDemanda);
        if (d == null) {
            throw new BusinessLogicException("Demanda no encontrada.");
        }
        
//        if (d.getPagadoTotalmente()) {
//            throw new BusinessLogicException("La deuda ya fue cancelada totalmente. Imposible realizar otro abono.");
//        }
        
        List<ProgramacionAbonos> programaciones = d.getProgramaciones();
        if (programaciones.isEmpty()) {
            throw new BusinessLogicException("No hay programaciones pactadas para realizar el pago del abono.");
        }
        
        BigInteger totalPagado = BigInteger.ZERO;
        //la ULTIMA programacion tiene el totalAPagar calculado.
        BigInteger totalAPagar = programaciones.get(programaciones.size() - 1).getTotalAPagar(); 
        for (int i = 0; i < programaciones.size(); i++) {
            ProgramacionAbonos p = programaciones.get(i);
            List<Abono> abonos = p.getAbonos();
            for (int j = 0; j < abonos.size(); j++) {
                Abono abono = abonos.get(j);
                totalPagado = totalPagado.add(abono.getMontoSinDescuento());
            }
        }
        BigInteger restante = totalAPagar.subtract(totalPagado);
        boolean pagadoTotalmente = false;
//        if (restante.subtract(montoPagado).compareTo(BigInteger.ZERO) < 0) {
//            throw new BusinessLogicException("El pago excede la deuda total, debe ser menor.");
//        }else if
        if(restante.subtract(montoPagado).compareTo(BigInteger.ZERO) <= 0) {
            pagadoTotalmente = true;
        }
        
        ProgramacionAbonos ultimaProgramacion = programaciones.get(programaciones.size() - 1);
        
        Abono abono = new Abono();
        abono.setFecha(Calendar.getInstance().getTime());        
        abono.setDescuentoAplicado(new BigDecimal("0.00"));
        abono.setMontoPagado(montoPagado);
        abono.setMontoSinDescuento(montoPagado);
        abono.setResponsable(responsable); 
        abono.setProgramacionAbonos(ultimaProgramacion);
        
        abono.setPagoWeb(true);
        abono.setFechaPagoWeb(fechaPagoWeb);
        
        aboFac.create(abono);
        
        ultimaProgramacion.getAbonos().add(abono);
        
        prograFac.edit(ultimaProgramacion);

        if (pagadoTotalmente) {
            d.setPagadoTotalmente(true);
            demFac.edit(d);
        }
        
    }

    @RolesAllowed({"ADMIN","NORMAL"})
    public void imprimirComprobanteAbono() throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @PermitAll
    public List<Demanda> getDemandasFiltradasPorRut(String rut) throws BusinessLogicException {
        return demFac.filtrarPorRut(rut);
    }
    
    @PermitAll
    public List<Demanda> getDemandasFiltradasPorNombre(String nombre) throws BusinessLogicException {
        return demFac.filtrarPorNombre(nombre);
    }
    
    @PermitAll
    public List<Demanda> getDemandas() {
        return demFac.findAll();
    }

    public List<Usuario> getUsuarios() {
        return userFac.findAll();
    }

}
