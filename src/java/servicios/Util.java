/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entidades.Abono;
import entidades.Demanda;
import entidades.ProgramacionAbonos;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
//import model.entities.inhouse.PlayerIH;

/**
 *
 * @author Pablo
 */
public class Util implements Serializable {


    public static String cambiarSlashes(String str) {
        return str.replaceAll("\\\\", "/");
    }

    public static String hashPassword(String rawpassword) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = rawpassword.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1,m.digest());
            return String.format("%1$032X", i).toLowerCase();
        }catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    
    public static String hashPassword(String rawpassword, String username) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            String str = rawpassword.concat(username);
            byte[] data = str.getBytes();
            m.update(data,0,data.length);
            BigInteger i = new BigInteger(1,m.digest());
            return String.format("%1$032X", i).toLowerCase();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void addErrorMessage(String summary, String detail) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        msg.setSummary(summary);
        msg.setDetail(detail);
        ctx.addMessage(null, msg);
    }

    public static void addInfoMessage(String summary, String detail) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary(summary);
        msg.setDetail(detail);
        ctx.addMessage(null, msg);
    }

    public static void addFatalMessage(String summary, String detail) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_FATAL);
        msg.setSummary(summary);
        msg.setDetail(detail);
        ctx.addMessage(null, msg);
    }

    public static void addWarnMessage(String summary, String detail) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        msg.setSummary(summary);
        msg.setDetail(detail);
        ctx.addMessage(null, msg);
    }

    public static Date dateSinMillis(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date dateSinTime(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static void keepMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void navigate(String where) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, where);
        ctx.renderResponse();
    }
    
    public static List<Demanda> parsearDemandasCSV(String formatoCSV) throws BusinessLogicException {
        List<Demanda> demandas = new ArrayList<Demanda>();
        formatoCSV = formatoCSV.trim();
        BufferedReader reader = new BufferedReader(new StringReader(formatoCSV));
        
        String line = null;
        
        try {
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                if (split.length == 24) {
                    Demanda demanda = new Demanda();
                    demanda.setNumeroTarjeta(split[0].trim());
                    demanda.setRut(split[1].trim());
                    demanda.setNombreCompleto(split[2].trim());
                    demanda.setSexo(split[3].trim());
                    demanda.setCalleParticular(split[4].trim());
                    demanda.setDepartamentoParticular(split[5].trim());
                    demanda.setNumeroParticular(split[6].trim());
                    demanda.setComunaParticular(split[7].trim());
                    demanda.setCodigoAreaParticular(split[8].trim());
                    demanda.setTelefonoParticular(split[9].trim());
                    demanda.setCalleComercial(split[10].trim());
                    demanda.setDepartamentoComercial(split[11].trim());
                    demanda.setNumeroComercial(split[12].trim());
                    demanda.setComunaComercial(split[13].trim());
                    demanda.setCodigoAreaComercial(split[14].trim());
                    demanda.setTelefonoComercial(split[15].trim());
                    demanda.setCelular(split[16].trim());
                    demanda.setCapital(new BigInteger(split[17].trim()));
                    demanda.setCapitalPagare(new BigInteger(split[18].trim()));                    
                    demanda.setVencimientoDeuda(parsearFecha(split[19].trim()));
                    demanda.setVencimientoPagare(parsearFecha(split[20].trim()));
                    demanda.setCodigoAbogado(split[21].trim());
                    demanda.setFechaAsignacion(parsearFecha(split[22].trim()));
                    demanda.setFechaDemandaCorte(null);
                    
                    demandas.add(demanda);
                    
                }else{
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, "ERROR AL PARSEAR DATOS, SE ASUMEN 24 DATOS, PERO LLEGARON LOS SIGUIENTES (" + split.length + "): " + line);
                    throw new BusinessLogicException("Error al ingresar datos de las demandas.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessLogicException("Error al ingresar datos de las demandas.");
        }
        
        return demandas;
    }
    
    public static List<Object[]> parsearPagosWeb(String pagosWeb) throws BusinessLogicException {
        
        //String, Date, BigInteger
        //rut, fechapagoweb, monto
        List<Object[]> abonos = new ArrayList<Object[]>();
        pagosWeb = pagosWeb.trim();
        
        BufferedReader reader = new BufferedReader(new StringReader(pagosWeb));
        
        String line = null;
        
        try {
            //elimnamos la primera linea q no nos interesa.
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                
                String[] split = line.split("\\s+");
                
                if (split.length == 4) {
                    //split[0] no se toma en cuena... viene el nro de tarjeta
                    String fechaYPago = split[1].trim();
                    String codigoFalabella = split[2].trim(); //si es 0 no tomar en cuenta.
                    if (codigoFalabella.equals("0")) {
                        continue;
                    }
                    String rut = split[3].trim(); //eliminar el guión, si es que lo posee.
                    
                    String fecha = fechaYPago.substring(0, 8);
                    String pago = fechaYPago.substring(8, 23);
                    
                    StringBuilder sb = new StringBuilder();
                    
                    boolean cerosInicialesParseados = false;
                    for (int i = 0; i < rut.length(); i++) {
                        char c = rut.charAt(i);
                        if (!cerosInicialesParseados && c == '0') {
                            continue;
                        }
                        if (!cerosInicialesParseados && c != '0') {
                            cerosInicialesParseados = true;
                        }
                        if (c != '-') {
                            sb.append(c);
                        }
                    }
                    
                    rut = sb.toString();
                    
                    Object[] abono = new Object[3];
                    abono[0] = rut;
                    abono[1] = Util.parsearFechaPagosWeb(fecha);
                    abono[2] = new BigInteger(pago);
                    
                    abonos.add(abono);
                    
                }else{
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, "ERROR AL PARSEAR PAGOS WEB, SE ASUMEN 4 DATOS, PERO LLEGARON LOS SIGUIENTES (" + split.length + "): " + line);
                    throw new BusinessLogicException("Error al ingresar pagos web.");
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessLogicException("Error al ingresar pagos web.");
        }
        
        return abonos;
    }
    
    public static Date parsearFechaPagosWeb(String fecha) throws BusinessLogicException {
        //formato DDMMYYYY
        int dia = 0;
        int mes = 0;
        int anio = 0;
        try {
            dia = Integer.parseInt(fecha.substring(0,2));
            mes = Integer.parseInt(fecha.substring(2,4));
            anio = Integer.parseInt(fecha.substring(4,8));
        } catch (NumberFormatException ex) {
            throw new BusinessLogicException("Error al intentar traducir la fecha en los pagos web.");
        }
        return new GregorianCalendar(anio,mes,dia).getTime();
    }
    
    public static Date parsearFecha(String fecha) {
        //formato YYYYMMDD
        int anio = Integer.parseInt(fecha.substring(0, 4));
        int mes = Integer.parseInt(fecha.substring(4, 6));
        int dia = Integer.parseInt(fecha.substring(6,8));
        return new GregorianCalendar(anio,mes,dia).getTime();
    }

    public static void imprimirComprobante(Abono abono) {
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new Watermark());
            if (!document.isOpen()) {
                document.open();
            }
            
            
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = format.format(abono.getFecha());
            document.add(new Paragraph(fecha));
            
            document.add(new Paragraph("ABOGADO ANDRÉS MAINO", FontFactory.getFont(FontFactory.COURIER_BOLDOBLIQUE, 20f)));
            
            Paragraph p = new Paragraph("COMPROBANTE DE RECIBO DE DINERO", FontFactory.getFont(FontFactory.COURIER_BOLD, 16f));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(Chunk.NEWLINE);
                                    
            Phrase phrase = new Phrase();
            phrase.add(new Chunk("El abogado ", FontFactory.getFont(FontFactory.HELVETICA, 8f)));
            phrase.add(new Chunk("Andrés Maino Benitez", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 8f)));
            phrase.add(new Chunk(", por cuenta de PROMOTORA FALABELLA S.A da recibo de lo siguiente.", FontFactory.getFont(FontFactory.HELVETICA, 8f)));
            
            p = new Paragraph(phrase);
            p.setAlignment(Element.ALIGN_CENTER);
            
            document.add(p);
            
            document.add(Chunk.NEWLINE);
            
            PdfPTable table = new PdfPTable(new float[]{10f, 30f});
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER); 
            
            table.addCell(new Phrase("FOLIO:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(String.valueOf(abono.getId())));
            
            table.addCell(new Phrase("FECHA:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(fecha, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            int numAbono = 1;
            int numProgramacion = 0;
            boolean listo = false;
            List<ProgramacionAbonos> programaciones = abono.getProgramacionAbonos().getDemanda().getProgramaciones();
            for (int i = 0; !listo && i < programaciones.size(); i++) {
                ProgramacionAbonos programacion = programaciones.get(i);
                List<Abono> abonos = programacion.getAbonos();
                for (int j = 0; !listo && j < abonos.size(); j++) {
                    Abono a = abonos.get(j);
                    if (a.equals(abono)) {
                        listo = true;
                        numProgramacion = i+1;
                    }else{
                        numAbono++;
                    }
                }
            }
            
            
            table.addCell(new Phrase("Abono:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(String.valueOf(numAbono) + " (realizado en la programación #" + numProgramacion + ").", FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("RUT:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(formatearRut(abono.getProgramacionAbonos().getDemanda().getRut()), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            String nombre = abono.getProgramacionAbonos().getDemanda().getNombreCompleto();
            table.addCell(new Phrase("Nombre:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(nombre, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            
            String caratulado = "PROMOTORA CMR FALABELLA con - " + nombre;
            table.addCell(new Phrase("Caratulado:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(caratulado, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("ROL:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getProgramacionAbonos().getDemanda().getRol(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Juzgado:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getProgramacionAbonos().getDemanda().getTribunal(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Materia:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase("JUICIO EJECUTIVO", FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Monto:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(formatearDinero(abono.getMontoPagado().toString()), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Atendido por:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getResponsable().toString(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
                        
            document.add(table);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(new Chunk("----------------------------------------------------------------------------------------------------------------------------"));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            
            
            document.add(new Paragraph(fecha));
            
            document.add(new Paragraph("ABOGADO ANDRÉS MAINO", FontFactory.getFont(FontFactory.COURIER_BOLDOBLIQUE, 20f)));
            
            p = new Paragraph("COMPROBANTE DE RECIBO DE DINERO", FontFactory.getFont(FontFactory.COURIER_BOLD, 16f));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(Chunk.NEWLINE);
                                    
            phrase = new Phrase();
            phrase.add(new Chunk("El abogado ", FontFactory.getFont(FontFactory.HELVETICA, 8f)));
            phrase.add(new Chunk("Andrés Maino Benitez", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 8f)));
            phrase.add(new Chunk(", por cuenta de PROMOTORA FALABELLA S.A da recibo de lo siguiente.", FontFactory.getFont(FontFactory.HELVETICA, 8f)));
            
            p = new Paragraph(phrase);
            p.setAlignment(Element.ALIGN_CENTER);
            
            document.add(p);
            
            document.add(Chunk.NEWLINE);
            
            table = new PdfPTable(new float[]{10f, 30f});
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER); 
            
            table.addCell(new Phrase("FOLIO:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(String.valueOf(abono.getId())));
            
            table.addCell(new Phrase("FECHA:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(fecha, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
                                    
            table.addCell(new Phrase("Abono:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(String.valueOf(numAbono) + " (realizado en la programación #" + numProgramacion + ").", FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("RUT:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(formatearRut(abono.getProgramacionAbonos().getDemanda().getRut()), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            
            table.addCell(new Phrase("Nombre:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(nombre, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
              
            table.addCell(new Phrase("Caratulado:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(caratulado, FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("ROL:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getProgramacionAbonos().getDemanda().getRol(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Juzgado:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getProgramacionAbonos().getDemanda().getTribunal(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Materia:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase("JUICIO EJECUTIVO", FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Monto:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(formatearDinero(abono.getMontoPagado().toString()), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
            
            table.addCell(new Phrase("Atendido por:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
            table.addCell(new Phrase(abono.getResponsable().toString(), FontFactory.getFont(FontFactory.HELVETICA, 10f)));
                        
            document.add(table);
            
            document.close();
            
            writePDFToResponse(((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()), baos, "comprobante");
        } catch (DocumentException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private static void writePDFToResponse(HttpServletResponse response, ByteArrayOutputStream baos, String fileName) throws IOException, DocumentException {     
        response.setContentType("application/pdf");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".pdf");
        response.setContentLength(baos.size());
        
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }
    
    public static String formatearDinero(String dinero) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"));
        return nf.format(Integer.parseInt(dinero));
    }
    
    public static String formatearRut(String rut) {
        String r1 = rut.substring(0, rut.length()-1);
        String r2 = rut.substring(rut.length() - 1, rut.length());
        int parsed = Integer.parseInt(r1);
        NumberFormat f = NumberFormat.getInstance();
        f.setCurrency(Currency.getInstance("CLP"));
        String format = f.format(parsed);
        return format + "-" + r2;
        
    }
    
}
