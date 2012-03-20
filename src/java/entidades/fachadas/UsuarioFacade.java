/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.fachadas;

import entidades.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rulyone
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "SoftenPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario getUsuarioPorRut(String rut) {
        Usuario u = null;
        try {
             u = em.createNamedQuery("Usuario.buscarPorRut", Usuario.class)
                .setParameter("rut", rut)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        return u;
    }
    
}
