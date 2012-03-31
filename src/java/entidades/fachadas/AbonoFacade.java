/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.fachadas;

import entidades.Abono;
import entidades.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rulyone
 */
@Stateless
public class AbonoFacade extends AbstractFacade<Abono> {
    @PersistenceContext(unitName = "SoftenPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AbonoFacade() {
        super(Abono.class);
    }
    
    public List<Abono> buscarPorResponsable(Usuario responsable) {
        return em.createNamedQuery("Abono.buscarPorResponsable", Abono.class)
                .setParameter("responsable", responsable)
                .getResultList();
    }
    
}
