package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class breakoutMon {
	enum faculty {
		Arts, Science, Engineering
	}

	private static int n, k, w;
	private static Room room = new Room();
	private static List<Student> aStudents = new ArrayList<>();
	private static StringBuilder report= new StringBuilder();
	public static void main(String[] args) {

		n = Integer.parseInt(args[0]);
		k = Integer.parseInt(args[1]);
		w = Integer.parseInt(args[2]);
		
		createStudents();
		
		aStudents.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		System.out.println(report.toString());
		
	}

	private static void createStudents() {
		
		for (faculty f : faculty.values()) {
			for (int i = 0; i < 4; i++) {
				aStudents.add(new Student(f));
			}
		}
		aStudents.forEach(Thread::start);
	}

	static class Room {
		private static Optional<faculty> currentFaculty = Optional.empty();
		List<Student> studentsInSide = new ArrayList<>();
		List<Student> waitList = new ArrayList<>();

		public synchronized boolean requestEntry(Student aStudent) {
			// if no one is inside or if students inside are of the same faculty as aStudent
			if (currentFaculty.isEmpty() || currentFaculty.get() == aStudent.getFaculty()) {
				studentsInSide.add(aStudent);
				
				if(currentFaculty.isEmpty())report.append("|" + currentFaculty+"------>");
				
				currentFaculty = Optional.of(aStudent.getFaculty());
				
				if(currentFaculty.isEmpty())report.append(currentFaculty+"\n");
				return true;
			} else {
				waitList.add(aStudent);
				
				return false;
			}
		}

		public synchronized void requestExit(Student aStudent) {
			studentsInSide.remove(aStudent);
			// see if the room is now empty
			if (studentsInSide.size() == 0) {
				report.append("|" + currentFaculty+"------>");
				
				currentFaculty = Optional.empty();
				
				report.append(currentFaculty+"\n");
				//this counter is simply for report, and has no other use.
				int counter =0;
				// see if anyone is on the waitinglist and call any number of students of the
				// same faculty in.
				while (!waitList.isEmpty()
						&& (currentFaculty.isEmpty() || waitList.get(0).getFaculty() == currentFaculty.get())) {
					counter++;
					Student waitingStudent = waitList.remove(0);
					
					if(counter==1)report.append("|" + currentFaculty+"------>");
					currentFaculty = Optional.of(waitingStudent.getFaculty());
					if(counter==1)report.append(currentFaculty+"\n");
					
					studentsInSide.add(waitingStudent);
					synchronized (waitingStudent.lock) {
						waitingStudent.lock.notify();
					}

				}
			}
		}

	}

	static class Student extends Thread {
		public final Object lock = new Object();
		private faculty aFaculty;

		public Student(faculty pFaculty) {
			aFaculty = pFaculty;
		}

		@Override
		public void run() {
			long startTime = System.currentTimeMillis();

			while (System.currentTimeMillis() - startTime < (n * 1000)) {

				// initial sleep
				try {
					sleep(ThreadLocalRandom.current().nextInt(1, 11) * k);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// attempt to enter the room
				try {
					enter();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				// study for some time
				try {
					sleep(ThreadLocalRandom.current().nextInt(1, 11) * w);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// leave the room

				leave();

			}

		}

		private void enter() throws InterruptedException {
			if (!room.requestEntry(this)) {
				synchronized (lock) {
					lock.wait();
				}
			}
		}

		private void leave() {
			room.requestExit(this);
		}

		public faculty getFaculty() {
			return aFaculty;
		}
	}

}
