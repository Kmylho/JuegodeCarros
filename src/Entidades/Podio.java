package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kmylho
 */
@Entity
@Table(name = "Podio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Podio.findAll", query = "SELECT p FROM Podio p")
    , @NamedQuery(name = "Podio.findByIdCarrera", query = "SELECT p FROM Podio p WHERE p.idCarrera = :idCarrera")})
public class Podio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdCarrera")
    private Integer idCarrera;
    @JoinColumns({
        @JoinColumn(name = "IdTercero", referencedColumnName = "IdJugador")
        , @JoinColumn(name = "IdSegundo", referencedColumnName = "IdJugador")
        , @JoinColumn(name = "IdPrimero", referencedColumnName = "IdJugador")})
    @ManyToOne(optional = false)
    private Jugadores jugadores;
    @JoinColumn(name = "IdPista", referencedColumnName = "IdPista")
    @ManyToOne(optional = false)
    private Pistas idPista;

    public Podio() {
    }

    public Podio(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public Jugadores getJugadores() {
        return jugadores;
    }

    public void setJugadores(Jugadores jugadores) {
        this.jugadores = jugadores;
    }

    public Pistas getIdPista() {
        return idPista;
    }

    public void setIdPista(Pistas idPista) {
        this.idPista = idPista;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarrera != null ? idCarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Podio)) {
            return false;
        }
        Podio other = (Podio) object;
        if ((this.idCarrera == null && other.idCarrera != null) || (this.idCarrera != null && !this.idCarrera.equals(other.idCarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controladores.Podio[ idCarrera=" + idCarrera + " ]";
    }
    
}
