/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import servicios.Servicios;
import servicios.Util;

/**
 *
 * @author rulyone
 */
@Named
@SessionScoped
public class LoginMB implements Serializable {

    @EJB private Servicios servicios;
    
    //para programmatic login.
    private String rut;
    private String password;

    /** Creates a new instance of LoginMB */
    public LoginMB() {
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

    public String login() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
        boolean logeado = false;
        try {
            req.login(rut, password);
            
            servicios.actualizarUltimoAcceso(rut);
            
            logeado = true;

        } catch (ServletException ex) {
            if (ex.getMessage().equals("Attempt to re-login while the user identity already exists")) {
                return "/aplicacion.xhtml";
            }
            Util.addErrorMessage("Login fallido", "Username o password no coinciden.");
            this.rut = null;
        }
        this.password = null;
        
        if (logeado) {
            return "/aplicacion.xhtml";
        }
        return null;
    }

    public String logout() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
        try {
            req.logout();
            ctx.getExternalContext().getSessionMap().remove("user");
            req.getSession(false).invalidate();
            this.rut = null;
            this.password = null;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Te has deslogeado satisfactoriamente.",""));
        } catch (ServletException ex) {
            ctx.addMessage(null, new FacesMessage("Error al deslogear"));
        } catch (IllegalStateException ex) {
            ctx.addMessage(null, new FacesMessage("Error al deslogear"));
        }
        return "/index.xhtml";
    }

}
