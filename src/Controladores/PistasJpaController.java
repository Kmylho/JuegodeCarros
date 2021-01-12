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
import Entidades.Pistas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Kmylho
 */
public class PistasJpaController implements Serializable {

    public PistasJpaController() {
        this.emf = Persistence.createEntityManagerFactory("CarGamePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pistas pistas) throws PreexistingEntityException, Exception {
        if (pistas.getPodioList() == null) {
            pistas.setPodioList(new ArrayList<Podio>());
        }
        if (pistas.getCarrosList() == null) {
            pistas.setCarrosList(new ArrayList<Carros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Podio> attachedPodioList = new ArrayList<Podio>();
            for (Podio podioListPodioToAttach : pistas.getPodioList()) {
                podioListPodioToAttach = em.getReference(podioListPodioToAttach.getClass(), podioListPodioToAttach.getIdCarrera());
                attachedPodioList.add(podioListPodioToAttach);
            }
            pistas.setPodioList(attachedPodioList);
            List<Carros> attachedCarrosList = new ArrayList<Carros>();
            for (Carros carrosListCarrosToAttach : pistas.getCarrosList()) {
                carrosListCarrosToAttach = em.getReference(carrosListCarrosToAttach.getClass(), carrosListCarrosToAttach.getIdCarro());
                attachedCarrosList.add(carrosListCarrosToAttach);
            }
            pistas.setCarrosList(attachedCarrosList);
            em.persist(pistas);
            for (Podio podioListPodio : pistas.getPodioList()) {
                Pistas oldIdPistaOfPodioListPodio = podioListPodio.getIdPista();
                podioListPodio.setIdPista(pistas);
                podioListPodio = em.merge(podioListPodio);
                if (oldIdPistaOfPodioListPodio != null) {
                    oldIdPistaOfPodioListPodio.getPodioList().remove(podioListPodio);
                    oldIdPistaOfPodioListPodio = em.merge(oldIdPistaOfPodioListPodio);
                }
            }
            for (Carros carrosListCarros : pistas.getCarrosList()) {
                Pistas oldIdPistaOfCarrosListCarros = carrosListCarros.getIdPista();
                carrosListCarros.setIdPista(pistas);
                carrosListCarros = em.merge(carrosListCarros);
                if (oldIdPistaOfCarrosListCarros != null) {
                    oldIdPistaOfCarrosListCarros.getCarrosList().remove(carrosListCarros);
                    oldIdPistaOfCarrosListCarros = em.merge(oldIdPistaOfCarrosListCarros);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPistas(pistas.getIdPista()) != null) {
                throw new PreexistingEntityException("Pistas " + pistas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pistas pistas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pistas persistentPistas = em.find(Pistas.class, pistas.getIdPista());
            List<Podio> podioListOld = persistentPistas.getPodioList();
            List<Podio> podioListNew = pistas.getPodioList();
            List<Carros> carrosListOld = persistentPistas.getCarrosList();
            List<Carros> carrosListNew = pistas.getCarrosList();
            List<String> illegalOrphanMessages = null;
            for (Podio podioListOldPodio : podioListOld) {
                if (!podioListNew.contains(podioListOldPodio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Podio " + podioListOldPodio + " since its idPista field is not nullable.");
                }
            }
            for (Carros carrosListOldCarros : carrosListOld) {
                if (!carrosListNew.contains(carrosListOldCarros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carros " + carrosListOldCarros + " since its idPista field is not nullable.");
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
            pistas.setPodioList(podioListNew);
            List<Carros> attachedCarrosListNew = new ArrayList<Carros>();
            for (Carros carrosListNewCarrosToAttach : carrosListNew) {
                carrosListNewCarrosToAttach = em.getReference(carrosListNewCarrosToAttach.getClass(), carrosListNewCarrosToAttach.getIdCarro());
                attachedCarrosListNew.add(carrosListNewCarrosToAttach);
            }
            carrosListNew = attachedCarrosListNew;
            pistas.setCarrosList(carrosListNew);
            pistas = em.merge(pistas);
            for (Podio podioListNewPodio : podioListNew) {
                if (!podioListOld.contains(podioListNewPodio)) {
                    Pistas oldIdPistaOfPodioListNewPodio = podioListNewPodio.getIdPista();
                    podioListNewPodio.setIdPista(pistas);
                    podioListNewPodio = em.merge(podioListNewPodio);
                    if (oldIdPistaOfPodioListNewPodio != null && !oldIdPistaOfPodioListNewPodio.equals(pistas)) {
                        oldIdPistaOfPodioListNewPodio.getPodioList().remove(podioListNewPodio);
                        oldIdPistaOfPodioListNewPodio = em.merge(oldIdPistaOfPodioListNewPodio);
                    }
                }
            }
            for (Carros carrosListNewCarros : carrosListNew) {
                if (!carrosListOld.contains(carrosListNewCarros)) {
                    Pistas oldIdPistaOfCarrosListNewCarros = carrosListNewCarros.getIdPista();
                    carrosListNewCarros.setIdPista(pistas);
                    carrosListNewCarros = em.merge(carrosListNewCarros);
                    if (oldIdPistaOfCarrosListNewCarros != null && !oldIdPistaOfCarrosListNewCarros.equals(pistas)) {
                        oldIdPistaOfCarrosListNewCarros.getCarrosList().remove(carrosListNewCarros);
                        oldIdPistaOfCarrosListNewCarros = em.merge(oldIdPistaOfCarrosListNewCarros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pistas.getIdPista();
                if (findPistas(id) == null) {
                    throw new NonexistentEntityException("The pistas with id " + id + " no longer exists.");
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
            Pistas pistas;
            try {
                pistas = em.getReference(Pistas.class, id);
                pistas.getIdPista();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pistas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Podio> podioListOrphanCheck = pistas.getPodioList();
            for (Podio podioListOrphanCheckPodio : podioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pistas (" + pistas + ") cannot be destroyed since the Podio " + podioListOrphanCheckPodio + " in its podioList field has a non-nullable idPista field.");
            }
            List<Carros> carrosListOrphanCheck = pistas.getCarrosList();
            for (Carros carrosListOrphanCheckCarros : carrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pistas (" + pistas + ") cannot be destroyed since the Carros " + carrosListOrphanCheckCarros + " in its carrosList field has a non-nullable idPista field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pistas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pistas> findPistasEntities() {
        return findPistasEntities(true, -1, -1);
    }

    public List<Pistas> findPistasEntities(int maxResults, int firstResult) {
        return findPistasEntities(false, maxResults, firstResult);
    }

    private List<Pistas> findPistasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pistas.class));
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

    public Pistas findPistas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pistas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPistasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pistas> rt = cq.from(Pistas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
