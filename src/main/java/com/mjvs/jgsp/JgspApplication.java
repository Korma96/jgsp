package com.mjvs.jgsp;

import com.mjvs.jgsp.model.Stop;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

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

    //@EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        int[] gradskeLinijeBrojevi = {1,2,232,233,3,4,5,6,241,242,7,8,9,10,69,70,11,12,234,235,13,14,15,16,17,18,19,20,21,22,
                23,24,239,240,25,26,27,28,29,30,31,33,34,81,82,83,84,85,86,243,244};
        String[][] gradskeLinijeOznake = {{"1A","SENTANDREJSKI PUT - ZMAJEVAČKI PUT"},{"1B","ŠTRAND - OKRETNICA"},
                {"1ZA","PROLETERSKA - OKRETNICA"},{"1ZB","ŠTRAND - OKRETNICA"},{"2A","USPENSKA - ŠAFARIKOVA"},
                {"2B","NOVO NASELJE - BISTRICA - OKRETNICA"},{"3A","PETROVARADIN - OKRETNICA"},{"3B","JANKA ČMELIKA - OBLAČIĆA RADA"},
                {"3AA","ŽELEZNIČKA STANICA - DOLASCI"},{"3AB","POBEDA"},{"4A","NARODNOG FRONTA - IVE ANDRIĆA"},{"4B","ŽELEZNIČKA STANICA"},
                {"5A","TEMERINSKI PUT - NADVOŽNJAK - OKRETNICA"},{"5B","AVIJATIČARSKA - KASARNA - OKRETNICA"},{"5NA","ŽELEZNIČKA STANICA"},
                {"5NB","TEMERINSKI PUT - NADVOŽNJAK - OKRETNICA"},{"6A","KOSOVSKA"},{"6B","SMEDEREVSKA - BANJALUČKA"},
                {"6AA","SMEDEREVSKA - BANJALUČKA"},{"6AB","VETERNIČKA - FEŠTER OSNOVNA ŠKOLA"},{"7A","BUL. J.DUČIĆA - IGRALIŠTE"},
                {"7B","BUL. KNEZA MILOŠA - BUL. J. DUČIĆA"},{"8A","NOVO NASELJE - BISTRICA - OKRETNICA"},{"8B","ŠTRAND - OKRETNICA"},
                {"9A","NOVO NASELJE - BISTRICA - OKRETNICA"},{"9B","PUT NOVI SAD - RUMA - ALIBEGOVAC"},{"10A","USPENSKA - ŠAFARIKOVA"},
                {"10B","PUT N. PART. ODREDA - ALBUS"},{"11A","BUL. JAŠE TOMIĆA - ŽELEZNIČKA STANICA"},{"11B","BUL. JAŠE TOMIĆA - BUL. OSLOBOĐENJA"},
                {"12A","USPENSKA - ŠAFARIKOVA"},{"12B","SENTELEKI KORNELA - O.Š. JOŽEF ATILA"},{"13A","JANKA ČMELIKA - OBLAČIĆA RADA"},
                {"13B","UNIVERZITET - DR SIME MILOŠEVIĆA"},{"14A","USPENSKA - ŠAFARIKOVA"},{"14B","SAJLOVO - KRAJNJA"},
                {"15A","USPENSKA - ŠAFARIKOVA"},{"15B","KOTEKSPRODUKT - OKRETNICA"},{"16A","ŽELEZNIČKA STANICA TERMINAL"},
                {"16B","BAJČI ŽILINSKOG - DANUBIUS"},{"17A","BIG ŠOPING CENTAR"},{"21A","BUL. JAŠE TOMIĆA - BUL. OSLOBOĐENJA"},
                {"21B","ŠANGAJ - OKRETNICA"},{"68A","ŽELEZNIČKA STANICA"},{"68B","SR. KAMENICA - VOJINOVO - OKRETNICA"},{"69A","ŽELEZNIČKA STANICA"},
                {"69B","KAMENICA - ČARDAK - OKRETNICA"},{"71A","ŽELEZNIČKA STANICA"},{"71B","SR. KAMENICA - BOCKE - OKTERNICA"},
                {"18A","NOVO NASELJE - BISTRICA - OKRETNICA"},{"18B","NOVO NASELJE - BISTRICA - OKRETNICA"}};
        HashMap<Integer, String[]> gradskeLinije = new HashMap<Integer, String[]>();
        for(int i = 0; i < gradskeLinijeBrojevi.length; i++) gradskeLinije.put(gradskeLinijeBrojevi[i], gradskeLinijeOznake[i]);

        int[] prigradskeLinijeBrojevi = {35,36,37,38,39,40,42,43,50,51,236,238,52,53,54,55,56,57,58,59,60,61,62,63,65,66,67,
                68,79,80,87,88,89,90,92,93,95,96,251,252};
        String[][] prigradskeLinijeOznake = {{"22A","ŽELEZNIČKA STANICA TERMINAL"},{"22B","KAĆ - OKRETNICA"},{"23A","ŽELEZNIČKA STANICA TERMINAL"},
                {"23B","BUDISAVA - OKRETNICA"},{"24A","ŽELEZNIČKA STANICA TERMINAL"},{"24B","KOVILJ - OKRETNICA"},
                {"30A","BUL. JAŠE TOMIĆA - BUL. OSLOBOĐENJA"},{"30B","PEJIĆEVI SALAŠI - OKRETNICA KOD MZ"},
                {"35A","BUL. JAŠE TOMIĆA - BUL. OSLOBOĐENJA"},{"35B","ČENEJ - VUKA KARADŽIĆA - KRAJNJA"},
                {"35ČLA","BUL. JAŠE TOMIĆA - BUL. OSLOBOĐENJA"},{"35ČLB","ČENEJ - LIS"},
                {"41A","ŽELEZNIČKA STANICA TERMINAL"},{"41B","RUMENKA - OKRETNICA"},{"42A","ŽELEZNIČKA STANICA TERMINAL"},{"42B","KISAČ - OKRETNICA"},
                {"43A","ŽELEZNIČKA STANICA TERMINAL"},{"43B","STEPANOVIĆEVO - OKRETNICA"},{"52A","ŽELEZNIČKA STANICA TERMINAL"},
                {"52B","VETERNIK - ŠKOLA"},{"53A","ŽELEZNIČKA STANICA TERMINAL"},{"53B","FUTOG - AROMA - OKRETNICA"},
                {"54A","ŽELEZNIČKA STANICA TERMINAL"},{"54B","FUTOG - MILAN VIDAK - OKRETNICA"},{"55A","ŽELEZNIČKA STANICA TERMINAL"},
                {"55B","FUTOG - AROMA - OKRETNICA"},{"56A","ŽELEZNIČKA STANICA TERMINAL"},{"56B","BEGEČ - OKRETNICA"},
                {"64A","ŽELEZNIČKA STANICA TERMINAL"},{"64B","BUKOVAC - OKRETNICA"},{"72A","ŽELEZNIČKA STANICA"},
                {"72B","PARAGOVO - VENAC - OKRETNICA"},{"73A","ŽELEZNIČKA STANICA"},{"73B","POPOVICA - OKRETNICA - MOŠINA VILA"},
                {"74A","ŽELEZNIČKA STANICA"},{"74B","POPOVICA - FRUŠKOGORSKI PUT - OKRETNICA"},{"76A","BUL. OSLOBOĐENJA - KRALJA PETRA I"},
                {"76B","STARI LEDINCI - OKRETNICA"},{"51A","ŽELEZNIČKA STANICA TERMINAL"},{"51B","ŽELEZNIČKA STANICA TERMINAL"}};
        HashMap<Integer, String[]> prigradskeLinije = new HashMap<Integer, String[]>();
        for(int i = 0; i < prigradskeLinijeBrojevi.length; i++) prigradskeLinije.put(prigradskeLinijeBrojevi[i], prigradskeLinijeOznake[i]);

        int[] medjumesneLinijeBrojevi = {44,45,46,47,48,49,71,72,74,75,77,78,98,99,100,101,102,103,106,107,109,110,111,112,
                253,254};
        String[][] medjumesneLinijeOznake = {{"31A","MAS - POLAZNI PERON BR. 13"},{"31B","BAČKI JARAK - OKRETNICA"},
                {"32A","MAS - POLAZNI PERON BR. 13"},{"32B","TEMERIN IX - ALMAŠKA UL. - OKRETNICA"},{"33A","MAS - POLAZNI PERON BR. 13"},
                {"33B","GOSPOÐINCI IV - CENTAR"},{"60A","MAS - POLAZNI PERON BR. 13"},{"60B","SR. KARLOVCI - BELILO II"},
                {"61A","MAS - POLAZNI PERON BR. 13"},{"61B","VINOGRADARSKA - MITR. STRATIMIROVIĆA OKRETNICA"},{"62A","MAS - POLAZNI PERON BR. 13"},
                {"62B","DUDARA - OKRETNICA"},{"77A","MAS - POLAZNI PERON BR. 14"},{"77B","ST. RAKOVAC VIII - OKRETNICA"},
                {"78A","MAS - POLAZNI PERON BR. 14"},{"78B","BEOČIN SELO IX - OKRETNICA"},{"79A","MAS - POLAZNI PERON BR. 14"},
                {"79B","ČEREVIĆ V - SPOMENIK"},{"80A","MAS - POLAZNI PERON BR. 14"},{"80B","BEOČIN V - AUTOB. STANICA"},
                {"81A","MAS - POLAZNI PERON BR. 14"},{"81B","BANOŠTOR V - OKRETNICA"},{"84A","MAS - POLAZNI PERON BR. 14"},{"84B","LUG III-CENTAR"},
                {"86A","SR. KAMENICA - CENTAR"},{"86B","NOVI SAD AS-Irig-Vrdnik(hotel\"Termal\")"}};
        HashMap<Integer, String[]> medjumesneLinije = new HashMap<Integer, String[]>();
        for(int i = 0; i < medjumesneLinijeBrojevi.length; i++) medjumesneLinije.put(medjumesneLinijeBrojevi[i], medjumesneLinijeOznake[i]);


        String bazniUrlZaStanice = "http://www.gspns.co.rs/mreza-get-stajalista-tacke";
        String staniceDirektorijumRelativnaPutanja = "." + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "stanice";
        String sortiraneStaniceDirektorijumRelativnaPutanja = "." + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "sortirane_stanice";

        napraviDirektorijumAkoNePostoji(staniceDirektorijumRelativnaPutanja);
        napraviDirektorijumAkoNePostoji(sortiraneStaniceDirektorijumRelativnaPutanja);

        dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, sortiraneStaniceDirektorijumRelativnaPutanja, "gradske_linije", gradskeLinije);
        dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, sortiraneStaniceDirektorijumRelativnaPutanja, "prigradske_linije", prigradskeLinije);
        dobaviStaniceIUpisiUFajl(bazniUrlZaStanice, staniceDirektorijumRelativnaPutanja, sortiraneStaniceDirektorijumRelativnaPutanja, "medjumesne_linije", medjumesneLinije);

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

    private static void dobaviStaniceIUpisiUFajl(String bazniUrlZaStanice, String staniceDirektorijumRelativnaPutanja, String sortiraneStaniceDirektorijumRelativnaPutanja, String vrstaLinija, HashMap<Integer, String[]> linije) {
        BufferedReader in = null;
        PrintWriter out = null;
        PrintWriter out2 = null;

        URL url;
        URLConnection connection;

        String procitanaLinija;

        String direktorijumVrstaLinijaPutanja = staniceDirektorijumRelativnaPutanja + File.separator + vrstaLinija;
        napraviDirektorijumAkoNePostoji(direktorijumVrstaLinijaPutanja);

        String direktorijumVrstaLinijaPutanjaSortiraneStanice = sortiraneStaniceDirektorijumRelativnaPutanja + File.separator + vrstaLinija;
        napraviDirektorijumAkoNePostoji(direktorijumVrstaLinijaPutanjaSortiraneStanice);

        int indexOfFirstStop = 6;
        String[] aOrB = {"A", "B"};

        ArrayList<Stop> stops;
        String[] tokens;
        String[] oznakaINaziv;

        for (int linijaBroj : linije.keySet()) {
            try {
                url = new URL(bazniUrlZaStanice + "?linija=" + linijaBroj);
                connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                oznakaINaziv = linije.get(linijaBroj);

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                out = new PrintWriter(new FileWriter(direktorijumVrstaLinijaPutanja+ File.separator
                        + "stanice_linije_" + oznakaINaziv[0] + ".txt"));
                out2 = new PrintWriter(new FileWriter(direktorijumVrstaLinijaPutanjaSortiraneStanice+ File.separator
                        + "sortirane_stanice_linije_" + oznakaINaziv[0] + ".txt"));

                stops = new ArrayList<Stop>();

                while ((procitanaLinija = in.readLine()) != null) {
                    procitanaLinija = procitanaLinija.trim();

                    if(procitanaLinija.equals("")) continue;

                    procitanaLinija = procitanaLinija.replace("[\"", "");
                    procitanaLinija = procitanaLinija.replace("\"]", "");
                    procitanaLinija = procitanaLinija.replace("\",\"", "\n");
                    procitanaLinija = StringEscapeUtils.unescapeJava(procitanaLinija);

                    System.out.println(procitanaLinija);
                    out.println(procitanaLinija);

					String[] tokensLinije = procitanaLinija.split("\n");
					for (int i = 0; i < tokensLinije.length; i++) {
						tokens = tokensLinije[i].split("\\|");
						if(tokens[3].equals(oznakaINaziv[1])) {
						    indexOfFirstStop = i;
                        }
						stops.add(new Stop(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[1]), tokens[3]));
					}
                }

				ArrayList<Stop> sortedStops = getSortedStops(stops, indexOfFirstStop);
				for (Stop stop: sortedStops) {
					System.out.println(stop);
					out2.println(stop);
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

                if(out2 != null) out2.close();
            }

        }

        System.out.println("\nCrunchify REST Service Invoked Successfully..");
        System.out.println("Sortirano*****************************************");
    }

    private static ArrayList<Stop> getSortedStops(ArrayList<Stop> stops, int indexOfFirstStop) {
        ArrayList<Stop> sortedStops = new ArrayList<Stop>();

        Stop currentStop = stops.get(indexOfFirstStop);
        sortedStops.add(currentStop);
        stops.remove(indexOfFirstStop);

        int indexOfNearest = -1;
        int minDistance;
        int distance;

        while(stops.size() > 1) {
            minDistance = Integer.MAX_VALUE;

            for(int i = 0; i < stops.size(); i++) {
                Stop stop = stops.get(i);
                distance = getDistance(currentStop, stop);
                if(distance < minDistance) {
                    minDistance = distance;
                    indexOfNearest = i;
                }
            }

            currentStop = stops.get(indexOfNearest);
            sortedStops.add(currentStop);
            stops.remove(indexOfNearest); // pocevsi od prosledjenog indeksa obrisi jedan element
        }

        sortedStops.add(stops.get(0));

        return sortedStops;
    }

    private static int getDistance(Stop currentStop, Stop nextStop) {
        String apiKey = "AIzaSyCncyqJ42IAu6XewfdwvXyVmCOUyr30gWI";

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+currentStop.getLatitude()+","+currentStop.getLongitude()+"&destinations="+nextStop.getLatitude()+","+nextStop.getLongitude()+"&key="+ apiKey);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            //connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder("");
            String procitanaLinija;

            while ((procitanaLinija = in.readLine()) != null) {
                sb.append(procitanaLinija);
            }

            HashMap response = JsonMapper.jsonToT(sb.toString(), HashMap.class);
            if(response.get("status").equals("OK")) {
                ArrayList rows = (ArrayList) response.get("rows");
                ArrayList elements = (ArrayList) ((HashMap) rows.get(0)).get("elements");
                HashMap distance = (HashMap) ((HashMap) elements.get(0)).get("distance");
                int distanceValue = (int) distance.get("value");

                return distanceValue;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return  -1;
    }
}
