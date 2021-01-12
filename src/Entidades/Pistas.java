package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kmylho
 */
@Entity
@Table(name = "Pistas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pistas.findAll", query = "SELECT p FROM Pistas p")
    , @NamedQuery(name = "Pistas.findByIdPista", query = "SELECT p FROM Pistas p WHERE p.idPista = :idPista")
    , @NamedQuery(name = "Pistas.findByNamePista", query = "SELECT p FROM Pistas p WHERE p.namePista = :namePista")
    , @NamedQuery(name = "Pistas.findByKm", query = "SELECT p FROM Pistas p WHERE p.km = :km")})
public class Pistas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdPista")
    private Integer idPista;
    @Basic(optional = false)
    @Column(name = "NamePista")
    private String namePista;
    @Basic(optional = false)
    @Column(name = "KM")
    private int km;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPista")
    private List<Podio> podioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPista")
    private List<Carros> carrosList;

    public Pistas() {
    }

    public Pistas(Integer idPista) {
        this.idPista = idPista;
    }

    public Pistas(Integer idPista, String namePista, int km) {
        this.idPista = idPista;
        this.namePista = namePista;
        this.km = km;
    }

    public Integer getIdPista() {
        return idPista;
    }

    public void setIdPista(Integer idPista) {
        this.idPista = idPista;
    }

    public String getNamePista() {
        return namePista;
    }

    public void setNamePista(String namePista) {
        this.namePista = namePista;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    @XmlTransient
    public List<Podio> getPodioList() {
        return podioList;
    }

    public void setPodioList(List<Podio> podioList) {
        this.podioList = podioList;
    }

    @XmlTransient
    public List<Carros> getCarrosList() {
        return carrosList;
    }

    public void setCarrosList(List<Carros> carrosList) {
        this.carrosList = carrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPista != null ? idPista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pistas)) {
            return false;
        }
        Pistas other = (Pistas) object;
        if ((this.idPista == null && other.idPista != null) || (this.idPista != null && !this.idPista.equals(other.idPista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controladores.Pistas[ idPista=" + idPista + " ]";
    }
    
}
