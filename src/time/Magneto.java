package time;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class Magneto extends AdvancedRobot {

	private double distanciaInimigo;
	private double anguloDoInimigo;

	private int acertos = 0;
	private int erros = 0;

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
		
		setTurnGunRight((relatividadeAngular(anguloDoInimigo + getHeading() - getGunHeading())));
		if (inimigo.getEnergy() < 12) {
			fire((inimigo.getEnergy() / 4) + .1);
		} else if (distanciaInimigo <= 200) {
			fire(3);
		} else if (distanciaInimigo > 200 && distanciaInimigo <= 325) {
			fire(2);
		} else if (distanciaInimigo > 325 && distanciaInimigo < 500) {
			fire(1);
		} else {
			turnRight(relatividadeAngular(anguloDoInimigo));
			ahead(distanciaInimigo * 0.3);
			fire(1);
		}

	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		acertos++;
		System.out.println("Acertos: " + acertos);
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		erros++;
		System.out.println("Erros" + erros);
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
		fireBullet(3);
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

	public void defineEstrategia(ScannedRobotEvent inimigo) {
		if(getOthers() == 1){
			double fixaMira = relatividadeAngular(inimigo.getBearing() + (getHeading() - getRadarHeading()));
			setTurnGunRight(fixaMira);
		}
		
		if (getEnergy() > 70 && inimigo.getEnergy() < 40) {
			jogaPraCimaDeles(inimigo);
		} else if ((getEnergy() > 35 && getEnergy() <= 70) || (getEnergy() <= inimigo.getEnergy())) {
			jogaNormal();
		} else {
			jogaNaRetranca();
		}
	}

	public void jogaPraCimaDeles(ScannedRobotEvent inimigo) {
		distanciaInimigo = inimigo.getDistance();
		anguloDoInimigo = inimigo.getBearing();
		
		turnRight(relatividadeAngular(anguloDoInimigo));
		ahead(distanciaInimigo * 0.4);
		tiroCerto(inimigo);
	}

	public void jogaNormal() {
		setAhead(100);
		setTurnRadarRight(90);
	}

	public void jogaNaRetranca() {
		// colocar ele proximo da parede
		while (!vaiBaterNaParede()) {
			back(100);
		}
		turnRight(90);
		setAhead(300);
		turnRight(180);
		setAhead(300);
		
	}

	public boolean vaiBaterNaParede() {
		return ((getX() < getBattleFieldWidth() - 50 || getX() > 50)
				|| (getY() < getBattleFieldHeight() - 50 || getY() > 50));
	}

}
