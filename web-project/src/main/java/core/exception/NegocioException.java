package core.exception;

/**
 * Classe de Excecao Customizada da aplicacao
 * @author gfsolucoesti
 */
public class NegocioException extends Exception {

	public NegocioException(Exception e) {
		super(e);
	}

	public NegocioException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 6446985914692633765L;
	

}
