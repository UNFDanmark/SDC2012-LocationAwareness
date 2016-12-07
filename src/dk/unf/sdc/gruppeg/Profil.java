package dk.unf.sdc.gruppeg;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

public class Profil {
	private String name = "";
	private Boolean active = true;
	private List<String> betingelser;
	private Boolean isTrue = false;

	private boolean Wifi = false;
	// Er mulig, kræver hack
	// private boolean Datanetværk = false;
	private boolean Lydløs = false;
	private boolean Bluetooth = false;
	private boolean Tether = false;
	private boolean Lysstyrke = false;
	private boolean Flymode = false;
	private boolean Vibrator = false;

	private boolean brugDag = false;
	private String dage;
	private boolean isDag = false;
	private boolean brugTid = false;
	private String startTid;
	private String slutTid;
	private boolean isTid = false;

	private boolean brugLocation = true;
	private Location location;
	private boolean isLocated = false;
	private int distance = 0;
	private int ID;

	/**
	 * @param name
	 *            Navn på profil
	 * @param active
	 *            Er profilen aktiv
	 * @param location
	 *            Lokationsobjekt for hvor i verdenen profilen gælder
	 * @param distance
	 *            Distance fra lokation
	 * @param startDag
	 *            Dagen vi starter med at gælde
	 * @param slutDag
	 *            Sidste dag
	 * @param startTid
	 *            Fra klokken x
	 * @param slutTid
	 *            Til klokken y
	 */
	public Profil(String name, boolean active, Location location, int distance,
			String dage, String startTid, String slutTid) {
		this.name = name;
		this.active = active;

		this.location = location;
		this.distance = distance;
		this.dage = dage;
		this.startTid = startTid;
		this.slutTid = slutTid;
	}

	/**
	 * Opret profil ud fra navn og ID. Der er vigtigt selv at sætte active
	 * 
	 * @param name
	 *            Navnet på profilen
	 * @param id
	 *            ID'et på profilen
	 */
	public Profil(String name, int id) {
		this.name = name;
		this.ID = id;
	}

	public Profil(String name) {
		this.name = name;
	}

	public Profil(String name, boolean active, String dage, String startTid,
			String slutTid, Context context) {
		this.name = name;
		this.active = active;
		this.isTrue = false;

		this.dage = dage;
		this.startTid = startTid;
		this.slutTid = slutTid;
	}
	
	public boolean isProfileConditionsSatisfied(Location newLocation) {
		boolean dagConditionSatisfied = false;
		boolean tidConditionSatisfied = false;
		boolean locConditionSatisfied = false;
		
		if(brugDag) {
			Time now = new Time();
			now.setToNow();
			Log.e("","Dagene er: "+dage);
			if ( // Er dagen i stringen? Og er det i dag?
				dage.indexOf("1") > -1 
				&& now.weekDay == 1
				|| dage.indexOf(2 + "") > -1
				&& now.weekDay == 2
				|| dage.indexOf(3 + "") > -1
				&& now.weekDay == 3
				|| dage.indexOf(4 + "") > -1
				&& now.weekDay == 4
				|| dage.indexOf(5 + "") > -1
				&& now.weekDay == 5
				|| dage.indexOf(6 + "") > -1
				&& now.weekDay == 6
				|| dage.indexOf(7 + "") > -1 
				&& now.weekDay == 7) {
				
				dagConditionSatisfied = true;
			}		
		}
		else dagConditionSatisfied = true;
		if(brugTid) {
			Time now = new Time();
			now.setToNow();
			int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
			int HOUR_OF_DAY = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int startTidTimer = Integer.parseInt(startTid.split(":")[0]);
			int startTidMinutter = Integer.parseInt(startTid.split(":")[1]);
			int slutTidTimer = Integer.parseInt(slutTid.split(":")[0]);
			int slutTidMinutter = Integer.parseInt(slutTid.split(":")[1]);
			int staTid=startTidTimer*60+startTidMinutter;
			int sluTid=slutTidTimer*60+slutTidMinutter;
			if (staTid < HOUR_OF_DAY*60 +MINUTE&&
					sluTid > HOUR_OF_DAY*60 +MINUTE) {
				tidConditionSatisfied = true;
			}
			Log.e("","Det er "+(tidConditionSatisfied?"":"ikke ")+"tid kl. "+HOUR_OF_DAY+":"+MINUTE+" vs. "
			+startTidTimer+":"+startTidMinutter+"(bruger"+(brugTid?" ":" ikke ")+"tid)");
		} else tidConditionSatisfied = true;
		if(brugLocation) {
			Log.e("","Mellem "+location.getLatitude()+","+location.getLongitude()+" og "+newLocation.getLatitude()+","+newLocation.getLongitude()+" er der "+newLocation.distanceTo(location)+" hvilket er "+(newLocation.distanceTo(location)<distance?"kortere":"længere") +"end " + distance);
			if (newLocation.distanceTo(location) < distance) {
				locConditionSatisfied = true;
			}
		} else locConditionSatisfied = true;
		
		return locConditionSatisfied && tidConditionSatisfied && dagConditionSatisfied;
		
	}

	/** Betingelserne er opfyldt check */
	public boolean isTrue() {
		Log.e("",""+brugDag);
		if (active == false) {
			return false;
		}
		boolean go = false;
		if (brugDag) {
			if (isDag) {
				if (brugTid) {
					if (isTid) {
						go = true;
						Log.e(name, "Jeg er på det rigtige tidpunkt og dag!");
					}
				} else {
					go = true;
					Log.e(name, "Jeg er på den rigtige dag!");
				}
			} else {
				Log.e(name, "Jeg er ikke på den rigtige dag :-(");
			}
		} else {
			Log.e(name, "Jeg bruger ikke dage.");
		}
		if (brugLocation) {
			if (isLocated) {
				go = true;
				Log.e(name, "Jeg er lokaliseret!");
			} else {
				go = false;
				Log.e(name, "Jeg er ikke lokaliseret!");
			}
		} else {
			Log.e(name, "Jeg bruger ikke lokation.");
		}
		if (brugTid&&!(brugDag||isDag)) {
			if (isTid) {
				go = true;
				Log.e(name, "Jeg er på det rigtige tidpunkt og dag!");
			}
		} else {
			go = true;
			Log.e(name, "Jeg er på den rigtige dag!");
		}
		return (go);
	}

	/**
	 * Samme som isTrue(), men udfører først checks
	 * 
	 * @param doChecks
	 *            true hvis checksne skal ske. false er unødvendig
	 * @return true/false
	 */
	public boolean isTrue(boolean doChecks) {
		Log.e("","Jeg er en profil som hedder "+name);
		if (brugTid) {
			checkTid();
		}
		if (brugDag) {
			checkDag();
		}
		return isTrue();
	}

	/**
	 * BRUG IKKE BRUG IKKE BRUG IKKE BRUG IKKE BRUG IKKE 
	 * Skal kaldes når brugerens lokation har ændret sig -- der er ny
	 * positionsdata
	 * 
	 * @param newLocation
	 *            Det nye lokationsobjekt
	 */
	public void locationChanged(Location newLocation) {
		/*Log.v("", newLocation.getLatitude() + "");
		Log.v("", location.getLatitude() + "");
		Log.e("", "Distance til location: " + newLocation.distanceTo(location));*/
		if (newLocation.distanceTo(location) < distance) {
			isLocated = true;
		} else {
			isLocated = false;
		}
	}

	public void checkTid() {
		Time now = new Time();
		now.setToNow();
		int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
		int HOUR_OF_DAY = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int startTidTimer = Integer.parseInt(startTid.split(":")[0]);
		int startTidMinutter = Integer.parseInt(startTid.split(":")[1]);
		int slutTidTimer = Integer.parseInt(slutTid.split(":")[0]);
		int slutTidMinutter = Integer.parseInt(slutTid.split(":")[1]);
		if (startTidTimer < HOUR_OF_DAY && startTidMinutter < MINUTE
				&& slutTidTimer > HOUR_OF_DAY
				&& slutTidMinutter > MINUTE) {
			isTid = true;
		}
		Log.e("","Det er "+(isTid?"":"ikke ")+"tid kl. "+HOUR_OF_DAY+":"+MINUTE+" vs. "
		+startTidMinutter+":"+startTidTimer+"(bruger"+(brugTid?" ":" ikke ")+"tid)");
	}

	public void checkDag() {
		Time now = new Time();
		now.setToNow();
		Log.e("","Dagene er: "+dage);
//		Log.e("","Today is: "+Calendar.DAY_OF_WEEK +" which is not one of "+dage + " ("+dage.indexOf(Calendar.DAY_OF_WEEK)+")");
			if ( // Er dagen i stringen? Og er det i dag?
				dage.indexOf("1") > -1 
				&& now.weekDay == 1
				|| dage.indexOf(2 + "") > -1
				&& now.weekDay == 2
				|| dage.indexOf(3 + "") > -1
				&& now.weekDay == 3
				|| dage.indexOf(4 + "") > -1
				&& now.weekDay == 4
				|| dage.indexOf(5 + "") > -1
				&& now.weekDay == 5
				|| dage.indexOf(6 + "") > -1
				&& now.weekDay == 6
				|| dage.indexOf(7 + "") > -1 
				&& now.weekDay == 7) {
				
			isDag = true;
		}
			Log.e("",now.weekDay+"");
				
	}
	
	public void setRadius(int radius){
		this.distance=radius;
	}

	public void setDage(String dage) {
		this.dage = dage;
	}

	public void setTider(String startTid, String slutTid) {
		this.startTid = startTid;
		this.slutTid = slutTid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<String> getBetingelser() {
		return betingelser;
	}

	public void setBetingelser(List<String> betingelser) {
		this.betingelser = betingelser;
	}

	public boolean isWifi() {
		return Wifi;
	}	

	public void setWifi(boolean wifi) {
		Wifi = wifi;
	}

	public boolean isLydløs() {
		return Lydløs;
	}

	public void setLydløs(boolean lydløs) {
		Lydløs = lydløs;
	}

	public boolean isBluetooth() {
		return Bluetooth;
	}

	public void setBluetooth(boolean bluetooth) {
		Bluetooth = bluetooth;
	}

	public boolean isTether() {
		return Tether;
	}

	public void setTether(boolean tether) {
		Tether = tether;
	}

	public boolean isLysstyrke() {
		return Lysstyrke;
	}

	public void setLysstyrke(boolean lysstyrke) {
		Lysstyrke = lysstyrke;
	}

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}

	public boolean isBrugDag() {
		return brugDag;
	}

	public void setBrugDag(boolean brugDag) {
		this.brugDag = brugDag;
	}

	public boolean isDag() {
		return isDag;
	}

	public void setDag(boolean isDag) {
		this.isDag = isDag;
	}

	public boolean isBrugTid() {
		return brugTid;
	}

	public void setBrugTid(boolean brugTid) {
		this.brugTid = brugTid;
	}

	public String getStartTid() {
		return startTid;
	}

	public void setStartTid(String startTid) {
		this.startTid = startTid;
	}

	public String getSlutTid() {
		return slutTid;
	}

	public void setSlutTid(String slutTid) {
		this.slutTid = slutTid;
	}

	public boolean isTid() {
		return isTid;
	}

	public void setTid(boolean isTid) {
		this.isTid = isTid;
	}

	public boolean isBrugLocation() {
		return brugLocation;
	}

	public void setBrugLocation(boolean brugLocation) {
		this.brugLocation = brugLocation;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isLocated() {
		return isLocated;
	}

	public void setLocated(boolean isLocated) {
		this.isLocated = isLocated;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setId(int insertID) {
		ID = insertID;
	}

	public int getID() {
		return ID;
	}

	public boolean isFlymode() {
		return Flymode;
	}

	public void setFlymode(boolean flymode) {
		Flymode = flymode;
	}

	public boolean isVibrator() {
		return Vibrator;
	}

	public void setVibrator(boolean vibrator) {
		Vibrator = vibrator;
	}

	public String getDage() {
		return dage;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
