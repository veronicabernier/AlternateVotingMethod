package election;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import ArrayList.ArrayList;
import Set.DynamicSet;
import Set.Set;

public class Election {

	public static void main(String[] args) throws IOException {

		Set<Ballot> ballots;
		ArrayList<String> candidatesNames = new ArrayList<String>(10);
		int candidatesNum = 0;
		int totalBallots = 0;
		int blankBallots = 0;
		int invalidBallots = 0;

		File ballotsFile = new File("..\\AlternateVotingMethod\\res\\ballots.csv");
		File candidatesFile = new File("..\\AlternateVotingMethod\\res\\candidates.csv");

		try (Scanner s = new Scanner(candidatesFile, StandardCharsets.UTF_8.name())) {
			while (s.hasNextLine()) {
				String line = s.nextLine();
				candidatesNum++;
				//the name in the format are the first elements before the comma.
				candidatesNames.add(line.split(",")[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ballots = new DynamicSet<Ballot>(candidatesNum);
		
		try (Scanner s = new Scanner(ballotsFile, StandardCharsets.UTF_8.name())) {
			while (s.hasNextLine()) {
				String line = s.nextLine();
				//find if the new ballot is valid and add to set of all ballots.
				Ballot newBallot = new Ballot(line, candidatesNum);
				if(!newBallot.isValid()) {
					invalidBallots++;
				}
				else if(newBallot.isBlank()) {
					blankBallots++;
				}
				else {
					ballots.add(newBallot);
				}
				totalBallots++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Give our ballots, the number of candidates and their names to our election system.
		ElectionSystem e = new ElectionSystem(ballots, candidatesNum, candidatesNames);

		//create the output result.txt file
		File file = new File("result.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		System.out.println("Number of ballots: " + totalBallots);
		System.out.println("Number of blank ballots: " + blankBallots);
		System.out.println("Number of invalid ballots: " + invalidBallots);
		e.winnerFile(ps);
	}
}
