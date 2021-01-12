package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Jugadores;
import Entidades.Pistas;
import Entidades.Podio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Kmylho
 */
public class PodioJpaController implements Serializable {

    public PodioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("CarGamePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Podio podio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugadores jugadores = podio.getJugadores();
            if (jugadores != null) {
                jugadores = em.getReference(jugadores.getClass(), jugadores.getIdJugador());
                podio.setJugadores(jugadores);
            }
            Pistas idPista = podio.getIdPista();
            if (idPista != null) {
                idPista = em.getReference(idPista.getClass(), idPista.getIdPista());
                podio.setIdPista(idPista);
            }
            em.persist(podio);
            if (jugadores != null) {
                jugadores.getPodioList().add(podio);
                jugadores = em.merge(jugadores);
            }
            if (idPista != null) {
                idPista.getPodioList().add(podio);
                idPista = em.merge(idPista);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPodio(podio.getIdCarrera()) != null) {
                throw new PreexistingEntityException("Podio " + podio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Podio podio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Podio persistentPodio = em.find(Podio.class, podio.getIdCarrera());
            Jugadores jugadoresOld = persistentPodio.getJugadores();
            Jugadores jugadoresNew = podio.getJugadores();
            Pistas idPistaOld = persistentPodio.getIdPista();
            Pistas idPistaNew = podio.getIdPista();
            if (jugadoresNew != null) {
                jugadoresNew = em.getReference(jugadoresNew.getClass(), jugadoresNew.getIdJugador());
                podio.setJugadores(jugadoresNew);
            }
            if (idPistaNew != null) {
                idPistaNew = em.getReference(idPistaNew.getClass(), idPistaNew.getIdPista());
                podio.setIdPista(idPistaNew);
            }
            podio = em.merge(podio);
            if (jugadoresOld != null && !jugadoresOld.equals(jugadoresNew)) {
                jugadoresOld.getPodioList().remove(podio);
                jugadoresOld = em.merge(jugadoresOld);
            }
            if (jugadoresNew != null && !jugadoresNew.equals(jugadoresOld)) {
                jugadoresNew.getPodioList().add(podio);
                jugadoresNew = em.merge(jugadoresNew);
            }
            if (idPistaOld != null && !idPistaOld.equals(idPistaNew)) {
                idPistaOld.getPodioList().remove(podio);
                idPistaOld = em.merge(idPistaOld);
            }
            if (idPistaNew != null && !idPistaNew.equals(idPistaOld)) {
                idPistaNew.getPodioList().add(podio);
                idPistaNew = em.merge(idPistaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = podio.getIdCarrera();
                if (findPodio(id) == null) {
                    throw new NonexistentEntityException("The podio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Podio podio;
            try {
                podio = em.getReference(Podio.class, id);
                podio.getIdCarrera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The podio with id " + id + " no longer exists.", enfe);
            }
            Jugadores jugadores = podio.getJugadores();
            if (jugadores != null) {
                jugadores.getPodioList().remove(podio);
                jugadores = em.merge(jugadores);
            }
            Pistas idPista = podio.getIdPista();
            if (idPista != null) {
                idPista.getPodioList().remove(podio);
                idPista = em.merge(idPista);
            }
            em.remove(podio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Podio> findPodioEntities() {
        return findPodioEntities(true, -1, -1);
    }

    public List<Podio> findPodioEntities(int maxResults, int firstResult) {
        return findPodioEntities(false, maxResults, firstResult);
    }

    private List<Podio> findPodioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Podio.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Podio findPodio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Podio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPodioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Podio> rt = cq.from(Podio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
