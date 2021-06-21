package assignment3;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import java.util.regex.Matcher;

public class q1 {

	private static int t;
	private static Random rand = new Random();
	private static final int STRING_LENGTH = 300000;
	private static String text;
	private static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', 'a' };
	private static DFA masterDFA = new DFA();
	private static volatile ArrayList<Normal> threads = new ArrayList<>();

	private static Pattern zero = Pattern.compile("0");
	private static Pattern dot = Pattern.compile("\\.");
	private static Pattern oneToNine = Pattern.compile("[1-9]");
	private static Pattern zeroToNine = Pattern.compile("[0-9]");
	private static Pattern sequential = Pattern.compile("(0|[1-9][0-9]*)\\.[0-9]+");
	private static ConcurrentLinkedQueue<Normal> a = new ConcurrentLinkedQueue<>();

	public static void main(String[] args) {
		init(args);
		long startTime = System.currentTimeMillis();
		threads.forEach(Thread::start);

		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		long endTime = System.currentTimeMillis();
		threads.forEach(a -> System.out.println(a));
		
		System.out.println("Total run time " + (endTime-startTime) + " ms");
	}

	/**
	 * @param args it does everything that needs to be done before the process
	 *             actually started including 1.randomly generate text to be
	 *             matched, and print it out 2.divide the text and initialize all
	 *             threads with their portion of workload
	 */
	private static void init(String[] args) {
		assert args.length > 0 && t >= 0;
		t = Integer.parseInt(args[0]);
		if (args.length >= 2)
			rand = new Random(Long.parseLong(args[1]));
		text = randString();
		int taskLength = (int) Math.ceil(STRING_LENGTH / (1.0 + t));
		System.out.println(text);
		System.out.println();
		threads.add(new Normal(0, text.substring(0, taskLength)));
		for (int i = 1; i <= t; i++) {
			if (i == t)
				threads.add(new Optimistic(i, text.substring(i * taskLength)));
			else
				threads.add(new Optimistic(i, text.substring(i * taskLength, (i + 1) * taskLength)));
		}
		threads.forEach(a -> System.out.println(a));
	}

	/**
	 * @return a String of length STRING_LENGTH randomly selected from chars
	 */
	private static String randString() {
		StringBuilder builder = new StringBuilder();
		rand.ints(STRING_LENGTH, 0, chars.length).forEach(i -> builder.append(chars[i]));
		return builder.toString();
	}

	public enum DFAState {
		start, q1, q2, q3, accept, NullState;

		public boolean isAccepted() {
			return this == accept;
		}

		public boolean isNull() {
			return this == NullState;
		}

		public DFAState transition(char c) {
			switch (this) {
			case accept:
				if (zeroToNine.matcher(Character.toString(c)).matches())
					return accept;
				else
					return start;
			case q1:
				if (dot.matcher(Character.toString(c)).matches())
					return q3;
				else
					return start;
			case q2:
				if (dot.matcher(Character.toString(c)).matches())
					return q3;
				else if (zeroToNine.matcher(Character.toString(c)).matches())
					return q2;
				else
					return start;
			case q3:
				if (zeroToNine.matcher(Character.toString(c)).matches())
					return accept;
				else
					return start;
			case start:
				if (zero.matcher(Character.toString(c)).matches())
					return q1;
				else if (oneToNine.matcher(Character.toString(c)).matches())
					return q2;
				else
					return start;
			default:
				return NullState;
			}
		}

	}

	static class DFA {
		public DFAState finalState(DFAState s, char[] input) {
			DFAState result = s;
			for (char c : input) {
				result = result.transition(c);
				if(result == DFAState.start)result = DFAState.start.transition(c);
			}
			return result;
		}

		public String startDFA(DFAState s, char[] input, boolean keepEnd) {
			StringBuilder buffer = new StringBuilder();
			StringBuilder result = new StringBuilder();
			DFAState current = s;
			for (char c : input) {
				buffer.append(c);
				current = current.transition(c);
				if (current.isAccepted()) {
					result.append(buffer.toString());
					buffer = new StringBuilder();
				}
				if (current == DFAState.start) {
					if(DFAState.start.transition(c)!=DFAState.start) {
						for (int i = 0; i < buffer.length()-1; i++) result.append("_");
						buffer = new StringBuilder();
						buffer.append(c);
						current = DFAState.start.transition(c);
					}else {
						for (int i = 0; i < buffer.length(); i++) result.append("_");
					    buffer = new StringBuilder();
					}
				}
			}
			if (keepEnd) {
				result.append(buffer.toString());
			} else {
				for (int i = 0; i < buffer.length(); i++)
					result.append("_");
			}
			return result.toString();
		}

		public DFAState End_Converge(char[] input) {
			Set<DFAState> possibleLast = new HashSet<>();
			for (DFAState aState : DFAState.values()) {
				possibleLast.add(masterDFA.finalState(aState, input));
				if (possibleLast.size() > 1)
					return DFAState.NullState;
			}
			return possibleLast.iterator().next();
		}

		public Optional<Boolean> decide_keepEnd(DFAState s, char[] input, boolean last) {
			if (s.isAccepted())
				return Optional.of(true);
			if (s == DFAState.start)
				return Optional.of(false);

			DFAState current = s;
			for (char c : input) {
				current = current.transition(c);
				if (s.isAccepted())
					return Optional.of(true);
				if (s == DFAState.start)
					return Optional.of(false);
			}
			if (last)
				return Optional.of(false);
			return Optional.empty();
		}
	}

	static class Normal extends Thread {
		int ID;
		char[] aTask;
		DFAState endState = DFAState.NullState;
		Object keepEndLock = new Object();
		volatile Optional<Boolean> keepEnd = Optional.empty();

		public Normal(int ID, String pTask) {
			this.ID = ID;
			aTask = pTask.toCharArray();
			if (t == 0 ) keepEnd = Optional.of(false);
		}

		@Override
		public void run() {
			if(t!=0) {
			endState = masterDFA.finalState(DFAState.start, aTask);
			writeToNext_Notify(endState);
			
			// wait until this thread has been told whether or nor to keep its end.
			synchronized (keepEndLock) {
				while (keepEnd.isEmpty()) {
					try {
						keepEndLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			}
			// finally this thread has everything it needs to do its job
			aTask = masterDFA.startDFA(DFAState.start, aTask, keepEnd.get()).toCharArray();
			System.out.println("Normal Thread Successfully terminate.");
		}

		@Override
		public String toString() {
			return String.valueOf(aTask);
		}

		Optimistic nextThread() {
			assert ID <= t;
			return (Optimistic) threads.get(ID + 1);
		}

		void writeToNext_Notify(DFAState write) {
			Optimistic nextThread = nextThread();
			synchronized (nextThread.initialStateLock) {
				nextThread.initialState = write;
				nextThread.initialStateLock.notify();
			}
		}

	}

	static class Optimistic extends Normal {
		private volatile DFAState initialState = DFAState.NullState;
		private final Object initialStateLock = new Object();
		// private EnumMap<DFAState, Supplier<String>> aStateMap = new
		// EnumMap<>(DFAState.class);

		public Optimistic(int ID, String pTask) {
			super(ID, pTask);
			if(ID == t) {
				keepEnd = Optional.of(false);
			}
		}

		Normal prevThread() {
			return threads.get(ID - 1);
		}

		void updatePrev_Notify(Optional<Boolean> write) {
			Normal prev = prevThread();
			synchronized (prev.keepEndLock) {
				prev.keepEnd = write;
				prev.keepEndLock.notify();
			}
		}

		@Override
		public void run() {
			endState = masterDFA.End_Converge(aTask);
			if (!endState.isNull()) {
				if (this.ID != t) {
					writeToNext_Notify(endState);
				}
			}
			// wait till this thread receives its initial state
			synchronized (initialStateLock) {
				while (initialState.isNull()) {
					try {
						System.out.println("Thread ID: "+ID+" waiting");
						initialStateLock.wait();
						System.out.println("Thread ID: "+ID+" awoken");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			// if previously, mappings don't converge, write to next now
			if (endState.isNull()) {
				endState = masterDFA.finalState(initialState, aTask);
				if ( this.ID !=t) {
				writeToNext_Notify(endState);
				}
			}
			// tell the previous thread if it needs to keep its end
			Optional<Boolean> prevkeepEnd;
			if (ID == t)
				prevkeepEnd = masterDFA.decide_keepEnd(initialState, aTask, true);
			else
				prevkeepEnd = masterDFA.decide_keepEnd(initialState, aTask, false);
			if (prevkeepEnd.isEmpty()) {
				// this only happens when this thread aren't even sure whether or not to keep
				// its end
				synchronized (this.keepEndLock) {
					while (keepEnd.isEmpty()) {
						try {
							keepEndLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					updatePrev_Notify(keepEnd);

				}
			} else {
				updatePrev_Notify(prevkeepEnd);
			}

			// wait until this thread has been told whether or nor to keep its end.
			synchronized (keepEndLock) {
				while (keepEnd.isEmpty()) {
					try {
						keepEndLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			// finally this thread has everything it needs to do its job

			aTask = masterDFA.startDFA(initialState, aTask, keepEnd.get()).toCharArray();
			System.out.println("Optimisitc Thread " + ID + " successfully terminates.");
		}
	}
}
