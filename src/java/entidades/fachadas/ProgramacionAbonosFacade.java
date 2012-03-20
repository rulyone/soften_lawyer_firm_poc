/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.fachadas;

import entidades.ProgramacionAbonos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rulyone
 */
@Stateless
public class ProgramacionAbonosFacade extends AbstractFacade<ProgramacionAbonos> {
    @PersistenceContext(unitName = "SoftenPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramacionAbonosFacade() {
        super(ProgramacionAbonos.class);
    }
    
}
