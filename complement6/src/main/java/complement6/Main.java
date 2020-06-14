package complement6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
/**
*
* @author  Miguel
* @version 1.0
*/
public class Main {
	private static String gitUrl = "https://github.com/vbasto-iscte/ESII1920.git";
	private static String repoPath = "covid";
	private static String fileToSearch = "covid19spreading.rdf";
	private static Git git;
	private static Repository repository;
	private static List<Ref> tags=new ArrayList<>();
	private static List<String> tags_name=new ArrayList<>();
	private static ArrayList<covidStats> tagsStats1=new ArrayList<>();
	private static ArrayList<covidStats> tagsStats2=new ArrayList<>();

	   /**
	   * Retira as ultimas 2 tags.
	   */
	public static void checkTags() throws GitAPIException {
		tags = git.tagList().call();

		Ref r;
		for (int i = 2; i !=0 ;i--) {
			r = tags.get(tags.size()-i);
			git.checkout().setName(r.getName()).call();
			repository = git.getRepository();
			try {
				accessFile(r);
			} catch (RevisionSyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	   /**
	   * Acede ao covid19spreading.rdf de uma tag.
	   * @param tag Tag para aceder ao ficheiro.
	   */
	public static void accessFile(Ref tag) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException {
		tags_name.add(tag.getName().split("/")[2]);

		ObjectId commitId = repository.resolve(Constants.HEAD);
		try (RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(commitId);
			RevTree tree = commit.getTree();
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create(fileToSearch));
				if (!treeWalk.next()) {
					throw new IllegalStateException("Did not find file");
				}

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);

				try {
					String path = repoPath + File.separator + tag.getName().split("/")[2] + ".rdf";
					File f = new File(path);
					f.getParentFile().mkdir();
					f.createNewFile();
					loader.copyTo(new FileOutputStream(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			revWalk.dispose();
		}

	}
	  /**
	   * Obtem o repositorio git.
	   */
	public static void getGit() {

		try {
			git=Git.cloneRepository()
					.setURI(gitUrl)
					.setDirectory(new File(repoPath))
					.call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repository=git.getRepository();

		try {
			checkTags();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	  /**
	   * Elimina um diretorio.
	   * @param file diretorio a eliminar
	   */
	public static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (! Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	  /**
	   * Le um ficheiro covid19spreading.rdf e guarda os resultados numa lista
	   * @param tag tag do ficheiro a ler
	   * @param tagStats  Mete os valores nesta lista
	   */
	public static void readfile(String tag,ArrayList<covidStats> tagsStats) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(repoPath + "/"+tag+".rdf"));
			String line = reader.readLine();
			covidStats stats;
			while (line != null) {
				if(line.contains("NamedIndividual rdf")){
					String regiao=line.split("ontology-3;")[1].split("\">")[0];
					stats=new covidStats(regiao);
					tagsStats.add(stats);
				}
				if(line.contains("Internamentos rdf")) {
					String Internamentos=line.split(">")[1].split("<")[0];
					tagsStats.get(tagsStats.size()-1).setInternamentos(Internamentos);;
				}
				if(line.contains("Infecoes rdf")) {
					String Infecoes=line.split(">")[1].split("<")[0];
					tagsStats.get(tagsStats.size()-1).setInfecoes(Infecoes);
				}
				if(line.contains("Testes rdf")) {
					String Testes=line.split(">")[1].split("<")[0];
					tagsStats.get(tagsStats.size()-1).setTestes(Testes);;
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	  /**
	   * Cria 3 tabelas sendo a primeira a tag mais antiga, a segunda 
	   * a tag mais recente e por ultimo uma tabela com a legenda das cores utilizadas.
	   * 
	   */
	public static void buildTables() {
//		covidStats stats;
//		stats=new covidStats("teste");
//		tagsStats2.add(stats);
		HTMLTableBuilder htmlBuilder = new HTMLTableBuilder(tags_name.get(0), true, 4, 4);
		htmlBuilder.addTableHeader("Regiao", "Internados", "Infecoes","Testes");
		for(covidStats c: tagsStats1) {
			boolean del=true;
			for(covidStats c2: tagsStats2) {
				if(c.getRegiao().equals(c2.getRegiao())) {
					del=false;
				}
			}
			if(del) {
				int[] i=new int[4];
				for(int d=0;d<4;d++) {
					i[d]=1;			
				}
				htmlBuilder.addSpecialRowValues(i, c.getRegiao(),c.getInternamentos(), c.getInfecoes(),c.getTestes());
			}else {
				htmlBuilder.addRowValues(c.getRegiao(),c.getInternamentos(), c.getInfecoes(),c.getTestes());
			}
		}
		
		String table = htmlBuilder.build();
		HTMLTableBuilder htmlBuilder2 = new HTMLTableBuilder(tags_name.get(1), true, 4, 4);
		htmlBuilder2.addTableHeader("Regiao", "Internados", "Infecoes","Testes");
		for(covidStats c: tagsStats2) {
			boolean New=true;
			int[] i=new int[4];
			
			for(covidStats c2: tagsStats1) {
				if(c.getRegiao().equals(c2.getRegiao())) {
					New=false;
					if(!c.getInternamentos().equals(c2.getInternamentos())){
						i[1]=2;
					}
					if(!c.getInfecoes().equals(c2.getInfecoes())){
						i[2]=2;
					}
					if(!c.getTestes().equals(c2.getTestes())){
						i[3]=2;
					}
						
				}
			}
			
			if(New) {
				for(int d=0;d<4;d++) {
					i[d]=3;			
				}
				htmlBuilder2.addSpecialRowValues(i, c.getRegiao(),c.getInternamentos(), c.getInfecoes(),c.getTestes());
			}else {
				htmlBuilder2.addSpecialRowValues(i, c.getRegiao(),c.getInternamentos(), c.getInfecoes(),c.getTestes());
			}
		}
		String table2 = htmlBuilder2.build();
		System.out.println(table.toString());
		System.out.println(table2.toString());
		
		int c[]=new int[3];
		c[0]=1;
		HTMLTableBuilder htmlBuilder3 = new HTMLTableBuilder("", true, 2, 2);
		htmlBuilder3.addTableHeader("cor", "Legenda");
		htmlBuilder3.addSpecialRowValues(c,"", "valores eliminados");
		c[0]=2;
		htmlBuilder3.addSpecialRowValues(c,"", "valores alterados");
		c[0]=3;
		htmlBuilder3.addSpecialRowValues(c,"", "valores adicionados");
		String table3 = htmlBuilder3.build();
		System.out.println(table3.toString());
	}
	
	  /**
	   * Corre o programa para ir buscar o repositorio git
	   * e criar as tabelas do covid19spreading
	   */
	public static void run() {
		File files=new File(repoPath);
		if (files.list()!=null) {
			if(files.list().length != 0){
				deleteDir(files);
			}
		}
		getGit();
		readfile(tags_name.get(0),tagsStats1);
		readfile(tags_name.get(1),tagsStats2);
		
		buildTables();
	}

	  /**
	   * Escreve o codigo html
	   * @param args Nao e utilizado
	   */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 System.out.println(cgi_lib.Header());
		run();
	}           

}
