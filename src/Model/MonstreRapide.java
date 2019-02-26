package Model;

public class MonstreRapide extends Monstre {
	private String bs1 = "Rdown1.png";
	private String bs2 = "Rdown2.png";
	private String bsn = "Rdownneutral.png";
	private String hs1 = "Rup1.png";
	private String hs2 = "Rup2.png";
	private String hsn = "Rupneutral.png";
	private String gs1 = "Rleft1.png";
	private String gs2 = "Rleft2.png";
	private String gsn = "Rleftneutral.png";
	private String ds1 = "Rright1.png";
	private String ds2 = "Rright2.png";
	private String dsn = "Rrightneutral.png";
	
	public MonstreRapide(int x, int y) {
		super(x,y);
		super.SetTabBas(bsn, bs1, bs2);
		super.SetTabHaut(hsn, hs1, hs2);
		super.SetTabGauche(gsn, gs1, gs2);
		super.SetTabDroite(dsn, ds1, ds2);
		super.SetVitesse(10);
	}
}
