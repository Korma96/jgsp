package com.mjvs.jgsp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class JgspApplication {
	private static final Logger logger = LogManager.getLogger(JgspApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JgspApplication.class, args);
		logger.debug("Debugging log");
		logger.info("Info log");
		logger.warn("Hey, This is a warning!");
		logger.error("Oops! We have an Error. OK");
		logger.fatal("Damn! Fatal error. Please fix me.");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		int[] gradskeLinije = {1, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 21, 68, 69, 71};
		int[] prigradskeLinije = {22, 23, 24, 30, 35, /*41,*/ 42, 43, 51, 52, 53, 54, 55, 56, 64, 72, 73, 74, 76,};
		int[] medjumesneLinije = {31, /*32,*/ 33, 60, 61, 62, 77, 78, 79, 80, 81, 84, 86};

		String bazniUrlZaStanice = "http://www.gspns.co.rs/mreza-get-stajalista-tacke";
		String staniceDirektorijumRelativnaPutanja = "." + File.separator + "src" + File.separator + "main"
														+ File.separator + "resources" + File.separator + "stanice";

		napraviDirektorijumAkoNePostoji(staniceDirektorijumRelativnaPutanja);

		dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, "gradske_linije", gradskeLinije);
		dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, "prigradske_linije", prigradskeLinije);
		dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, "medjumesne_linije", medjumesneLinije);

		System.out.println("***** Podaci o stanicama svih linija su dobavljeni i smesteni u fajlove. *****");
	}

	private static void napraviDirektorijumAkoNePostoji(String putanjaStr) {
		Path putanja = Paths.get(putanjaStr);

		if(Files.notExists(putanja)) {
			try {
				Files.createDirectories(putanja);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void dobaviStaniceIUpisiUFajl(String bazniUrlZaStanice, String staniceDirektorijumRelativnaPutanja, String vrstaLinija, int[] linije) {
		BufferedReader in = null;
		PrintWriter out = null;

		URL url;
		URLConnection connection;

		String procitanaLinija;
		String[] stanice;

		String direktorijumVrstaLinijaPutanja = staniceDirektorijumRelativnaPutanja + File.separator + vrstaLinija;
		napraviDirektorijumAkoNePostoji(direktorijumVrstaLinijaPutanja);

		for (int linija:linije) {
			try {
				url = new URL(bazniUrlZaStanice + "?linija=" + linija);
				connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);

				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				out = new PrintWriter(new FileWriter(direktorijumVrstaLinijaPutanja+ File.separator
						+ "stanice_linije_" + linija + ".txt"));

				while ((procitanaLinija = in.readLine()) != null) {
					procitanaLinija = procitanaLinija.trim();

					if(procitanaLinija.equals("")) continue;

					procitanaLinija = procitanaLinija.replace("[\"", "");
					procitanaLinija = procitanaLinija.replace("\"]", "");
					procitanaLinija = procitanaLinija.replace("\",\"", "\n");

					System.out.println(procitanaLinija);
					out.println(procitanaLinija);
				}
			}
			catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				System.out.println(e.getMessage());
			}
			finally {
				if(in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if(out != null) out.close();
			}

		}

		System.out.println("\nCrunchify REST Service Invoked Successfully..");
	}
}
