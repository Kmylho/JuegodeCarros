package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Podio;
import java.util.ArrayList;
import java.util.List;
import Entidades.Carros;
import Entidades.Jugadores;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Kmylho
 */
public class JugadoresJpaController implements Serializable {

    public JugadoresJpaController() {
        this.emf = Persistence.createEntityManagerFactory("CarGamePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugadores jugadores) throws PreexistingEntityException, Exception {
        if (jugadores.getPodioList() == null) {
            jugadores.setPodioList(new ArrayList<Podio>());
        }
        if (jugadores.getCarrosList() == null) {
            jugadores.setCarrosList(new ArrayList<Carros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Podio> attachedPodioList = new ArrayList<Podio>();
            for (Podio podioListPodioToAttach : jugadores.getPodioList()) {
                podioListPodioToAttach = em.getReference(podioListPodioToAttach.getClass(), podioListPodioToAttach.getIdCarrera());
                attachedPodioList.add(podioListPodioToAttach);
            }
            jugadores.setPodioList(attachedPodioList);
            List<Carros> attachedCarrosList = new ArrayList<Carros>();
            for (Carros carrosListCarrosToAttach : jugadores.getCarrosList()) {
                carrosListCarrosToAttach = em.getReference(carrosListCarrosToAttach.getClass(), carrosListCarrosToAttach.getIdCarro());
                attachedCarrosList.add(carrosListCarrosToAttach);
            }
            jugadores.setCarrosList(attachedCarrosList);
            em.persist(jugadores);
            for (Podio podioListPodio : jugadores.getPodioList()) {
                Jugadores oldJugadoresOfPodioListPodio = podioListPodio.getJugadores();
                podioListPodio.setJugadores(jugadores);
                podioListPodio = em.merge(podioListPodio);
                if (oldJugadoresOfPodioListPodio != null) {
                    oldJugadoresOfPodioListPodio.getPodioList().remove(podioListPodio);
                    oldJugadoresOfPodioListPodio = em.merge(oldJugadoresOfPodioListPodio);
                }
            }
            for (Carros carrosListCarros : jugadores.getCarrosList()) {
                Jugadores oldIdJugadorOfCarrosListCarros = carrosListCarros.getIdJugador();
                carrosListCarros.setIdJugador(jugadores);
                carrosListCarros = em.merge(carrosListCarros);
                if (oldIdJugadorOfCarrosListCarros != null) {
                    oldIdJugadorOfCarrosListCarros.getCarrosList().remove(carrosListCarros);
                    oldIdJugadorOfCarrosListCarros = em.merge(oldIdJugadorOfCarrosListCarros);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugadores(jugadores.getIdJugador()) != null) {
                throw new PreexistingEntityException("Jugadores " + jugadores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugadores jugadores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugadores persistentJugadores = em.find(Jugadores.class, jugadores.getIdJugador());
            List<Podio> podioListOld = persistentJugadores.getPodioList();
            List<Podio> podioListNew = jugadores.getPodioList();
            List<Carros> carrosListOld = persistentJugadores.getCarrosList();
            List<Carros> carrosListNew = jugadores.getCarrosList();
            List<String> illegalOrphanMessages = null;
            for (Podio podioListOldPodio : podioListOld) {
                if (!podioListNew.contains(podioListOldPodio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Podio " + podioListOldPodio + " since its jugadores field is not nullable.");
                }
            }
            for (Carros carrosListOldCarros : carrosListOld) {
                if (!carrosListNew.contains(carrosListOldCarros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carros " + carrosListOldCarros + " since its idJugador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Podio> attachedPodioListNew = new ArrayList<Podio>();
            for (Podio podioListNewPodioToAttach : podioListNew) {
                podioListNewPodioToAttach = em.getReference(podioListNewPodioToAttach.getClass(), podioListNewPodioToAttach.getIdCarrera());
                attachedPodioListNew.add(podioListNewPodioToAttach);
            }
            podioListNew = attachedPodioListNew;
            jugadores.setPodioList(podioListNew);
            List<Carros> attachedCarrosListNew = new ArrayList<Carros>();
            for (Carros carrosListNewCarrosToAttach : carrosListNew) {
                carrosListNewCarrosToAttach = em.getReference(carrosListNewCarrosToAttach.getClass(), carrosListNewCarrosToAttach.getIdCarro());
                attachedCarrosListNew.add(carrosListNewCarrosToAttach);
            }
            carrosListNew = attachedCarrosListNew;
            jugadores.setCarrosList(carrosListNew);
            jugadores = em.merge(jugadores);
            for (Podio podioListNewPodio : podioListNew) {
                if (!podioListOld.contains(podioListNewPodio)) {
                    Jugadores oldJugadoresOfPodioListNewPodio = podioListNewPodio.getJugadores();
                    podioListNewPodio.setJugadores(jugadores);
                    podioListNewPodio = em.merge(podioListNewPodio);
                    if (oldJugadoresOfPodioListNewPodio != null && !oldJugadoresOfPodioListNewPodio.equals(jugadores)) {
                        oldJugadoresOfPodioListNewPodio.getPodioList().remove(podioListNewPodio);
                        oldJugadoresOfPodioListNewPodio = em.merge(oldJugadoresOfPodioListNewPodio);
                    }
                }
            }
            for (Carros carrosListNewCarros : carrosListNew) {
                if (!carrosListOld.contains(carrosListNewCarros)) {
                    Jugadores oldIdJugadorOfCarrosListNewCarros = carrosListNewCarros.getIdJugador();
                    carrosListNewCarros.setIdJugador(jugadores);
                    carrosListNewCarros = em.merge(carrosListNewCarros);
                    if (oldIdJugadorOfCarrosListNewCarros != null && !oldIdJugadorOfCarrosListNewCarros.equals(jugadores)) {
                        oldIdJugadorOfCarrosListNewCarros.getCarrosList().remove(carrosListNewCarros);
                        oldIdJugadorOfCarrosListNewCarros = em.merge(oldIdJugadorOfCarrosListNewCarros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jugadores.getIdJugador();
                if (findJugadores(id) == null) {
                    throw new NonexistentEntityException("The jugadores with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugadores jugadores;
            try {
                jugadores = em.getReference(Jugadores.class, id);
                jugadores.getIdJugador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugadores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Podio> podioListOrphanCheck = jugadores.getPodioList();
            for (Podio podioListOrphanCheckPodio : podioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugadores (" + jugadores + ") cannot be destroyed since the Podio " + podioListOrphanCheckPodio + " in its podioList field has a non-nullable jugadores field.");
            }
            List<Carros> carrosListOrphanCheck = jugadores.getCarrosList();
            for (Carros carrosListOrphanCheckCarros : carrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugadores (" + jugadores + ") cannot be destroyed since the Carros " + carrosListOrphanCheckCarros + " in its carrosList field has a non-nullable idJugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(jugadores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugadores> findJugadoresEntities() {
        return findJugadoresEntities(true, -1, -1);
    }

    public List<Jugadores> findJugadoresEntities(int maxResults, int firstResult) {
        return findJugadoresEntities(false, maxResults, firstResult);
    }

    private List<Jugadores> findJugadoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugadores.class));
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

    public Jugadores findJugadores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugadores.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugadores> rt = cq.from(Jugadores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
