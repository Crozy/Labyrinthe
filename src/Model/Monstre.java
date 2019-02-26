package Model;

import Controller.Controller;

public class Monstre extends Personnage {
	private int vitesseMonstre = 0;
	private int incrementeur = 0;
	
	public Monstre(int x,int y) {
		super(x,y);
	}
	
	protected void SetTabBas(String neutral, String s1, String s2) {
		super.tabBas[0] = s1;
		super.tabBas[1] = s2;
		super.tabBas[2] = neutral;
	}
	
	protected void SetTabHaut(String neutral, String s1, String s2) {
		super.tabHaut[0] = s1;
		super.tabHaut[1] = s2;
		super.tabHaut[2] = neutral;
	}
	
	protected void SetTabGauche(String neutral, String s1, String s2) {
		super.tabGauche[0] = s1;
		super.tabGauche[1] = s2;
		super.tabGauche[2] = neutral;
	}
	
	protected void SetTabDroite(String neutral, String s1, String s2) {
		super.tabDroite[0] = s1;
		super.tabDroite[1] = s2;
		super.tabDroite[2] = neutral;
	}
	
	public void SetVitesse(int l) {
		vitesseMonstre = l;
	}
	public void NextPoint() {
		if(vitesseMonstre <= incrementeur) {
			CarteDistance carte = Controller.getCarte();
			Point selection = Model.getLabyrinth().getPosition(super.GetPosition().GetX(),super.GetPosition().GetY());
			if(selection.GetEst() != null && carte.tab[selection.GetEst().GetX()][selection.GetEst().GetY()] < carte.tab[selection.GetX()][selection.GetY()]){
				selection = selection.GetEst();
				super.setTurn(3);
			}
			else if(selection.GetNord() != null && carte.tab[selection.GetNord().GetX()][selection.GetNord().GetY()] < carte.tab[selection.GetX()][selection.GetY()]){ 
				selection = selection.GetNord();
				super.setTurn(1);
			}
			else if(selection.GetOuest() != null && carte.tab[selection.GetOuest().GetX()][selection.GetOuest().GetY()] < carte.tab[selection.GetX()][selection.GetY()]){
				selection = selection.GetOuest();
				super.setTurn(2);
			}
			else if(selection.GetSud() != null && carte.tab[selection.GetSud().GetX()][selection.GetSud().GetY()] < carte.tab[selection.GetX()][selection.GetY()]) {
				selection = selection.GetSud();
				super.setTurn(0);
			}
			super.SetPosition(new Point(selection.GetX(),selection.GetY()));
			incrementeur = 0;
		}
		else
			incrementeur++;
}
}
