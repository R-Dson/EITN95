import java.util.Random;

public class Person {
	private double x;
	private double y;
	private int reset;
	private Random random;
	private int direction;
	private int speed;
	public int id;
	public double[] meetings;
	private static final int NORTH = 0, NORTHWEST = 1, WEST = 2, SOUTHWEST = 3, SOUTH = 4, SOUTHEAST = 5, EAST = 6,
			NORTHEAST = 7;

	public Person(int speed, int id) {
		this.id = id;
		this.speed = speed;
		random = new Random();
		x = random.nextInt(20) + 0.5;
		y = random.nextInt(20) + 0.5;
		changeDirection();
		meetings = new double[20];
	}

	public void move() {
		double oldx = x;
		double oldy = y;
		switch (direction) {
			case NORTH:
				y++;
				break;
			case NORTHWEST:
				y++;
				x--;
				break;
			case WEST:
				x--;
				break;
			case SOUTHWEST:
				x--;
				y--;
				break;
			case SOUTH:
				y--;
				break;
			case SOUTHEAST:
				x++;
				y--;
				break;

			case EAST:
				x++;
				break;
			case NORTHEAST:
				x++;
				y++;
				break;
		}
		if (x < 0 || x > 20 || y < 0 || y > 20) {// out of bounds
			x = oldx;
			y = oldy;
			reset = 1;
		}

	}

	public void center() {
		reset--;
		if (reset == 0) {
			changeDirection();
		}

	}

	private void changeDirection() {
		reset = random.nextInt(10) + 1;
		direction = random.nextInt(8);
	}

	public boolean sameSquare(Person p) {
		return (x == p.x && y == p.y && (p.id != id));
	}

	public void meeting(int id, double delay) {
		meetings[id] += delay;
	}

	public double timeto() {
		if (direction == NORTHEAST || direction == NORTHWEST || direction == SOUTHWEST || direction == SOUTHEAST) {
			return Math.sqrt(1.0 / 2) / speed;
		} else {
			return (1.0 / 2) / speed;
		}

	}

	public String toString() {
		return x + " " + y + " " + direction + " " + reset;
	}

	public boolean done() {
		for (int i = 0; i < 20; i++) {
			if (meetings[i] == 0.0 && i != id) {
				return false;
			}

		}
		return true;
	}

	public String printMeetings() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 20; i++) {
			if (i != id) {
				sb.append((int)meetings[i] + "\t");
			}
			

		}
		return sb.toString();
	}
}
