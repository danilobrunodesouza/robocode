package time;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class TesteRobo extends AdvancedRobot {
	int acertos=0;
	double distanciaInimigo;
	double anguloDoInimigo;
	
	
	@Override
	public void run() {

		while (true) {
			this.setAhead(100);
			this.setTurnLeft(45);
			this.setTurnRadarLeft(90);
			this.setTurnGunLeft(90);
			this.execute();

		}

	}

	@Override
	public void onScannedRobot(ScannedRobotEvent inimigoNaMira) {
		distanciaInimigo = inimigoNaMira.getDistance();
		
		anguloDoInimigo = inimigoNaMira.getBearing();
		
		
		if(distanciaInimigo < 200){
			fire(3);		
		} else if (distanciaInimigo > 200 && distanciaInimigo <= 325){
			fire(2);
		} else if (distanciaInimigo > 325 && distanciaInimigo < 500){
			fire(1);
		} else {
			turnRight(anguloDoInimigo);
			ahead(distanciaInimigo * 0.6);
		}
		//para mirar o canhão no adversário.

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
		setTurnGunLeft(anguloDoInimigo);
	
	}
	
	
	public double anguloRelativo(double ANG) {
		if (ANG> -180 && ANG<= 180) {
			return ANG;
		}
		double REL = ANG;
		while (REL<= -180) {
			REL += 360;
		}
		while (ANG> 180) {
			REL -= 360;
		}
		return REL;
	}
}
