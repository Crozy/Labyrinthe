package Model;

public class MonstreLent extends Monstre {
	private String bs1 = "Kdown1.png";
	private String bs2 = "Kdown2.png";
	private String bsn = "Kdownneutral.png";
	private String hs1 = "Kup1.png";
	private String hs2 = "Kup2.png";
	private String hsn = "Kupneutral.png";
	private String gs1 = "Kleft1.png";
	private String gs2 = "Kleft2.png";
	private String gsn = "Kleftneutral.png";
	private String ds1 = "Kright1.png";
	private String ds2 = "Kright2.png";
	private String dsn = "Krightneutral.png";
	
	public MonstreLent(int x, int y) {
		super(x,y);
		super.SetTabBas(bsn, bs1, bs2);
		super.SetTabHaut(hsn, hs1, hs2);
		super.SetTabGauche(gsn, gs1, gs2);
		super.SetTabDroite(dsn, ds1, ds2);
		super.SetVitesse(30);
	}
}
