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
@Table(name = "Jugadores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugadores.findAll", query = "SELECT j FROM Jugadores j")
    , @NamedQuery(name = "Jugadores.findByIdJugador", query = "SELECT j FROM Jugadores j WHERE j.idJugador = :idJugador")
    , @NamedQuery(name = "Jugadores.findByNameJugador", query = "SELECT j FROM Jugadores j WHERE j.nameJugador = :nameJugador")
    , @NamedQuery(name = "Jugadores.findByVecesPrimero", query = "SELECT j FROM Jugadores j WHERE j.vecesPrimero = :vecesPrimero")
    , @NamedQuery(name = "Jugadores.findByVecesSegundo", query = "SELECT j FROM Jugadores j WHERE j.vecesSegundo = :vecesSegundo")
    , @NamedQuery(name = "Jugadores.findByVecesTercero", query = "SELECT j FROM Jugadores j WHERE j.vecesTercero = :vecesTercero")})
public class Jugadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdJugador")
    private Integer idJugador;
    @Basic(optional = false)
    @Column(name = "NameJugador")
    private String nameJugador;
    @Column(name = "VecesPrimero")
    private Integer vecesPrimero;
    @Column(name = "VecesSegundo")
    private Integer vecesSegundo;
    @Column(name = "VecesTercero")
    private Integer vecesTercero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugadores")
    private List<Podio> podioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJugador")
    private List<Carros> carrosList;

    public Jugadores() {
    }

    public Jugadores(Integer idJugador) {
        this.idJugador = idJugador;
    }

    public Jugadores(Integer idJugador, String nameJugador) {
        this.idJugador = idJugador;
        this.nameJugador = nameJugador;
    }

    public Integer getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Integer idJugador) {
        this.idJugador = idJugador;
    }

    public String getNameJugador() {
        return nameJugador;
    }

    public void setNameJugador(String nameJugador) {
        this.nameJugador = nameJugador;
    }

    public Integer getVecesPrimero() {
        return vecesPrimero;
    }

    public void setVecesPrimero(Integer vecesPrimero) {
        this.vecesPrimero = vecesPrimero;
    }

    public Integer getVecesSegundo() {
        return vecesSegundo;
    }

    public void setVecesSegundo(Integer vecesSegundo) {
        this.vecesSegundo = vecesSegundo;
    }

    public Integer getVecesTercero() {
        return vecesTercero;
    }

    public void setVecesTercero(Integer vecesTercero) {
        this.vecesTercero = vecesTercero;
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
        hash += (idJugador != null ? idJugador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jugadores)) {
            return false;
        }
        Jugadores other = (Jugadores) object;
        if ((this.idJugador == null && other.idJugador != null) || (this.idJugador != null && !this.idJugador.equals(other.idJugador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controladores.Jugadores[ idJugador=" + idJugador + " ]";
    }
    
}
