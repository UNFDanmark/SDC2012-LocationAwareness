package dk.unf.sdc.gruppeg.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is created to use at Software Development Camp 2012, held by The Danish Youth Association of Science (Ungdommens Naturvidenskabelige Forening), http://unf.dk.
 * All the comments are in Danish.
 * DatabaseManagerAbstraction by Tobias Ansbak Louv is licensed under a Creative Commons Attribution-ShareAlike 2.5 Denmark License (http://creativecommons.org/licenses/by-sa/2.5/dk/).
 * The auther is not responsible of the use of this piece of code.
 * 
 * This class defines some helping methods to implement an SQLite database within your app. 
 * The code is mainly based upon the tutorial on this site: http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * 
 * Den er redigeret senere!
 * @author Tobias Ansbak Louv (tal@unf.dk)
 */
abstract class DatabaseManagerAbstraction extends SQLiteOpenHelper {

	/*
	 * Databaseversion - sandsynligvis ikke noget, I f�r brug for overhovedet.
	 * Skal l�be fra 1 og monotont opad.
	 * Det har noget at g�re med, at n�r man opdaterer sin app, vil man m�ske opdatere sin database med nogle flere tabeller osv.
	 * Man bruger metoderne onUpgrade() og onDowngrade()
	 * Men t�nkt ikke p� det nu!
	 */
	private static final int DATABASE_VERSION = 1;
	private String DATABASE_NAME;
	private Context context;

	public DatabaseManagerAbstraction(Context context, String name) {
		/*
		 * Vi s�tter blot cursoren factory til null, hvilket giver os default.
		 * I f�r ikke brug for at lave specielle cursorer.
		 * Hvis I ikke ved, hvad en cursor er, vil I finde ud af det ved at l�se videre.
		 */
		super(context, name, null, DATABASE_VERSION);
		this.DATABASE_NAME=name;
		this.context=context;
	}

	/*
	 * Denne metode har en tilsvarende onDowngrade, som er implementeret direkte i abstraktionen SQLiteOpenHelper. Dog er onUpgrade ikke.
	 * 
	 * Nu bruger vi jo ikke version af vores databse, s� der er igen grund til at lave noget her.
	 * Vi g�r bare pr�cis det samme som med onDowngrade, nemlig smider en SQLiteException.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new SQLiteException("onUpgrade not supported");
	}

	/**
	 * Inds�tter en r�kke i tabellen.
	 * @param table
	 * @param values en ContentValues, hvor keys svarer til kolonner i tabellen og svarer til v�rdien der skal inds�ttes i denne kolonne.
	 * @return r�kkeID'et p� den indsatte r�kke. Hvis noget gik galt s� ingen r�kke bliver indsat, vil der blive returneret -1.
	 */
	public long insertRow(String table, ContentValues values) {
		// Hent databasen frem. Vi skal kunne skrive til den.
		SQLiteDatabase db = getWritableDatabase();

		/*
		 * Vi s�tter anden argument til null, fordi vi ikke �nsker at benytte det s�kaldte "null-hack". T�nk ikke over det - I f�r nok ikke brug for det!
		 * Der inds�ttes null udfor alle de key-value-pairs som mangler. Ved at skrive null her, s�rger vi for, at hvis ingen key-value-pairs er givet, vil der ikke blevet indsat nogen v�rdier overhovedet.
		 */
		long result = db.insert(table, null, values);

		// Husk at lukke forbindelsen til databasen. Og send resultatet
		db.close();
		return result;
	}

	/**
	 * Henter alle r�kker i en given tabel.
	 * Kolonnetype bliver oversat som f�lger:
	 * Blob -> byte[]
	 * Float -> Float
	 * Integer -> Integer
	 * Short -> Short
	 * Long -> Long
	 * Text -> String
	 * R�kkef�lgen af s�jlerne er tilf�ldig (dvs. regn ikke med, at f�rste indsatte r�kker er f�rste i listen).
	 * @param table tabellen, der skal hentes fra.
	 * @return en liste med et (column name, data)-maps.
	 */
	public List<Map<String, Object>> getAll(String table) {
		return getAllWhere(table, new HashMap<String, String>());
	}

	/**
	 * Henter alle r�kker i en given tabel.
	 * Kolonnetype bliver oversat som f�lger:
	 * Blob -> byte[]
	 * Float -> Float
	 * Integer -> Integer
	 * Short -> Short
	 * Long -> Long
	 * Text -> String
	 * R�kkef�lgen af s�jlerne er tilf�ldig (dvs. regn ikke med, at f�rste indsatte r�kker er f�rste i listen).
	 * Der kan udv�lges nogle kolonner, som skal checkes for lighed.
	 * @param table tabellen, der skal hentes fra.
	 * @param where key-value-pair hvor key er kolonnenavn og value er den v�rdi, som den skal have.
	 * @return en liste med et (column name, data)-maps.
	 */
	public List<Map<String, Object>> getAllWhere(String table, Map<String, String> where) {
		// Definer f�rst listen.
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		/*
		 * �bn en l�sbar database og k�r select querien.
		 */
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor;
		if (where.size() == 0) {
			// Alle null-r�kkerne er f�lgende: columns, groupBy, having, orderBy.
			cursor = db.query(table, null, null, null, null, null, null);
		}
		else {

			/*
			 * Lav selection String og selectionArgs String[] udfra brugerdata.
			 * Her kan man godt funcke op, hvis brugerdate ikke er i orden.
			 * TODO: Lav check, om brugeren laver ballade.
			 * Dog burde det kun v�re brugeren af denne klasse - ikke brugeren af jeres app, der kan lave ballade. Dvs. kun I kan lave ballade! ;-)
			 * Vi er n�dt til at g� lidt en omvej for at lave et String array, fordi man ikke dynamisk kan g�re et array st�rre.
			 */
			String selection = "";
			List<String> selectionArgsList = new LinkedList<String>();
			int numOfWheres = 0;
			for (Map.Entry<String, String> e : where.entrySet()) {
				if (numOfWheres > 0) selection += " AND ";
				selection += e.getKey() + " = ?";
				selectionArgsList.add(e.getValue());
				numOfWheres++;
			}
			String[] selectionArgs = new String[selectionArgsList.size()];
			selectionArgsList.toArray(selectionArgs);

			// Alle null-r�kkerne er f�lgende: columns, groupBy, having, orderBy.
			cursor = db.query(table, null, selection, selectionArgs, null, null, null);

		}

		if (!cursor.moveToFirst()) {
			// Tabellen er tom. Stop her!
			db.close();
			return result; // Retuner en tom liste
		}

		/*
		 * L�b igennem alle r�kker i tabellen.
		 */
		do {
			// Lav et map
			Map<String, Object> tableRow = new HashMap<String, Object>();

			// L�b igennem alle kolonner
			int noOfColumns = cursor.getColumnCount();
			for (int i=0; i<noOfColumns; i++) {
				String columnName = cursor.getColumnName(i);

				/*
				 * Find typen p� indholdet i 
				 */
				Cursor typeCursor = db.rawQuery("SELECT TYPEOF (" + columnName + ") FROM " + table, null);
				typeCursor.moveToFirst();
				String columnContentType = typeCursor.getString(0);

				/*
				 * V�lg den rigtige repr�sentation af dataen
				 */
				if (columnContentType.equals("integer")) {
					tableRow.put(columnName, Integer.valueOf(cursor.getInt(i)));
				}
				else if (columnContentType.equals("long")) {
					tableRow.put(columnName, Long.valueOf(cursor.getLong(i)));
				}
				else if (columnContentType.equals("short")) {
					tableRow.put(columnName, Short.valueOf(cursor.getShort(i)));
				}
				else if (columnContentType.equals("text")) {
					tableRow.put(columnName, cursor.getString(i));
				}
				else if (columnContentType.equals("blob")) {
					tableRow.put(columnName, cursor.getBlob(i));
				}
				else if (columnContentType.equals("real")) {
					tableRow.put(columnName, Float.valueOf(cursor.getFloat(i)));
				}
				else {
					/*
					 * Indholdstypen er sandsynligvis null, hvilket er fordi indholdet er null.
					 * Dette giver i realiteten intet under key'en columnName.
					 */
					// Hvis typen ikke er null, og vi er kommet herned, er det fordi, typen ikke er genkendt. Dette fanges derfor i loggen.
					if (!columnContentType.equals("null")) {
						Log.e("DataBaseAbstraction", "The type of the value in " + columnName + " is " + columnContentType + ", which is not recognized by this implementation. Therefore null was used.");
					}
					tableRow.put(columnName, null);
				}
			}
			// Tilf�j til listen.
			result.add(tableRow);
		} while(cursor. moveToNext());

		// Luk forbindelsen og returner svaret.
		db.close();
		return result;
	}

	/**
	 * Vil hente en r�kke p� baggrund af ID'et.
	 * @param table navnet p� tabellen.
	 * @param idRow navnet p� den kolonne, som er ID-kolonnen.
	 * @param id ID'et p� r�kken, som skal hentes frem.
	 * @return r�kken med det givne ID eller null, hvis den ikke findes.
	 */
	public Map<String, Object> getById(String table, String idRow, int id) {
		/*
		 * Vi skal bare bruge getAllWhere-metoden.
		 * Og hvis denne returnerer netop �t element, vil vi returnere 
		 */
		Map<String, String> where = new HashMap<String, String>();
		where.put(idRow, String.valueOf(id));
		
		// Hvis denne liste har netop �n r�kke, er det rigtigti. Ellers er der noget galt, og vi returnerer null.
		List<Map<String, Object>> getList = getAllWhere(table, where);
		if (getList.size() != 1) return null;
		return getList.get(0);
	}

	/**
	 * Vil slette en r�kke p� baggrund af ID'et.
	 * @param table navnet p� tabellen.
	 * @param idRow navnet p� den kolonne, som er ID-kolonnen.
	 * @param id ID'et p� r�kken, som skal slettes.
	 * @return r�kken med det givne ID eller null, hvis den ikke findes.
	 */
	public void removeById(String table, String idRow, int id) {
		// Hent en skrivbar database frem
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * Slet:
		 * Metoden db.delete skal indenholde:
		 * Tabellen, der skal slettes fra.
		 * Et slags udtryk (expression), hvor man erstatte nogle af tingene med ?-tegn.
		 * Og s� en String[] (String-array), som maskineriet erstatter med ?-tengnene. R�kkef�lgen skal passe ift. ?-tegnenes ankomst.
		 */
		db.delete(table, idRow + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}
	
	/**
	 * Opdaterer en r�kke med et givent ID i databsen. Og s�tter alle de kolonner, der er angivet i values til de v�rdier, der er angivet.
	 * @param table tabellen, der skal opdateres.
	 * @param idColumn kolonnenavnet p� ID-r�kken.
	 * @param id ID'et for r�kken.
	 * @param values et key-value-pair, hvor key er kolonnenavne og value er den v�rdi, den skal s�ttes til.
	 * @return antallet af r�kker, der blev opdateret. Hvis ID-r�kken har unikke v�rdier, vil resultatet altid v�re 0 eller 1. Hvis det er 0, blev intet opdateret. Hvis 1 det er 1, blev r�kken opdateret.
	 */
	public int updateById(String table, String idColumn, int id, ContentValues values) {
		// Hent databasen frem. Vi skal kunne skrive til den.
		SQLiteDatabase db = getWritableDatabase();
		return db.update(table, values, idColumn + " = ?", new String[] { String.valueOf(id) });
	}
	/**
	 * 
	 * @param Writeable Skal den være skrivebar?
	 * @return
	 */
	public SQLiteDatabase getDB(boolean Writeable){
		if(Writeable){return getWritableDatabase();}
		else{return getReadableDatabase();}
	}
	
	public void deleteDB(){
		context.deleteDatabase(DATABASE_NAME);
	}
}

























