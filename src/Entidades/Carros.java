package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "Carros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carros.findAll", query = "SELECT c FROM Carros c")
    , @NamedQuery(name = "Carros.findByIdCarro", query = "SELECT c FROM Carros c WHERE c.idCarro = :idCarro")
    , @NamedQuery(name = "Carros.findByNameCarro", query = "SELECT c FROM Carros c WHERE c.nameCarro = :nameCarro")})
public class Carros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdCarro")
    private Integer idCarro;
    @Basic(optional = false)
    @Column(name = "NameCarro")
    private String nameCarro;
    @JoinColumn(name = "IdPista", referencedColumnName = "IdPista")
    @ManyToOne(optional = false)
    private Pistas idPista;
    @JoinColumn(name = "IdJugador", referencedColumnName = "IdJugador")
    @ManyToOne(optional = false)
    private Jugadores idJugador;

    public Carros() {
    }

    public Carros(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public Carros(Integer idCarro, String nameCarro) {
        this.idCarro = idCarro;
        this.nameCarro = nameCarro;
    }

    public Integer getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public String getNameCarro() {
        return nameCarro;
    }

    public void setNameCarro(String nameCarro) {
        this.nameCarro = nameCarro;
    }

    public Pistas getIdPista() {
        return idPista;
    }

    public void setIdPista(Pistas idPista) {
        this.idPista = idPista;
    }

    public Jugadores getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Jugadores idJugador) {
        this.idJugador = idJugador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarro != null ? idCarro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carros)) {
            return false;
        }
        Carros other = (Carros) object;
        if ((this.idCarro == null && other.idCarro != null) || (this.idCarro != null && !this.idCarro.equals(other.idCarro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Controladores.Carros[ idCarro=" + idCarro + " ]";
    }
    
}
