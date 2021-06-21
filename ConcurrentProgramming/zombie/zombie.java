package assignment1;

import java.util.ArrayList;
import java.util.Random;

public class zombie {
	//-----------------Variables for Main Thread, Not shared.-------------
	private static long runTime = 60000;
	private static long startTime;
	private static int aZombieNum = 0;
	private static int aZombieKilled = 0;
	private static boolean aWaveStopped = false;
	private final static ArrayList<Friend> aFriends = new ArrayList<>();
	private final static Random rand = new Random();

	//-----------------Main Thread ------------------------
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		assert k >= 1 && n >= 2;
		startTime = System.currentTimeMillis();
		// create and start all k friends
		createStart(k, n);
		// fight zombies with limit n
		fightZombies(n);
		// if all threads successfully terminate, exit with results. 
		exit(System.currentTimeMillis() - startTime);
	}

	private static void fightZombies(int n) {
		// No zombie is reported yet so sleep
		mainPause(1000);
		do {
			aZombieNum = aFriends.stream().mapToInt(pFriend -> pFriend.count()).sum();
			// check to see if friends should stop letting in zombies
			if (aZombieNum >= n) {
				aFriends.forEach(Friend::stopZombie);
				aWaveStopped = true;
			} else if (aZombieNum < n / 2 && aWaveStopped == true) {
				aFriends.forEach(Friend::letInZombie);
				aWaveStopped = false;
			} else if (aZombieNum >= n / 2 && aWaveStopped == true) {
			} else
				aFriends.forEach(Friend::letInZombie);

			long intervalStart = System.currentTimeMillis();
			// shoot every 10 ms and check every 1 s ;
			while (System.currentTimeMillis() - intervalStart <= 1000) {
				shoot();
				mainPause(10);
			}
		} while (System.currentTimeMillis() - startTime < runTime);
		aFriends.forEach(Friend::end);
		aFriends.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private static void mainPause(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void createStart(int k, int n) {
		for (int i = 0; i < k; i++) {
			aFriends.add(new Friend());
		}
		System.out.println("Running zombie with " + k + " friend(s) and limit " + n);
		aFriends.forEach(Friend::start);
	}

	private static void shoot() {
		if (rand.nextInt(10) <= 3 && aZombieNum > 0) {
			aZombieNum--;
			aZombieKilled++;
		}
	}

	private static void exit(long totalTime) {
		System.out.println("All threads successfully terminate.");
		System.out.println("Run Time: " + totalTime + "ms");
		System.out.println("Zombies killed: " + aZombieKilled);
		System.out.print("kill rate: " + ((double) aZombieKilled / totalTime * 1000.) + " zombies/s");

	}

}

class Friend extends Thread {
	private final static Random rand = new Random();
	private volatile int aZombieIn = 0;
	private volatile boolean aPauseFlag = false;
	private volatile boolean aEndFlag = false;

	@Override
	public void run() {
		while (!aEndFlag) {
			try {
				if (aPauseFlag == false && rand.nextInt(10) == 0) {
					aZombieIn++;
				}
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	synchronized int count() {
		aPauseFlag = true;
		int temp = aZombieIn;
		aZombieIn = 0;
		return temp;
	}

	synchronized void stopZombie() {
		aPauseFlag = true;
	}

	synchronized void letInZombie() {
		aPauseFlag = false;
	}

	synchronized void end() {
		aEndFlag = true;
	}

}