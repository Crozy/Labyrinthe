package Controller;

import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import Model.*;
import View.*;
import Model.Arrete.Matiere;
import org.omg.CORBA.INTERNAL;

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
	private Boolean debutParti = true;
	private HashMap<String, Node> memoir = new HashMap();
	private ArrayList<Point> listCheminEffectue;
	private String actionPrec = null;

	// private static Monstre[] monstres;
	private static int nbMonstre = 1;

	private static Interrupteur[] portes;
	private static int nbPorte = 0;

	private static int tailleLabyrinthe = 10;
	private static int nbcoins = 3;

	private static Score score;
	private static int niveau = 0;

//	private ArrayList<Point> aExplorer = new ArrayList<>();
//	private ArrayList<Point> visites = new ArrayList<>();

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
		// if(i==nbMonstre) {
		return joueur.getTurn();
		// else
		// return monstres[i].getTurn();
	}

	public static Point GetJoueurPosition() {
		return joueur.GetPosition();
	}

	public static CarteDistance getCarte() {
		return carte;
	}

	public static void ActualiserCarte() {
		// carte = new CarteDistance();
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
		//algo();
		int t = 0;
		// initMonstre();
	}

//	private static void initMonstre() {
//		int i = 0;
//		while(i < nbMonstre) {
//			if(niveau == 0) {
//				monstres = new Monstre[nbMonstre];
//				monstres[i] = new MonstreLent((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[i].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 1) {
//				nbPorte=1;
//				monstres = new Monstre[nbMonstre];
//				monstres[i] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[i].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 2) {
//				monstres[i] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[i].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 3) {
//				nbPorte=2;
//				nbMonstre=2;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreLent((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 4) {
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreLent((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 5) {
//				nbPorte=3;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 6) {
//				nbMonstre=3;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreLent((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[2] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[2].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 7) {
//				nbMonstre=3;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[2] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[2].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 8) {
//				nbPorte=4;
//				nbMonstre=3;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreModere((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[2] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[2].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			if(niveau == 9) {
//				nbMonstre=3;
//				monstres = new Monstre[nbMonstre];
//				monstres[0] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[0].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[1] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[1].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//				monstres[2] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[2].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			else {
//				monstres = new Monstre[nbMonstre];
//				monstres[i] = new MonstreRapide((int)(Math.random()*(tailleLabyrinthe-1)),(int)(Math.random()*(tailleLabyrinthe-1)));
//				if(monstres[i].GetPosition().compareTo(joueur.GetPosition())==0)
//					i--;
//			}
//			i++;
//		}
//	}
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
		// if(i == nbMonstre)
		return joueur.GetPosition();
		// return monstres[i].GetPosition();
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
//	private static void GameOver() {
//		for(int i = 0; i < nbMonstre; i++) {
//			if(joueur.GetPosition().compareTo(monstres[i].GetPosition()) == 0) {
//				joueur.SetPosition(new Point(0,0));
//				arrivee = new Point(tailleLabyrinthe-1,tailleLabyrinthe-1);
//				model.setLabyrinth(new Graph(tailleLabyrinthe,tailleLabyrinthe));
//				initPorte();
//				initMonstre();
//				initCoins();
//				score.PerdreScore();
//				break;
//			}
//}algo

//		if(joueur.GetPosition().compareTo(arrivee) == 0) {
//			score.AjouterScore(nbcoins*100);
//			niveau++;
//			tailleLabyrinthe++;
//			joueur.SetPosition(new Point(0,0));
//			arrivee = new Point(tailleLabyrinthe-1,tailleLabyrinthe-1);
//			model.setLabyrinth(new Graph(tailleLabyrinthe,tailleLabyrinthe));
//			if(niveau>9)
//				nbMonstre++;
//			initMonstre();
//			initPorte();
//			nbcoins++;
//			initCoins();
//		}
//	}

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
				// turn = 1;
				joueur.setTurn(1);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetNord());
			} else if (event.getCode() == KeyCode.DOWN) {
				// turn = 0;
				joueur.setTurn(0);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetSud());
			} else if (event.getCode() == KeyCode.LEFT) {
				// turn = 2;
				joueur.setTurn(2);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetOuest());
			} else if (event.getCode() == KeyCode.RIGHT) {
				// turn = 3;
				joueur.setTurn(3);
				joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetEst());
			}
			// GameOver();
			 AttraperPiece();
			ActualiserCarte();
		}

	};

	public void actionPerformed(ActionEvent arg0) {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				scheduler.shutdown();
//                    	try {
//							Thread.sleep(100);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
				Platform.runLater(new Runnable() {
					public void run() {
//                            	for(int i = 0; i<nbMonstre;i++){
//                        			monstres[i].NextPoint();
//                            	}
						for (int i = 0; i < nbPorte * 2; i++) {
							if (portes[i].GetPosition().compareTo(joueur.GetPosition()) == 0)
								portes[i].interrupteurActionner();
						}
						View.getInstance().raffraichir(Model.getLabyrinth());
						// GameOver();

						int x = joueur.GetPosition().GetX();
						int y = joueur.GetPosition().GetY();

						if((joueur.GetPosition().GetX() != arrivee.GetX()) || (joueur.GetPosition().GetY() != arrivee.GetY())){
							AttraperPiece();
							algo();
						
						
						Point currentPoint = Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(),
								joueur.GetPosition().GetY());
						if (currentPoint.getSuccesseur() != null) {
							Point successeur = currentPoint.getSuccesseur();
							joueur.SetPosition(successeur);
						}
						
						if(bloque) {
							choiceAction();
						}
						}
						//choiceAction();
						 
						//joueur.setTurn(0);
						// joueur.SetPosition(Model.getLabyrinth().GetPoint(x, y).GetSud());

						// System.out.println(arrivee.GetX() + " " + arrivee.GetY());

						System.out.println("Tour : " + tour);

						System.out.println(
								"Position joueur : " + joueur.GetPosition().GetX() + " " + joueur.GetPosition().GetY());

//						System.out.println("Taille H : " + Model.getLabyrinth().getH() + "taille W : "
//								+ Model.getLabyrinth().getW());
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
		// if(i == nbMonstre)
		return joueur.getIdImage();
		// return monstres[i].getIdImage();
	}

	public void choiceAction() {

		Random random = new Random();

		for (Node action : memoir.values()) {
		//	if((joueur.GetPosition().GetX() != arrivee.GetX()) || (joueur.GetPosition().GetY() != arrivee.GetY())){
			if (action.getName().equals("nord") || action.getName().equals("sud") || action.getName().equals("est")
					|| action.getName().equals("ouest")) {
//				if (action.getMemoire().size() <= 1000) {
			//	if ((joueur.GetPosition().GetX() != arrivee.GetX()) || (joueur.GetPosition().GetY() != arrivee.GetY())) {
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
					//System.out.println(arrivee.GetX() + " " + arrivee.GetY());
					break;
				}
	//		} 
//		else {
//				break;
//			}
		}

//		for (Node action : memoir.values()) {
//			if (action.getMemoire().size() <= 10) {
//				if(action.getName() == "nord") {
//					
//				}
//
//				
//			}
//		}
//		if (joueur.GetPosition().GetNord() != null) {
//			joueur.SetPosition(joueur.GetPosition().GetNord());
//			score.AjouterScore(score.GetScore() + 10);
//			memoir.get("nord").setScore(10);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("nord"));
//			}
//			actionPrec = "nord";
//		} else if (joueur.GetPosition().GetSud() != null) {
//			joueur.SetPosition(joueur.GetPosition().GetSud());
//			score.AjouterScore(score.GetScore());
//			memoir.get("sud").setScore(10);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("sud"));
//			}
//			actionPrec = "sud";
//		} else if (joueur.GetPosition().GetEst() != null) {
//			joueur.SetPosition(joueur.GetPosition().GetEst());
//			score.AjouterScore(score.GetScore() + 10);
//			memoir.get("est").setScore(10);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("est"));
//			}
//			actionPrec = "est";
//		} else if (joueur.GetPosition().GetOuest() != null) {
//			joueur.SetPosition(joueur.GetPosition().GetOuest());
//			score.AjouterScore(score.GetScore() + 10);
//			memoir.get("ouest").setScore(10);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("ouest"));
//			}
//			actionPrec = "ouest";
//		} else if (joueur.GetPosition().GetNord() == null) {
//			score.AjouterScore(score.GetScore() - 2);
//			memoir.get("nordCollision").setScore(-2);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("nordCollision"));
//			}
//			actionPrec = "nordCollision";
//		} else if (joueur.GetPosition().GetSud() == null) {
//			score.AjouterScore(score.GetScore() - 2);
//			memoir.get("sudCollision").setScore(-2);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("sudCollision"));
//			}
//			actionPrec = "sudCollision";
//		} else if (joueur.GetPosition().GetEst() == null) {
//			score.AjouterScore(score.GetScore() - 2);
//			memoir.get("estCollision").setScore(score.GetScore());
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("estCollision"));
//			}
//			actionPrec = "estCollision";
//		} else if (joueur.GetPosition().GetOuest() == null) {
//			score.AjouterScore(score.GetScore() - 2);
//			memoir.get("ouestCollision").setScore(-2);
//			if (actionPrec != null) {
//				memoir.get(actionPrec).getMemoire().add(memoir.get("ouestCollision"));
//			}
//			actionPrec = "ouestCollision";
//		}
	}

	public void algo() {
		//if((joueur.GetPosition().GetX() != arrivee.GetX()) || (joueur.GetPosition().GetY() != arrivee.GetY())){
		ArrayList<Point> aExplorer = new ArrayList<>();
		ArrayList<Point> visites = new ArrayList<>();
		aExplorer.add(Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(), joueur.GetPosition().GetY()));
		Point pointACoutMinimal = Model.getLabyrinth().GetPoint(joueur.GetPosition().GetX(),
				joueur.GetPosition().GetY());
		while (!aExplorer.isEmpty() && !aExplorer.contains(arrivee)) {
			aExplorer.remove(pointACoutMinimal);
			visites.add(pointACoutMinimal);

			bloque = true;
			
			//if((joueur.GetPosition().GetX() != arrivee.GetX()) && (joueur.GetPosition().GetX() != arrivee.GetX())){
			if (pointACoutMinimal.GetNord() != null && !visites.contains(pointACoutMinimal.GetNord())) {
				// if(!visites.contains(pointACoutMinimal.GetNord()){
				computeGHAndPredecesseur(pointACoutMinimal.GetNord(), pointACoutMinimal);
				aExplorer.add(pointACoutMinimal.GetNord());
				System.out.println("NORD");
				bloque = false;
				// } else {

				// }
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
				//choiceAction();
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
			
		//}
		
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
