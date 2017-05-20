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

//ajustar a mira
public class Magneto extends AdvancedRobot {

	private double distanciaInimigo;
	private double anguloDoInimigo;
	private double energiaDoInimigo;

	private int acertos = 0;
	private int erros = 0;

	@Override
	public void run() {
		setColors(defineCores(108, 12, 1), defineCores(50, 34, 103), defineCores(50, 34, 103));
		setScanColor(defineCores(108, 12, 1));
		setBulletColor(defineCores(182, 250, 251));
		while (true) {
			jogaNormal();
			this.execute();
		}

	}

	public Color defineCores(int r, int g, int b) {
		return new Color(r, g, b);
	}

	public void miraNoInimigo(ScannedRobotEvent inimigo) {
		anguloDoInimigo = inimigo.getBearing();
		setTurnGunRight((relatividadeAngular(anguloDoInimigo + getHeading() - getGunHeading())));
	}

	public void tiroCerto(ScannedRobotEvent inimigo) {
		distanciaInimigo = inimigo.getDistance();
		energiaDoInimigo = inimigo.getEnergy();

		if (energiaDoInimigo < 12 && distanciaInimigo < 300) {
			fire((inimigo.getEnergy() / 4) + .1);
		} else if (distanciaInimigo <= 150) {
			fireBullet(3);
		} else if (distanciaInimigo > 150 && distanciaInimigo <= 225) {
			fire(2);
		} else if (distanciaInimigo > 225 && distanciaInimigo < 300) {
			fire(1);
		} else {
			turnRight(relatividadeAngular(anguloDoInimigo));
			setAhead(distanciaInimigo * 0.3);
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
		// defineEstrategia(inimigoNoRadar);
		miraNoInimigo(inimigoNoRadar);
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
		setBack(100);
		setTurnRight(90);
		fireBullet(3);
	}

	public double relatividadeAngular(double angulo) {
		if (angulo >= -180 && angulo <= 180) {
			return angulo;
		}
		double miraRapida = angulo;
		if (miraRapida >  180) miraRapida -= 180;
		if (miraRapida < -180) miraRapida += 180;
		return miraRapida;

	}

	/*
	 * public void defineEstrategia(ScannedRobotEvent inimigo) { if (getEnergy()
	 * > 55 && inimigo.getEnergy() < 30 && getOthers() == 1) {
	 * jogaPraCimaDeles(inimigo); } else if ((getEnergy() > 35 && getEnergy() <=
	 * 70) || (getEnergy() <= inimigo.getEnergy())) { jogaNormal(); } else {
	 * jogaNaRetranca(); } }
	 */

	public void jogaPraCimaDeles(ScannedRobotEvent inimigo) {
		distanciaInimigo = inimigo.getDistance();
		anguloDoInimigo = inimigo.getBearing();

		turnRight(relatividadeAngular(anguloDoInimigo));
		ahead(distanciaInimigo * 0.4);
	}

	public void jogaNormal() {
		setAhead(100);
		setTurnRadarRight(360);
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
		return ((getX() > getBattleFieldWidth() - 50 || getX() < 50)
				|| (getY() > getBattleFieldHeight() - 50 || getY() < 50));
	}

}
