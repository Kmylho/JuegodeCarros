package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Carros;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Pistas;
import Entidades.Jugadores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Kmylho
 */
public class CarrosJpaController implements Serializable {

    public CarrosJpaController() {
        this.emf = Persistence.createEntityManagerFactory("CarGamePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carros carros) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pistas idPista = carros.getIdPista();
            if (idPista != null) {
                idPista = em.getReference(idPista.getClass(), idPista.getIdPista());
                carros.setIdPista(idPista);
            }
            Jugadores idJugador = carros.getIdJugador();
            if (idJugador != null) {
                idJugador = em.getReference(idJugador.getClass(), idJugador.getIdJugador());
                carros.setIdJugador(idJugador);
            }
            em.persist(carros);
            if (idPista != null) {
                idPista.getCarrosList().add(carros);
                idPista = em.merge(idPista);
            }
            if (idJugador != null) {
                idJugador.getCarrosList().add(carros);
                idJugador = em.merge(idJugador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarros(carros.getIdCarro()) != null) {
                throw new PreexistingEntityException("Carros " + carros + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carros carros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carros persistentCarros = em.find(Carros.class, carros.getIdCarro());
            Pistas idPistaOld = persistentCarros.getIdPista();
            Pistas idPistaNew = carros.getIdPista();
            Jugadores idJugadorOld = persistentCarros.getIdJugador();
            Jugadores idJugadorNew = carros.getIdJugador();
            if (idPistaNew != null) {
                idPistaNew = em.getReference(idPistaNew.getClass(), idPistaNew.getIdPista());
                carros.setIdPista(idPistaNew);
            }
            if (idJugadorNew != null) {
                idJugadorNew = em.getReference(idJugadorNew.getClass(), idJugadorNew.getIdJugador());
                carros.setIdJugador(idJugadorNew);
            }
            carros = em.merge(carros);
            if (idPistaOld != null && !idPistaOld.equals(idPistaNew)) {
                idPistaOld.getCarrosList().remove(carros);
                idPistaOld = em.merge(idPistaOld);
            }
            if (idPistaNew != null && !idPistaNew.equals(idPistaOld)) {
                idPistaNew.getCarrosList().add(carros);
                idPistaNew = em.merge(idPistaNew);
            }
            if (idJugadorOld != null && !idJugadorOld.equals(idJugadorNew)) {
                idJugadorOld.getCarrosList().remove(carros);
                idJugadorOld = em.merge(idJugadorOld);
            }
            if (idJugadorNew != null && !idJugadorNew.equals(idJugadorOld)) {
                idJugadorNew.getCarrosList().add(carros);
                idJugadorNew = em.merge(idJugadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carros.getIdCarro();
                if (findCarros(id) == null) {
                    throw new NonexistentEntityException("The carros with id " + id + " no longer exists.");
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
            Carros carros;
            try {
                carros = em.getReference(Carros.class, id);
                carros.getIdCarro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carros with id " + id + " no longer exists.", enfe);
            }
            Pistas idPista = carros.getIdPista();
            if (idPista != null) {
                idPista.getCarrosList().remove(carros);
                idPista = em.merge(idPista);
            }
            Jugadores idJugador = carros.getIdJugador();
            if (idJugador != null) {
                idJugador.getCarrosList().remove(carros);
                idJugador = em.merge(idJugador);
            }
            em.remove(carros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carros> findCarrosEntities() {
        return findCarrosEntities(true, -1, -1);
    }

    public List<Carros> findCarrosEntities(int maxResults, int firstResult) {
        return findCarrosEntities(false, maxResults, firstResult);
    }

    private List<Carros> findCarrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carros.class));
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

    public Carros findCarros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carros.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carros> rt = cq.from(Carros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
