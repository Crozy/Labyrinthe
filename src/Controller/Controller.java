package Controller;

import javafx.event.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import Model.*;
import View.*;

public class Controller implements ActionListener {
	private static Joueur joueur;
	private static Controller instance = null;
	private static Model model;
	private static AnimationTimer timer;
	private static Point arrivee;
	private static CarteDistance carte;
	private static Point[] coins;
	public int tour = 0;
	private Boolean bloque = true;
	private HashMap<String, Node> memoir = new HashMap();

	// private static Monstre[] monstres;
	private static int nbMonstre = 1;

	private static Interrupteur[] portes;
	private static int nbPorte = 0;

	private static int tailleLabyrinthe = 10;
	private static int nbcoins = 3;

	private static Score score;
	private static int niveau = 0;

	public static int getScore() {
		return score.GetScore();
	}

	public static int getNiveau() {
		return niveau + 1;
	}

	public static int getNbCoins() {
		return nbcoins;
	}

	public static int getTurn(int i) {
		return joueur.getTurn();
	}

	public static Point GetJoueurPosition() {
		return joueur.GetPosition();
	}

	public static CarteDistance getCarte() {
		return carte;
	}

	public static void ActualiserCarte() {
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public Controller() {
		memoir.put("est", new Node("est"));
		memoir.put("ouest", new Node("ouest"));
		memoir.put("nord", new Node("nord"));
		memoir.put("sud", new Node("sud"));
		memoir.put("estCollision", new Node("estCollision"));
		memoir.put("ouestCollision", new Node("ouestCollision"));
		memoir.put("nordCollision", new Node("nordCollision"));
		memoir.put("sudCollision", new Node("sudCollision"));
		score = new Score();
		joueur = new Joueur(0, 0);
		arrivee = new Point(tailleLabyrinthe - 1, tailleLabyrinthe - 1);
		initCoins();
		model = new Model();
		carte = new CarteDistance();
		initPorte();
		int t = 0;
	}

	private static void initPorte() {
		int i = 0;
		portes = new Interrupteur[nbPorte * 2];
		while (i < nbPorte * 2) {
			carte = new CarteDistance();
			Arrete porte;
			do {
				porte = Model.getLabyrinth().GetPorte(carte);
			} while (porte.getPoint1().compareTo(joueur.GetPosition()) == 0
					|| porte.getPoint2().compareTo(joueur.GetPosition()) == 0
					|| porte.getPoint1().compareTo(arrivee) == 0 || porte.getPoint2().compareTo(arrivee) == 0);
			Point interrupteurOuverture;
			do {
				interrupteurOuverture = new Point((int) (Math.random() * (tailleLabyrinthe - 1)),
						(int) (Math.random() * (tailleLabyrinthe - 1)));
			} while (carte.tab[interrupteurOuverture.GetX()][interrupteurOuverture
					.GetY()] > carte.tab[porte.getPoint1().GetX()][porte.getPoint1().GetY()]
					|| carte.tab[interrupteurOuverture.GetX()][interrupteurOuverture
							.GetY()] > carte.tab[porte.getPoint2().GetX()][porte.getPoint2().GetY()]
					|| carte.tab[interrupteurOuverture.GetX()][interrupteurOuverture.GetY()] == -1);
			Point interrupteurFermeture;
			do {
				interrupteurFermeture = new Point((int) (Math.random() * (tailleLabyrinthe - 1)),
						(int) (Math.random() * (tailleLabyrinthe - 1)));
			} while (carte.tab[interrupteurFermeture.GetX()][interrupteurFermeture
					.GetY()] < carte.tab[porte.getPoint1().GetX()][porte.getPoint1().GetY()]
					|| carte.tab[interrupteurFermeture.GetX()][interrupteurFermeture
							.GetY()] < carte.tab[porte.getPoint2().GetX()][porte.getPoint2().GetY()]
					|| carte.tab[interrupteurFermeture.GetX()][interrupteurFermeture.GetY()] == -1);
			portes[i] = new Interrupteur(interrupteurOuverture, porte, Interrupteur.Action.ouvrir);
			portes[i + 1] = new Interrupteur(interrupteurFermeture, porte, Interrupteur.Action.fermer);
			i += 2;
		}
		for (i = 0; i < nbPorte; i++)
			portes[i * 2 + 1].interrupteurActionner();
	}

	public static Interrupteur.Action GetTypeInterrupteur(int i) {
		return portes[i].GetAction();
	}

	public static int getNbMonster() {
		return nbMonstre;
	}

	public static int getNbInterrupteur() {
		return nbPorte * 2;
	}

	public static Point positionPersonnage(int i) {
		return joueur.GetPosition();
	}

	public static Point positionInterrupteur(int i) {
		return portes[i].GetPosition();
	}

	public static void start(Stage primaryStage) throws Exception {
		View.getInstance().createGlobalView(primaryStage, Model.getLabyrinth());
		View.getInstance().raffraichir(Model.getLabyrinth());
		primaryStage.show();
		timer = new AnimationTimer(getInstance());
		timer.start();
	}

	private static void initCoins() {
		coins = new Point[nbcoins];

		int i = 0;
		while (i != nbcoins) {
			coins[i] = new Point((int) (Math.random() * (tailleLabyrinthe - 1)),
					(int) (Math.random() * (tailleLabyrinthe - 1)));
			if (coins[i].compareTo(joueur.GetPosition()) == 0) {
				i--;
			}
			for (int j = i - 1; j >= 0; j--) {
				if (coins[i].compareTo(coins[j]) == 0) {
					i--;
				}
			}
			i++;
		}

	}

	private static void AttraperPiece() {
		for (int i = 0; i < nbcoins; i++) {
			if (joueur.GetPosition().compareTo(getCoin(i)) == 0) {
				coins[i] = new Point(-1, -1);
				score.AjouterScore(100);
			}
		}
	}

	public static EventHandler<KeyEvent> eventHandlerButton = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent event) {

			int x = joueur.GetPosition().GetX();
			int y = joueur.GetPosition().GetY();
			if (event.getCode().equals(KeyCode.UP)) {
				joueur.setTurn(1);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetNord());
			} else if (event.getCode() == KeyCode.DOWN) {
				joueur.setTurn(0);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetSud());
			} else if (event.getCode() == KeyCode.LEFT) {
				joueur.setTurn(2);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetOuest());
			} else if (event.getCode() == KeyCode.RIGHT) {
				joueur.setTurn(3);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetEst());
			}
			AttraperPiece();
			ActualiserCarte();
		}

	};

	public void actionPerformed(ActionEvent arg0) {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				scheduler.shutdown();
				Platform.runLater(new Runnable() {
					public void run() {
						for (int i = 0; i < nbPorte * 2; i++) {
							if (portes[i].GetPosition().compareTo(joueur.GetPosition()) == 0)
								portes[i].interrupteurActionner();
						}
						View.getInstance().raffraichir(Model.getLabyrinth());

						int x = joueur.GetPosition().GetX();
						int y = joueur.GetPosition().GetY();

						if ((joueur.GetPosition().GetX() != arrivee.GetX())
								|| (joueur.GetPosition().GetY() != arrivee.GetY())) {
							AttraperPiece();
							algo();

							Point currentPoint = Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(),
									joueur.GetPosition().GetY());
							if (currentPoint.getSuccesseur() != null) {
								Point successeur = currentPoint.getSuccesseur();
								joueur.SetPosition(successeur);
							}

							if (bloque) {
								choiceAction();
							}
						}

						System.out.println("Tour : " + tour);

						System.out.println(
								"Position joueur : " + joueur.GetPosition().GetX() + " " + joueur.GetPosition().GetY());
						tour++;
					}
				});
			}
		}, 1, 1, TimeUnit.MILLISECONDS);
	}

	public static Point getCoin(int i) {
		return coins[i];
	}

	public static String getIdImage(int i) {
		return joueur.getIdImage();
	}

	public void choiceAction() {

		Random random = new Random();

		for (Node action : memoir.values()) {
			if (action.getName().equals("nord") || action.getName().equals("sud") || action.getName().equals("est")
					|| action.getName().equals("ouest")) {
				int result = 1 + random.nextInt(4);
				switch (result) {
				case 1:
					if (joueur.GetPosition().GetNord() != null) {
						joueur.SetPosition(joueur.GetPosition().GetNord());
						score.AjouterScore(score.GetScore() + 10);
						memoir.get("nord").setScore(10);
						memoir.get("nord").getMemoire().add(1);
					} else {
						score.AjouterScore(score.GetScore() - 2);
						memoir.get("nord").setScore(-2);
						memoir.get("nord").getMemoire().add(2);
					}
					break;
				case 2:
					if (joueur.GetPosition().GetSud() != null) {
						joueur.SetPosition(joueur.GetPosition().GetSud());
						score.AjouterScore(score.GetScore() + 10);
						memoir.get("sud").setScore(10);
						memoir.get("sud").getMemoire().add(1);
					} else {
						score.AjouterScore(score.GetScore() - 2);
						memoir.get("sud").setScore(-2);
						memoir.get("sud").getMemoire().add(2);
					}
					break;
				case 3:
					if (joueur.GetPosition().GetEst() != null) {
						joueur.SetPosition(joueur.GetPosition().GetEst());
						score.AjouterScore(score.GetScore() + 10);
						memoir.get("est").setScore(10);
						memoir.get("est").getMemoire().add(1);
					} else {
						score.AjouterScore(score.GetScore() - 2);
						memoir.get("est").setScore(-2);
						memoir.get("est").getMemoire().add(2);
					}
					break;
				case 4:
					if (joueur.GetPosition().GetOuest() != null) {
						joueur.SetPosition(joueur.GetPosition().GetOuest());
						score.AjouterScore(score.GetScore() + 10);
						memoir.get("ouest").setScore(10);
						memoir.get("ouest").getMemoire().add(1);
					} else {
						score.AjouterScore(score.GetScore() - 2);
						memoir.get("ouest").setScore(-2);
						memoir.get("ouest").getMemoire().add(2);
					}
					break;

				default:
					break;
				}
			} else {
				break;
			}
		}
	}

	public void algo() {
		ArrayList<Point> aExplorer = new ArrayList<>();
		ArrayList<Point> visites = new ArrayList<>();
		aExplorer.add(Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(), joueur.GetPosition().GetY()));
		Point pointACoutMinimal = Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(),
				joueur.GetPosition().GetY());
		while (!aExplorer.isEmpty() && !aExplorer.contains(arrivee)) {
			aExplorer.remove(pointACoutMinimal);
			visites.add(pointACoutMinimal);

			bloque = true;

			if (pointACoutMinimal.GetNord() != null && !visites.contains(pointACoutMinimal.GetNord())) {
				computeGHAndPredecesseur(pointACoutMinimal.GetNord(), pointACoutMinimal);
				aExplorer.add(pointACoutMinimal.GetNord());
				System.out.println("NORD");
				bloque = false;
			} else if (pointACoutMinimal.GetSud() != null && !visites.contains(pointACoutMinimal.GetSud())) {
				computeGHAndPredecesseur(pointACoutMinimal.GetSud(), pointACoutMinimal);
				aExplorer.add(pointACoutMinimal.GetSud());
				System.out.println("SUD");
				bloque = false;
			} else if (pointACoutMinimal.GetEst() != null && !visites.contains(pointACoutMinimal.GetEst())) {
				computeGHAndPredecesseur(pointACoutMinimal.GetEst(), pointACoutMinimal);
				aExplorer.add(pointACoutMinimal.GetEst());
				System.out.println("EST");
				bloque = false;
			} else if (pointACoutMinimal.GetOuest() != null && !visites.contains(pointACoutMinimal.GetOuest())) {
				computeGHAndPredecesseur(pointACoutMinimal.GetOuest(), pointACoutMinimal);
				aExplorer.add(pointACoutMinimal.GetOuest());
				System.out.println("OUEST");
				bloque = false;
			} else {
				bloque = true;
			}

			int coutMin = Integer.MAX_VALUE;
			for (Point p : aExplorer) {
				if (p.getCoutF() < coutMin) {
					pointACoutMinimal = p;
				}
			}
			pointACoutMinimal.getPredecesseur().setSuccesseur(pointACoutMinimal);

		}

	}

	private void computeGHAndPredecesseur(Point currentVoisin, Point predecesseur) {
		int coutGCurrentVoisinX = Math.abs(currentVoisin.GetX() - joueur.GetPosition().GetX());
		int coutGCurrentVoisinY = Math.abs(currentVoisin.GetY() - joueur.GetPosition().GetY());
		currentVoisin.setCoutG(coutGCurrentVoisinX + coutGCurrentVoisinY);
		int coutHCurrentVoisinX = Math.abs(currentVoisin.GetX() - arrivee.GetX());
		int coutHCurrentVoisinY = Math.abs(currentVoisin.GetY() - arrivee.GetY());
		currentVoisin.setCoutH(coutHCurrentVoisinX + coutHCurrentVoisinY);
		currentVoisin.setPredecesseur(predecesseur);
	}
}
