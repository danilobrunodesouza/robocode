package time;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class Magneto extends AdvancedRobot {

	private double distanciaInimigo;
	private double anguloDoInimigo;
	
	int acertos = 0;
	int erros = 0;
	

	@Override
	public void run() {
		setColors(Color.RED, Color.BLUE, Color.BLUE);
		while (true) {
			jogaNormal();
			this.execute();

		}

	}

	public void tiroCerto(ScannedRobotEvent inimigo) {
		distanciaInimigo = inimigo.getDistance();
		anguloDoInimigo = inimigo.getBearing();

		turnGunRight(relatividadeAngular(anguloDoInimigo + getHeading() - getGunHeading()));

		if (distanciaInimigo < 200) {
			fire(3);
		} else if (distanciaInimigo > 200 && distanciaInimigo <= 325) {
			fire(2);
		} else if (distanciaInimigo > 325 && distanciaInimigo < 500) {
			fire(1);
		} else {
			turnRight(relatividadeAngular(anguloDoInimigo));
			ahead(distanciaInimigo * 0.6);
			fire(1);
		}

	}

	@Override
	public void onScannedRobot(ScannedRobotEvent inimigoNoRadar) {
		defineEstrategia(inimigoNoRadar);
		tiroCerto(inimigoNoRadar);
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		setTurnLeft(45);
		setBack(100);
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		setTurnLeft(180);
	}

	@Override
	public void onWin(WinEvent event) {
		setTurnRight(720000);
		setTurnGunLeft(720000);
		setTurnRadarRight(72000);
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		anguloDoInimigo = event.getBearing();
		turnGunRight(relatividadeAngular(anguloDoInimigo + getHeading() - getGunHeading()));
		fire(3);
	}

	public double relatividadeAngular(double angulo) {
		if (angulo > -180 && angulo <= 180) {
			return angulo;
		}
		double miraRapida = angulo;
		while (miraRapida <= -180) {
			miraRapida += 360;
		}
		while (angulo > 180) {
			miraRapida -= 360;
		}
		return miraRapida;
	}
	
	
	public void defineEstrategia(ScannedRobotEvent inimigo){
		if(getEnergy() > 70 && inimigo.getEnergy() < 40){
			jogaPraCimaDeles(inimigo);
		} else if ((getEnergy() > 35 && getEnergy() <=70) || (getEnergy() <= inimigo.getEnergy())){
			jogaNormal();
		} else {
			jogaNaRetranca();
		}
	}
	
	public void jogaPraCimaDeles(ScannedRobotEvent inimigo) {
		distanciaInimigo = inimigo.getDistance();
		anguloDoInimigo = inimigo.getBearing();

		turnGunRight(relatividadeAngular(anguloDoInimigo + getHeading() - getGunHeading()));
		turnRight(relatividadeAngular(anguloDoInimigo));
		ahead(distanciaInimigo * 0.4);
	}
	
	public void jogaNormal(){
		this.setAhead(100);
		this.setTurnRadarRight(90);
	}
	

	public void jogaNaRetranca() {
		//colocar ele proximo da parede
		back(300);
		
	}

}
