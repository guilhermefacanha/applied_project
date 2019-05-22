package core.config;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import core.util.UtilFunctions;

/**
 * Classe responsavel por guardar os valores de configuracao da aplicacao
 * @author gfsolucoesti
 */
public class AppConfig
{
	private static String caminhoRepositorio = "";
	private static String sufixo_url = "";

	private static Properties properties = new Properties();

	/**
	 * Metodo que le o arquivos sistema.properties e popula suas variaveis
	 */
	public static void carregarPropriedades()
	{
		try
		{
			Locale.setDefault(new Locale("pt", "BR"));

			InputStream in = AppConfig.class.getResourceAsStream("/sistema.properties");
			properties.load(in);

			caminhoRepositorio = properties.getProperty("caminhoRepositorio");
			sufixo_url = properties.getProperty("sufixo_url");

		}
		catch (Exception e)
		{
			System.out.println("ERRO ao carregar propriedades do sistema: " + e.getMessage());
		}
	}
	
	public static String getDataSourceJndi() {
		return "java://comp//env//jdbc//sisgia";
	}

	/**
	 * Metodo retorna o caminho do repositorio storage dos arquivos de upload do sistema
	 * @return String
	 */
	public static String getCaminhoRepositorio()
	{
		return AppConfig.caminhoRepositorio;
	}

	/**
	 * Metodo retorna o caminho do repositorio storage dos arquivos de upload do sistema relacionados com entidade {@link Documento}
	 * @param idDocumento 
	 * @return String
	 */
	public static String getCaminhoRepositorioDocumentos(long idDocumento)
	{
		return AppConfig.caminhoRepositorio+File.separator+"documentos"+File.separator+idDocumento+File.separator;
	}

	/**
	 * Metodo retorna o caminho do repositorio storage dos arquivos de upload do sistema relacionados com entidade {@link Documento}
	 * @param idDocumento
	 * @param idClausula
	 * @return
	 */
	public static String getCaminhoRepositorioClausula(long idDocumento,long idClausula)
	{
		return AppConfig.caminhoRepositorio+File.separator+"documentos"+File.separator+idDocumento+File.separator+"clausula_"+idClausula+File.separator;
	}

	/**
	 * Metodo retorna o caminho do repositorio storage dos arquivos de upload do sistema relacionados com entidade {@link DocumentoShape}
	 * @param idDocumento
	 * @return
	 */
	public static String getCaminhoRepositorioDocumentoAnexo(long idDocumento)
	{
		return AppConfig.caminhoRepositorio+File.separator+"documentos"+File.separator+idDocumento+File.separator+"anexo"+File.separator;
	}
	
	/**
	 * Metodo retorna o caminho do repositorio storage dos arquivos de upload do sistema relacionados com entidade {@link DocumentoShape}
	 * @param idDocumento
	 * @return
	 */
	public static String getCaminhoRepositorioDocumentoShape(long idDocumento)
	{
		return AppConfig.caminhoRepositorio+File.separator+"documentos"+File.separator+idDocumento+File.separator+"shape"+File.separator;
	}
	
	/**
	 * Metodo que retorna caminho do repositorio temporario do sistema
	 * @return String
	 */
	public static String getCaminhoRepositorioTmp() {
		return System.getProperty("java.io.tmpdir")+File.separator+"sisgia";
	}

	
	/**
	 * Metodo retorna o sufixo das paginas web
	 * @return String
	 */
	public static String getSufixo_url()
	{
		return sufixo_url;
	}

	/**
	 * Metodo retorna o esquema de shapes do banco de dados
	 * @return String
	 */
	public static String getShapeSchema() {
		return "shape.";
	}

	public static String getServerLogPath() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")){
			return "C:\\Users\\limafacanhag\\Desktop\\workspace\\eclipse_workspace\\spcl_topics\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\logs\\localhost_access_log."+UtilFunctions.getStringDeData(new Date(), "yyyy-MM-dd")+".txt";
		}
		else {
			return "/tomcat/logs/catalina.out"; 
		}
	}

}
