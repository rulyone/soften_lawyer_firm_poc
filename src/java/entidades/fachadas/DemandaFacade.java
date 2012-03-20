/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.fachadas;

import entidades.Demanda;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rulyone
 */
@Stateless
public class DemandaFacade extends AbstractFacade<Demanda> {
    @PersistenceContext(unitName = "SoftenPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DemandaFacade() {
        super(Demanda.class);
    }
    
    public List<Demanda> filtrarPorRut(String rut) {
        return em.createNamedQuery("Demanda.filtrarPorRut", Demanda.class)
                .setParameter("rut", "%" + rut + "%")
                .getResultList();
    }
    
    public List<Demanda> filtrarPorNombre(String nombre) {
        return em.createNamedQuery("Demanda.filtrarPorNombre", Demanda.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }

    public List<Demanda> filtrarPorRutExacto(String rut) {
        return em.createNamedQuery("Demanda.filtrarPorRutExacto", Demanda.class)
                .setParameter("rut", rut)
                .getResultList();
    }
    
}
