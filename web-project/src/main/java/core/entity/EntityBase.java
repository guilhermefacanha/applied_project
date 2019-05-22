package core.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.google.gson.annotations.Expose;

import core.util.UtilFunctions;
import lombok.Data;

/**
 * Classe Base para Entidade de relacionamento com banco de dados
 * @author gfsolucoesti
 */
@Data
@MappedSuperclass
public abstract class EntityBase implements Serializable
{
	private static final long serialVersionUID = 5641638789450652037L;

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@Version
	private long version;
	
	@Transient
	private String idBase64;
	
	/**
	 * Metodo que transforma o id em base64
	 * @return String
	 */
	public String getIdBase()
	{
		return UtilFunctions.gerarBase64(String.valueOf(id));
	}
	
	/**
	 * Metodo que verificar string base64 e converte para long populando id da entidade
	 */
	public void verificarIdBase(){
		try {
			this.id = Long.parseLong(UtilFunctions.lerBase64(idBase64));
		} catch (Exception e) {
		}
	}

}
