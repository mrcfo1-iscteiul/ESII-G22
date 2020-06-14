package complement6;


public class HTMLTableBuilder {
	private int columns;
	private StringBuilder table = new StringBuilder();
	public String HTML_START = "<html>\n";
	public String HTML_END = "</html>";
	public String TABLE_START_BORDER = "<table border=\"1\">\n";
	public String TABLE_START = "<table>\n";
	public String TABLE_END = "</table>\n";
	public String HEADER_START = "<th>";
	public String HEADER_END = "</th>\n";
	public String ROW_START = "<tr>\n";
	public String ROW_END = "</tr>\n";
	public String COLUMN_START = "<td>";
	public String COLUMN_END = "</td>\n";
	public int i=0;

	/**
	 * @param header
	 * @param border
	 * @param rows
	 * @param columns
	 */
	public HTMLTableBuilder(String header, boolean border, int rows, int columns) {
		this.columns = columns;
		if (header != null) {
			table.append("<h3>");
			table.append(header);
			table.append("</h3>");
		}
		table.append(border ? TABLE_START_BORDER : TABLE_START);
		table.append(TABLE_END);

	}


	/**
	 * Adciona os titulos das colunas
	 * @param values
	 */
	public void addTableHeader(String... values) {
		if (values.length != columns) {
			System.out.println("Error column lenth");
		} else {
			int lastIndex = table.lastIndexOf(TABLE_END);
			if (lastIndex > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(ROW_START);
				for (String value : values) {
					sb.append(HEADER_START);
					sb.append(value);
					sb.append(HEADER_END);
				}
				sb.append(ROW_END);
				table.insert(lastIndex, sb.toString());
			}
		}
	}


	/**
	 * Adiciona uma linha a tabela
	 * @param values
	 */
	public void addRowValues(String... values) {
		if (values.length != columns) {
			System.out.println("Error column lenth");
		} else {
			int lastIndex = table.lastIndexOf(ROW_END);
			if (lastIndex > 0) {
				int index = lastIndex + ROW_END.length();
				StringBuilder sb = new StringBuilder();
				sb.append(ROW_START);
				for (String value : values) {
					sb.append(COLUMN_START);
					sb.append(value);
					sb.append(COLUMN_END);
				}
				sb.append(ROW_END);
				table.insert(index, sb.toString());
			}
		}
	}
	
	/**
	 * Adiciona um linha nuam tabela com cores
	 * @param values valores da linha
	 */
	public void addSpecialRowValues(int[] c,String... values) {
		int i=0;
		if (values.length != columns) {
			System.out.println("Error column lenth");
		} else {
			int lastIndex = table.lastIndexOf(ROW_END);
			if (lastIndex > 0) {
				int index = lastIndex + ROW_END.length();
				StringBuilder sb = new StringBuilder();
				sb.append(ROW_START);
				for (String value : values) {
					if(c[i]==0) {
						sb.append(COLUMN_START);
						sb.append(value);
						sb.append(COLUMN_END);
					}
					if(c[i]==1) {
						sb.append("<td style=\"background-color:Red\">");
						sb.append(value);
						sb.append(COLUMN_END);
					}
					if(c[i]==2) {
						sb.append("<td style=\"background-color:yellow\">");
						sb.append(value);
						sb.append(COLUMN_END);
					}
					if(c[i]==3) {
						sb.append("<td style=\"background-color:lightgreen\">");
						sb.append(value);
						sb.append(COLUMN_END);
					}
					i++;
				}
				sb.append(ROW_END);
				table.insert(index, sb.toString());
			}
		}
	}


	/**
	 * @return retorna uma string da tabela em html
	 */
	public String build() {
		return table.toString();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		int c[]=new int[3];
//		c[0]=2;
//		c[1]=0;
//		HTMLTableBuilder htmlBuilder = new HTMLTableBuilder("teste", true, 2, 3);
//		htmlBuilder.addTableHeader("1H", "2H", "3H");
//		htmlBuilder.addRowValues("1", "2", "3");
//		htmlBuilder.addRowValues("4", "5", "6");
//		htmlBuilder.addRowValues("9", "8", "7");
//		String table = htmlBuilder.build();
//		System.out.println(table.toString());
//
//		HTMLTableBuilder htmlBuilder2 = new HTMLTableBuilder("teste2", true, 2, 3);
//		htmlBuilder2.addTableHeader("1H", "2H", "3H");
//		htmlBuilder2.addRowValues("1", "2", "3");
//		htmlBuilder2.addRowValues("4", "5", "6");
//		htmlBuilder2.addRowValues("9", "8", "7");
//		htmlBuilder2.addSpecialRowValues(c,"9", "8", "7");
//		String table2 = htmlBuilder2.build();
//		System.out.println(table2.toString());
//		
//		
//		HTMLTableBuilder htmlBuilder3 = new HTMLTableBuilder("", true, 2, 2);
//		htmlBuilder3.addTableHeader("cor", "Legenda");
//		htmlBuilder3.addSpecialRowValues(c,"", "valores alterados");
//		c[0]=3;
//		htmlBuilder3.addSpecialRowValues(c,"", "valores adicionados");
//		String table3 = htmlBuilder3.build();
//		System.out.println(table3.toString());
	}
}
