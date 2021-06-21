package assignment2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class checkers {

	private static Board board;
	private static Tile[][] tiles;
	private static int t;
	private static int k;
	private static int n;
	// Randomly Picked, assuming that (0,0) is black and color alternates
	// 0 --> Black, 1 --> White
	private static int color;
	public static void main(String[] args) {

		// number of threads
		t = Integer.parseInt(args[0]);
		// wait time
		k = Integer.parseInt(args[1]);
		// moves
		n = Integer.parseInt(args[2]);

		assert k > 0 && n > 0;

		board = new Board(t);
		tiles = board.aTiles;
		board.startAll();
	}

	static class Tile {
		private int x;
		private int y;
		 
		private volatile Pawn aPawn = Pawn.nullPawn;
		public ReadWriteLock aReadWriteLock = new ReentrantReadWriteLock();

		public Tile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void put(Pawn pPawn) {
			aReadWriteLock.writeLock().lock();
			aPawn = pPawn;
			aReadWriteLock.writeLock().unlock();
		}

		public boolean isOccupied() {
			aReadWriteLock.readLock().lock();
			boolean isOccupied = !aPawn.isNull();
			aReadWriteLock.readLock().unlock();
			return isOccupied;
		}

		public static boolean isValid(int x, int y) {
			return x >= 0 && x <= 7 && y >= 0 && y <= 7;
		}

		public void pawnCaptured() {
			aPawn.captured();
		}
	}

	static class Board {
		public Tile[][] aTiles = new Tile[8][8];
		private List<Pawn> aPawns = new ArrayList<>();

		/**
		 * @param pawnNum
		 * @pre pawnNum >= 2 && pawnNum <31
		 */
		public Board(int pawnNum) {
			assert pawnNum >= 2 && pawnNum < 31;
			for (int i = 0; i < aTiles.length; i++) {
				for (int j = 0; j < aTiles[i].length; j++) {
					aTiles[i][j] = new Tile(i, j);
				}
			}
			populate(pawnNum);
		}

		/**
		 * Helper method that populates the Board at the beginning with pawnNum amount
		 * of pawns
		 * 
		 * @param pawnNum
		 */
		private void populate(int pawnNum) {
			// First, Randomly Pick a Color, assuming that (0,0) is black and color
			// alternates
			// 0 --> Black, 1 --> White
			color = ThreadLocalRandom.current().nextInt(2);
			// Decide the nth White/Black Tile to put pawns on
			// The decision is made on the observation that every row contains exactly 4
			// tiles of each color
			// and the color of first tile of each row depends on parity
			// note it is indexed starting at 0 to 31;
			ThreadLocalRandom.current().ints(0, 32).distinct().limit(pawnNum).forEach(i -> {
				// if the row starts with black tile
				if ((i / 4) % 2 == 0) {
					Pawn pawn = new Pawn(aTiles[i / 4][i % 4 * 2 + color]);
					aTiles[i / 4][i % 4 * 2 + color].put(pawn);
					aPawns.add(pawn);
				}
				// else if the row starts with white tile
				else {
					Pawn pawn = new Pawn(aTiles[i / 4][i % 4 * 2 + (1 - color)]);
					aTiles[i / 4][i % 4 * 2 + (1 - color)].put(pawn);
					aPawns.add(pawn);
				}

			});
		}

		private void startAll() {
			aPawns.forEach(Thread::start);
		}

	}

	static class Pawn extends Thread {
		int numMoves = n;
		private volatile boolean captured = false;
		Tile myTile;
		public static Pawn nullPawn = new Pawn(null) {
			@Override
			public boolean isNull() {
				return true;
			}
		};

		public boolean isNull() {
			return false;
		}

		public Pawn(Tile pTile) {
			myTile = pTile;
		}

		@Override
		public void run() {
			System.out.println(
					"T" + currentThread().getId() + ": starts at (" + myTile.x + "," + myTile.y + ")");
			
			
			while (numMoves != 0 ) {
				if(captured) {
					respawn();
				}
				
				// initial sleep
				try {
					sleep(k);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// if randMove is not empty then whatever tile inside is already locked, and has
				// to be unlocked once finish moving
				Optional<Tile> randMove = findMove();

				if (randMove.isEmpty()) {
					// sleep and try again
					continue;
				} else {
					// determine if it's a simple move or a capture
					// Note: the original myTile is already cleared inside findMove()
					int dx = randMove.get().x - myTile.x;

					myTile = randMove.get();
					myTile.put(this);
					numMoves--;
					randMove.get().aReadWriteLock.writeLock().unlock();
					
					if (Math.abs(dx) == 1) {
						// Move
						System.out.println(
								"T" + currentThread().getId() + ": moves to (" + myTile.x + "," + myTile.y + ")");
					} else if (Math.abs(dx) == 2) {
						// Capture
						System.out.println(
								"T" + currentThread().getId() + ": captures (" + myTile.x + "," + myTile.y + ")");
					}
				}
			}
			myTile.put(nullPawn);
			System.out.println("T" + currentThread().getId() + ": exits at ("+ myTile.x + "," + myTile.y + ")");
			

		}

		public void captured() {
			captured = true;

		}

		private ArrayList<Tile> diagonals() {
			ArrayList<Tile> result = new ArrayList<>();
			if (Tile.isValid(myTile.x - 1, myTile.y - 1))
				result.add(tiles[myTile.x - 1][myTile.y - 1]);
			if (Tile.isValid(myTile.x + 1, myTile.y - 1))
				result.add(tiles[myTile.x + 1][myTile.y - 1]);
			if (Tile.isValid(myTile.x - 1, myTile.y + 1))
				result.add(tiles[myTile.x - 1][myTile.y + 1]);
			if (Tile.isValid(myTile.x + 1, myTile.y + 1))
				result.add(tiles[myTile.x + 1][myTile.y + 1]);
			return result;
		}

		/**
		 * find a move for the pawn on MyTile
		 * 
		 * @return an Optional of the target tile, which will be locked, and must be
		 *         unlocked in another method, which is run() in this case. if it's
		 *         empty then no move possible and nothing is locked.
		 */
		private Optional<Tile> findMove() {
			ArrayList<Tile> available = diagonals();
			while (available.size() > 0) {
				// randomly pick a diagonal to try
				Collections.shuffle(available);
				Tile randPick = available.get(0);
				// lock that tile so no one else is trying to read or write to it

				// simple move
				if (!randPick.isOccupied()) {
					// The case where there is no competition for this free tile/ or this thread won
					// the competition
					if (randPick.aReadWriteLock.writeLock().tryLock()) {
						// immediately remove this thread from its tile since it has secured the target
						// tile
						// although this means threads reading before this write will miss an
						// opportunity to
						// move but it is fine because they can simply try again.
						// This action cannot be block because the tile is  previously occupied by this thread
						myTile.put(nullPawn);
						// retain the lock for randPick since we need to move to it(make a write)
						return Optional.of(randPick);
					}
					else {
						int dx = randPick.x - myTile.x;
						int dy = randPick.y - myTile.y;

						// check capture
						if (Tile.isValid(randPick.x + dx, randPick.y + dy)) {

							if (!tiles[randPick.x + dx][randPick.y + dy].isOccupied()) {
								// again check for competition/compete
								if (tiles[randPick.x + dx][randPick.y + dy].aReadWriteLock.writeLock().tryLock()) {
									myTile.put(nullPawn);
									randPick.pawnCaptured();
									// again writeLock is retained for run()
									return Optional.of(tiles[randPick.x + dx][randPick.y + dy]);
								}

							}
						}
					}
					// so this thread cannot move to randPick, nor can it capture the thread moving
					// to randPick.
					// try again with other diagonals
					available.remove(0);
					continue;

				}
				// check if a normal capture is possible
				else {
					int dx = randPick.x - myTile.x;
					int dy = randPick.y - myTile.y;

					// check capture
					if (Tile.isValid(randPick.x + dx, randPick.y + dy)) {

						if (!tiles[randPick.x + dx][randPick.y + dy].isOccupied()) {
							// again check for competition/compete
							if (tiles[randPick.x + dx][randPick.y + dy].aReadWriteLock.writeLock().tryLock()) {
								myTile.put(nullPawn);
								randPick.pawnCaptured();
								// again writeLock is retained for run()
								return Optional.of(tiles[randPick.x + dx][randPick.y + dy]);
							}

						}
					}

					// No possible normal capture in this direction, try again
					available.remove(0);
				}
			}

			// No move can be made of all directions
			return Optional.empty();
		}
		
		private void respawn() {
			//clear MyTile
			myTile.put(nullPawn);
			System.out.println(
					"T" + currentThread().getId() + ": captured.");
			
			while(captured) {
				int index = ThreadLocalRandom.current().nextInt(0,32);
				
				// if the row starts with black tile
				if ((index / 4) % 2 == 0) {
					
					if(!tiles[index / 4][index % 4 * 2 + color].isOccupied()) {
						if(tiles[index / 4][index % 4 * 2 + color].aReadWriteLock.writeLock().tryLock()) {
							this.myTile= tiles[index / 4][index % 4 * 2 + color];
							tiles[index / 4][index % 4 * 2 + color].put(this);
							tiles[index / 4][index % 4 * 2 + color].aReadWriteLock.writeLock().unlock();
							captured=false;
							System.out.println("T" + currentThread().getId() + ": respawning at ("+ 
									(index/4) + "," + (index % 4 * 2 + color) +")" );
							break;
							
						}
					}
					
				}
				// else if the row starts with white tile
				else {
					if(!tiles[index / 4][index % 4 * 2 + (1 - color)].isOccupied()) {
						if(tiles[index / 4][index % 4 * 2 + (1 - color)].aReadWriteLock.writeLock().tryLock()) {
							this.myTile= tiles[index / 4][index % 4 * 2 + (1 - color)];
							tiles[index / 4][index % 4 * 2 + (1 - color)].put(this);
							tiles[index / 4][index % 4 * 2 + (1 - color)].aReadWriteLock.writeLock().unlock();
							captured=false;
							System.out.println("T" + currentThread().getId() + ": respawning at ("+ 
									(index/4) + "," + (index % 4 * 2 + (1 - color)) +")" );
							break;
						}
					}
					
				}
			}
			
		}
	}
}
