package colladaLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * 
 * 
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 *
 *
 *
 * 
 * Represents a "file" inside a Jar File. Used for accessing resources (models, textures), as they
 * are all inside a jar file when exported.
 * 
 * @author Karl
 *
 */
public class MyFile {

	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;
	private File file;

	/**
	 * Konstruktør
	 * @param path Filstien
	 */
	public MyFile(String path) {
		this.path = FILE_SEPARATOR + path;
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	/**
	 * Konstruktør 
	 * @param paths Array med filstier
	 */
	public MyFile(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	/**
	 * Konstruktør
	 * @param file Original-fil
	 * @param subFile Filsti til underfil
	 */
	public MyFile(MyFile file, String subFile) {
		this.path = file.path + FILE_SEPARATOR + subFile;
		this.name = subFile;
	}

	/**
	 * Konstruktør
	 * @param file Original-filen
	 * @param subFiles Array med filstier til underfiler
	 */
	public MyFile(MyFile file, String... subFiles) {
		this.path = file.path;
		for (String part : subFiles) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}
	
	
/**
 * Konstruktør
 * @param modelFile En fil. Se {@link File}
 */
	public MyFile(File modelFile)
	{
		this.file = modelFile;
	}

	/**
	 * Henter filstien til filen
	 * @return Filstien som String
	 */
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	/**
	 * Henter en {@link InputStream} for å lese filen som er lagret i klassen
	 * @return Stream av filen som en {@link FileInputStream}
	 * @throws FileNotFoundException Filen ble ikke funnet
	 */
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	/**
	 * Henter en leser for en stream
	 * @return leser som en {@link BufferedReader}
	 * @throws Exception
	 */
	public BufferedReader getReader() throws Exception {
		try {
			
			InputStreamReader isr = new InputStreamReader(getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (Exception e) {
			System.err.println("Couldn't get reader for " + file.getPath());
			throw e;
		}
	}

	/**
	 * Henter navnet på filen
	 * @return Navnet på filen som en String
	 */
	public String getName() {
		return name;
	}

}
