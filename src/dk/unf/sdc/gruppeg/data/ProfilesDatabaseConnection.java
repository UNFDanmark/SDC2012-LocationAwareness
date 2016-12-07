package dk.unf.sdc.gruppeg.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import dk.unf.sdc.gruppeg.Profil;

/**
 * Eksempelklasse for brugen af SQLite under Android Denne klasse vil vise en
 * meget primitiv anvendelse af SQLite til Android. Vi udvider blot den
 * abstrakte klasse SQLiteOpenHelper. L�s om denne her:
 * http://developer.android.
 * com/reference/android/database/sqlite/SQLiteOpenHelper.html
 * 
 * Min databse skal indenholde kontakter, hvortil man kan gemme: Navn
 * Telefonnummer En r�kke grupper, som ens kontakt er medlem af
 * 
 * Vi skal bruge nogle hj�lpeklasser, Contact og Group. Disse er beskrevet i
 * deres egen source code.
 * 
 * Vores database vil indenholde to tabeller: contacts (INT id, TEXT name, TEXT
 * phone) contactGroups (INT contactId, TEXT groupName) PRIMARY KEY for contacts
 * er id, mens PRIMARY KEY for contactGroups er (INT contactId, TEXT groupName).
 * 
 * Potentiel udvidelse: Man kan oprette endnu en tabel groups (TEXT groupName,
 * BLOB groupImage) Med PRIMARY KEY groupName BLOB er en datatype, som tillader
 * at gemme filer, som er i bin�rt format (dvs. i praksis alting, som man ikke
 * kan gemme i TEXT).
 * 
 * @author Tobias Ansbak Louv
 */
// TODO: nedarver ikke fra SQLiteOpenHelper men fra DatabaseManagerAbstraction..
// Forklar!
public class ProfilesDatabaseConnection extends DatabaseManagerAbstraction {

	/*
	 * For at have en nem m�de at huske database-, tabel-, kolonnenavne,
	 * deklererer vi en masse statiske feltvariable. P� denne m�de vil
	 * compileren hj�lpe os lidt.
	 */

	/*
	 * Databasenavn - giv den nu noget sigende! :-) Fremover vil I referere til
	 * databasenavnet udelukkene ved at skrive
	 * ContactsManagerOpenHelper.DATABASE_NAME eller kort DATABASE_NAME.
	 */
	private static final String DATABASE_NAME = "profilesManager";

	/*
	 * Navnen p� tabelerne i jeres database Igen: Giv dem noget sigende! :-)
	 */
	private static final String TABLE_PROFILES = "profiles";
	// private static final String TABLE_CONTACT_GROUP = "contactGroup";

	/*
	 * Navnene p� kolonnerne i tabellerne. Hvad tror I, jeg vil sige om
	 * navngivningen? Giv dem noget sigende! :-)
	 */
	// Kolonnerne i contacts
	private static final String PROFILE_ID = "id";
	private static final String PROFILE_NAME = "name";
	private static final String PROFILE_ENABLED = "active";
	private static final String PROFILE_LONG = "longt";
	private static final String PROFILE_LAT = "lat";
	private static final String PROFILE_RADIUS = "radius";
	private static final String PROFILE_STARTTIME = "starttime";
	private static final String PROFILE_ENDTIME = "endtime";
	private static final String PROFILE_USINGTIME = "usingtime";
	private static final String PROFILE_USINGDAYS = "usindays";
	private static final String PROFILE_USINGLOCATION = "usinglocation";
	private static final String PROFILE_DAYS = "days";

	private static final String PROFILE_WIFI = "wifi";
	private static final String PROFILE_FLIGHT = "flight";
	private static final String PROFILE_BLUETOOTH = "blue";
	private static final String PROFILE_TETHER = "tether";
	private static final String PROFILE_SCREEN = "screen";

	// Kolonnerne i contactGroup

	/*
	 * For at bruge en database, skal vi oprette tabeller. Dette m� vi g�re ved
	 * at skrive noget SQL. Hj�lp kan findes p�:
	 * http://www.sqlite.org/lang_createtable.html
	 * 
	 * SQL'en som skal k�res for at oprette tabellerne, jeg har beskrevet
	 * ovenfor
	 */
	// For at oprette contacts
	private static final String TABLE_CREATE_CONTACTS = "CREATE TABLE "
			+ TABLE_PROFILES + " ( " + PROFILE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROFILE_NAME + " TEXT, "
			+ PROFILE_ENABLED + " TEXT, " + PROFILE_LONG + " TEXT, "
			+ PROFILE_LAT + " TEXT, " + PROFILE_STARTTIME + " TEXT, "
			+ PROFILE_ENDTIME + " TEXT, " + PROFILE_DAYS + " TEXT, "

			+ PROFILE_WIFI + " TEXT, " + PROFILE_FLIGHT + " TEXT, "
			+ PROFILE_BLUETOOTH + " TEXT, " + PROFILE_SCREEN + " TEXT, "
			+ PROFILE_RADIUS + " TEXT, " + PROFILE_USINGDAYS + " TEXT, "
			+ PROFILE_USINGLOCATION + " TEXT, " + PROFILE_USINGTIME + " TEXT, "
			+ PROFILE_TETHER + " TEXT" + ");";

	// For at oprette contactGroup

	/**
	 * Constructor for
	 * 
	 * @param context
	 */
	public ProfilesDatabaseConnection(Context context) {
		/*
		 * Her kalder vi tilbage til konstrukt�ren i DatabaseManagerAbstraction,
		 * for at forberede os p� at bruge SQLite. Der bliver egentlig ikke
		 * oprettet nogen database endnu, men vi g�r os klar!
		 */
		super(context, DATABASE_NAME);
		// Ingen yderligere handling er n�dvendig her.
	}

	/*
	 * Denne metode er vigtig, da det er her, vi skal oprette vores database og
	 * tabeller.
	 * 
	 * I t�nker m�ske: Hvorfor ikke bare oprette tabeller direkte i
	 * konstrukt�ren? Svaret er: Androidsystemet holder styr p� memory, CPU,
	 * osv., s� konstrukt�ren skal ikke lavet noget egentlig arbejde. Hold jer
	 * til dette, ellers sker der underlige ting!
	 * 
	 * I dette eksempel arbejder vi med to tabeller, s� vi skal oprette begge
	 * her.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Her vil vi oprette de to tabeller, som vi skal bruge. Dette g�res ved
		 * at udf�re SQL'en (execute SQL).
		 */

		// Vi opretter contacts
		db.execSQL(TABLE_CREATE_CONTACTS);
		Log.e("", TABLE_CREATE_CONTACTS);
	}

	// Her inds�tter vi data i databasen
	/**
	 * Add a contact to the database
	 * 
	 * @param c
	 *            the contact to be added
	 */
	public void addProfile(Profil profile) {
		/*
		 * Nu skal vi til at lave et ContentValues-objekter, for at kunne
		 * inds�tte data i tabellerne. Man skal oprette et key-value-pair for
		 * hver kolonne i tabellen, som ikke har en defaultv�rdi. Kolonnenavnene
		 * er selvf�lgelig keys.
		 */

		/*
		 * Her laver vi key-value-pairs for PROFILE_NAME- og
		 * KEY_CONTACTS_PHONE-kolonnerne. Vi laver *ikke* for ID'et, da der er
		 * auto increement p�.
		 */
		ContentValues table_contacts_values = new ContentValues();
		table_contacts_values.put(PROFILE_NAME, profile.getName());
		table_contacts_values.put(PROFILE_ENABLED, profile.getActive());

		try {
			Location myLoc = profile.getLocation();
			Log.v("", myLoc.toString());
			table_contacts_values.put(PROFILE_LONG, profile.getLocation()
					.getLongitude()+"");
			table_contacts_values.put(PROFILE_LAT, profile.getLocation()
					.getLatitude()+"");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("", "I kinda crashed during location work");
			table_contacts_values.put(PROFILE_LONG, 0);
			table_contacts_values.put(PROFILE_LAT, 0);
		}
		table_contacts_values.put(PROFILE_RADIUS, profile.getDistance());
		table_contacts_values.put(PROFILE_STARTTIME, profile.getStartTid());
		table_contacts_values.put(PROFILE_ENDTIME, profile.getSlutTid());
		table_contacts_values.put(PROFILE_DAYS, profile.getDage());
		Log.e("","Dayge "+profile.getDage());
		table_contacts_values.put(PROFILE_USINGDAYS, profile.isBrugDag());
		table_contacts_values.put(PROFILE_USINGTIME, profile.isBrugTid());
		table_contacts_values.put(PROFILE_USINGLOCATION, profile.isBrugLocation());

		table_contacts_values.put(PROFILE_WIFI, profile.isWifi());
		table_contacts_values.put(PROFILE_FLIGHT, profile.isFlymode());
		table_contacts_values.put(PROFILE_BLUETOOTH, profile.isBluetooth());
		table_contacts_values.put(PROFILE_SCREEN, profile.isLysstyrke());
		table_contacts_values.put(PROFILE_TETHER, profile.isTether());
		/*
		 * Inds�t r�kker i contacts Resultatet fra db.insert-metoden er en long,
		 * hvilket er ID'et p� den r�kke, man inds�tter. Der er altid et tal at
		 * modtage, og n�r man bruger auto increement, vil det svare til
		 * r�kkeID'et (med mindre man piller ved nummeret p� v�rdierne i
		 * databsen). Der inds�ttes null udfor alle de key-value-pairs som
		 * mangler.
		 */
		long insertID;
		try {
			insertID = insertRow(TABLE_PROFILES, table_contacts_values);
		} catch (Exception e) {
			// Formenligt sker fejlen også næste gang -- delete!

			// super.deleteDB();
			return;
		}

		if (insertID == -1) {
			// Formenligt sker fejlen også næste gang -- delete!
			// super.deleteDB();
			return; // Hvis insertID == -1, skete der en fejl. Stop!
		}
		// Ellers modtager vi ID'et p� kontakten. Dette gemmer vi for en god
		// ordens skyld i kontaktobjektet
		profile.setId((int) insertID);

		/*
		 * Inds�t r�kker i contactGroup Vi kal inds�tte en post for hver gruppe,
		 * kontakten er medlem af
		 */

	}

	/**
	 * Slet en kontakt fra databasen
	 * 
	 * @param id
	 *            ID'et p� den kontakt, der skal slettes
	 */
	public void removeProfile(int id) {
		removeById(TABLE_PROFILES, PROFILE_ID, id);
	}

	/**
	 * Update a contact
	 * 
	 * @param c
	 *            the contact to be updated
	 */
	public void updateProfile(Profil profil) {
		// Opdater i contacts-tabellen.
		ContentValues values = new ContentValues();
		values.put(PROFILE_NAME, profil.getName());
		values.put(PROFILE_ENABLED, profil.getActive());
		/*
		 * values.put(PROFILE_LONG, profil.getLocation().getLongitude());
		 * valuesprofilOFILE_LAT, profil.getLocation().getLatitude());
		 */
		values.put(PROFILE_RADIUS, profil.getDistance());
		values.put(PROFILE_STARTTIME, profil.getStartTid());
		values.put(PROFILE_ENDTIME, profil.getSlutTid());
		values.put(PROFILE_DAYS, profil.getDage());
		values.put(PROFILE_USINGTIME, profil.isBrugTid());
		values.put(PROFILE_USINGDAYS, profil.isBrugDag());
		values.put(PROFILE_USINGLOCATION, profil.isBrugLocation());

		values.put(PROFILE_WIFI, profil.isWifi());
		values.put(PROFILE_FLIGHT, profil.isFlymode());
		values.put(PROFILE_BLUETOOTH, profil.isBluetooth());
		values.put(PROFILE_SCREEN, profil.isLysstyrke());
		values.put(PROFILE_TETHER, profil.isTether());

		updateById(TABLE_PROFILES, PROFILE_ID, profil.getID(), values);
	}

	/**
	 * Hent en kontakt fra databasen
	 * 
	 * @param id
	 *            ID'et p� den kontakt, der skal hentes
	 */
	/*
	 * public Profil getProfile(int id) { Map<String, Object> pMap =
	 * getById(TABLE_PROFILES, PROFILE_ID, id); if (pMap == null) return null;
	 * Profil p = new Profil((String) pMap.get(PROFILE_NAME), (int) id);
	 * Location location=new Location("DATABASE"); location.setLatitude((Double)
	 * pMap.get(PROFILE_LAT)); location.setLongitude((Double)
	 * pMap.get(PROFILE_LONG)); p.setName((String) pMap.get(PROFILE_RADIUS));
	 * p.setStartTid((String) pMap.get(PROFILE_STARTTIME));
	 * p.setSlutTid((String) pMap.get(PROFILE_ENDTIME)); p.setDage((String)
	 * pMap.get(PROFILE_DAYS));
	 * 
	 * p.setWifi((Boolean) pMap.get(PROFILE_WIFI)); p.setFlymode((Boolean)
	 * pMap.get(PROFILE_FLIGHT)); p.setBluetooth((Boolean)
	 * pMap.get(PROFILE_BLUETOOTH)); p.setBluetooth((Boolean)
	 * pMap.get(PROFILE_SCREEN)); p.setLysstyrke((Boolean)
	 * pMap.get(PROFILE_TETHER));
	 * Log.v("",pMap.get(PROFILE_LAT)+","+pMap.get(PROFILE_LONG));
	 * p.setLocation(location); return p; }
	 */

	/**
	 * Get all contacts
	 * 
	 * @return a list of all contacts in the database
	 */
	public List<Profil> getProfiles() {
		List<Profil> profiles = new ArrayList<Profil>();

		// Hent alle r�kker i TABLE_CONTACTS
		List<Map<String, Object>> profileRows = getAll(TABLE_PROFILES);

		if (profileRows.size() == 0) {
			// Der er ingen kontakter i databasen. Stop her!
			return profiles; // Retuner en tom liste af kontakter
		}

		/*
		 * L�b gennem listen af r�kker.
		 */
		for (Map<String, Object> row : profileRows) {
			// Hent data ned. Vi ved, at der ikke sker cast exceptions, fordi vi
			// ved, hvad type felterne er.
			Location location = new Location("DATABASE");
			Profil p = new Profil((String) row.get(PROFILE_NAME),
					(Integer) row.get(PROFILE_ID));

			String startTid = (String) row.get(PROFILE_STARTTIME);
			String slutTid = (String) row.get(PROFILE_ENDTIME);
			String dage = (String) row.get(PROFILE_DAYS);
			String brugerTid = (String) row.get(PROFILE_USINGTIME);
			String brugerDage = (String) row.get(PROFILE_USINGDAYS);
			String brugerLocation = (String) row.get(PROFILE_USINGLOCATION);
			String active = (String) row.get(PROFILE_ENABLED);
			String radius = (String) row.get(PROFILE_RADIUS);
			boolean profileActive;
			if (Integer.parseInt(active) == 1) {
				profileActive = true;
			} else {
				profileActive = false;
			}
			
			String Wifi = (String) row.get(PROFILE_WIFI);
			String Flymode = (String) row.get(PROFILE_FLIGHT);
			String Bluetooth = (String) row.get(PROFILE_BLUETOOTH);
			String screen = (String) row.get(PROFILE_SCREEN);
			String Lysstyrke = (String) row.get(PROFILE_TETHER);
			

			p.setStartTid(startTid);
			p.setSlutTid(slutTid);
			p.setDage(dage);
			p.setBrugTid(stringToBoolean(brugerTid));
			p.setBrugDag(stringToBoolean(brugerDage));
			//Log.e("","BRUGER DAGE: "+p.isBrugDag()+","+brugerDage);
			p.setBrugLocation(stringToBoolean(brugerLocation));

			p.setWifi(stringToBoolean(Wifi));
			p.setFlymode(stringToBoolean(Flymode));
			p.setBluetooth(stringToBoolean(Bluetooth));
			p.setLysstyrke(stringToBoolean(screen));
			p.setLysstyrke(stringToBoolean(Lysstyrke));
			p.setRadius(Integer.parseInt(radius));
			String lat = (String) row.get(PROFILE_LAT);
			String lon = (String) row.get(PROFILE_LONG);
			/*
			 * location.setLatitude((Double) row.get(PROFILE_LAT));
			 * location.setLongitude((Double) row.get(PROFILE_LONG));
			 */
			location.setLatitude(Double.parseDouble(lat));
			p.setActive(profileActive);
			location.setLongitude(Double.parseDouble(lon));
			// Log.v("",row.get(PROFILE_LAT)+","+row.get(PROFILE_LONG));
			p.setLocation(location);

			profiles.add(p);
		}
		// Aflever listen af kontakter.
		return profiles;
	}

	/**
	 * 
	 * @param string
	 *            String som skal konverteres.
	 * @return Boolean true hvis string er "1" eller "true". Alt andet giver
	 *         false, også strings med indhold
	 */
	public boolean stringToBoolean(String string) {
		if (string.equals("1") || string.equals("true")) {
			return true;
		} else
			return false;
	}

	// FIXME
	public void logTable() {
		SQLiteDatabase db;
		try {
			db = getReadableDatabase();
		} catch (Exception e) {
			Log.e("logTable", "Nope", e);
			return;
		}
		Cursor c = db.query(TABLE_PROFILES, null, null, null, null, null, null);
		String[] columnNames;
		try {
			columnNames = c.getColumnNames();
		} finally {
			c.close();
		}
		c.moveToFirst();
		int n = 0;
		String tag = "TableConsistsOf";
		do {
			n++;
			Log.e(tag, "\n\nNr." + n);
			for (int i = 0; i < columnNames.length; i++) {
				Log.e(tag, columnNames[i] + ": " + c.getString(i));
			}
		} while (c.moveToNext());
	}
}
