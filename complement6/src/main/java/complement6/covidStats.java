package complement6;

/**
*
* @author  Miguel
* @version 1.0
*/
public class covidStats {
	private String regiao;
	private String infecoes;
	private String internamentos;
	private String testes;

	public covidStats(String regiao) {
		this.regiao=regiao;
		infecoes="-";
		internamentos="-";
		testes="-";
	}

	/**
	 * Obtem a regiao
	 */	
	public String getRegiao() {
		return regiao;
	}
	/**
	 * This method is used to add two integers. This is
	 * a the simplest form of a class method, just to
	 * show the usage of various javadoc Tags.
	 * @param numA This is the first paramter to addNum method
	 * @param numB  This is the second parameter to addNum method
	 * @return int This returns sum of numA and numB.
	 */
	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	/**
	 * Obtem os dados das infecoes
	 */	
	public String getInfecoes() {
		return infecoes;
	}
	
	/**
	 * Muda os dados das infecoes
	 * @param infecoes valor a mudar
	 */
	public void setInfecoes(String infecoes) {
		this.infecoes = infecoes;
	}

	/**
	 * Obtem os dados dos internamentos
	 */	
	public String getInternamentos() {
		return internamentos;
	}

	/**
	 * Muda os dados dos internamentos
	 * @param internamentos valor a mudar
	 */
	public void setInternamentos(String internamentos) {
		this.internamentos = internamentos;
	}

	/**
	 * Obtem os dados dos testes
	 */	
	public String getTestes() {
		return testes;
	}
	/**
	 * Muda os dados dos testes
	 * @param testes valor a mudar
	 */
	public void setTestes(String testes) {
		this.testes = testes;
	}

	@Override
	public String toString() {
		return "covidStats [regiao=" + regiao + ", infecoes=" + infecoes + ", internamentos=" + internamentos
				+ ", testes=" + testes + "]";
	}

}
