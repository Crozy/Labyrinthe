package Model;

public class MonstreModere extends Monstre {
	private String bs1 = "Adown1.png";
	private String bs2 = "Adown2.png";
	private String bsn = "Adownneutral.png";
	private String hs1 = "Aup1.png";
	private String hs2 = "Aup2.png";
	private String hsn = "Aupneutral.png";
	private String gs1 = "Aleft1.png";
	private String gs2 = "Aleft2.png";
	private String gsn = "Aleftneutral.png";
	private String ds1 = "Aright1.png";
	private String ds2 = "Aright2.png";
	private String dsn = "Arightneutral.png";
	
	public MonstreModere(int x, int y) {
		super(x,y);
		super.SetTabBas(bsn, bs1, bs2);
		super.SetTabHaut(hsn, hs1, hs2);
		super.SetTabGauche(gsn, gs1, gs2);
		super.SetTabDroite(dsn, ds1, ds2);
		super.SetVitesse(20);
	}
	
}