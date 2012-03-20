/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author rulyone
 */
@Entity
@Table(name="USUARIOS")
@NamedQueries({
    @NamedQuery(name="Usuario.buscarPorRut", query="SELECT u FROM Usuario u WHERE u.rut = :rut")
})
public class Usuario implements Serializable {    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Length(min=5, max=20)
    private String rut;
    @Column(nullable = false, length=32)
    @NotNull
    @Length(min=32, max=32)
    private String password;
    @Column(nullable = false, length=255)
    @NotNull
    @Length(min=10, max=255)
    private String nombreCompleto;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date fechaCreacion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date fechaUltimoAcceso;
    @Column(nullable = false)
    @NotNull
    private Boolean bloqueado;
    
    @OneToMany(mappedBy = "responsable")
    private List<ProgramacionAbonos> programaciones;
    
    @OneToMany(mappedBy = "responsable")
    private List<Abono> abonos;
    
    @Column(nullable=false)
    @NotNull
    private String rol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public void setAbonos(List<Abono> abonos) {
        this.abonos = abonos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
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

    public List<ProgramacionAbonos> getProgramaciones() {
        return programaciones;
    }

    public void setProgramaciones(List<ProgramacionAbonos> programaciones) {
        this.programaciones = programaciones;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNombreCompleto();
    }
    
}
