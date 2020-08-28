package election;

import java.io.PrintStream;

import ArrayList.ArrayList;
import Set.DynamicSet;
import Set.Set;

/**
 * My election system receives a set of all the valid ballots that will be used to determine the winner.
 * The program divides each of those ballots in sets depending on which candidate is ranked at #1 in that ballot. 
 * This way, when I eliminate a candidate, I only go through that set and rearrange those ballots.
 * 
 * @author Veronica
 *
 */
public class ElectionSystem {

	private Set<Ballot> ballots;
	private int n; // candidate count
	private Set<Integer> eliminatedIds;
	ArrayList<String> candidateNames;

	public ElectionSystem(Set<Ballot> ballots, int n, ArrayList<String> candidateNames) {
		this.ballots = ballots;
		this.n = n;
		this.eliminatedIds = new DynamicSet<Integer>(n-1); //stored eliminated candidates
		this.candidateNames = candidateNames;
	}

	//returns a set of all the ballots the given candidate has the first rank at.
	public Set<Ballot> firstChoiceAt(Set<Ballot> ballots, int candidateId) {
		Set<Ballot> firstOption = new DynamicSet<Ballot>(ballots.size());
		for (Ballot b : ballots) {
			if(b.getCandidateByRank(1) == candidateId) {
				firstOption.add(b);
			}
		}
		return firstOption;
	}

	//counts the ballots where the candidate has the given rank n.
	public int nChoiceCount(Set<Ballot> ballots, int n, int candidateId) {
		int optionN = 0;
		for (Ballot b : ballots) {
			if(b.getCandidateByRank(n) == candidateId) {
				optionN++;
			}
		}
		return optionN;
	}

	//returns an Array List of the sets of each candidate where they have the first rank.
	public ArrayList<Set<Ballot>> ballotStorage() {
		ArrayList<Set<Ballot>> votes = new ArrayList<Set<Ballot>>(this.n);
		for (int i = 0; i < n; i++) {
			votes.add(this.firstChoiceAt(ballots, i));
		} 
		return votes;
	}

	//given an array of candidates who are in danger with the less amount of #1's, 
	//this method looks through all the ballots to find how many #2's, #3's... they have
	//and it removes from danger the candidate that has more of that number each round until there only
	//is one candidate or it returns the one left with the highest ID.
	public int unTieArray(ArrayList<Integer> inDanger, ArrayList<Set<Ballot>> votes) {
		int countIndex, countNext;
		int nVoteType = 2;
		while(nVoteType <= n) { //they are only ranks from 1 to n
			for (int i = 0; i < inDanger.size() - 1; i++) {
				if(inDanger.size() != 1) { //need them to only be one left to return
					countIndex = this.nChoiceCount(this.ballots, nVoteType, inDanger.get(i));
					countNext = this.nChoiceCount(this.ballots, nVoteType, inDanger.get(i+1));
					if(countIndex > countNext) {
						//we remove the ones that are safe from the list
						inDanger.removeByIndex(i); 
						i--;
					}
					else if(countIndex < countNext) {
						inDanger.removeByIndex(i+1); 
						i--;
					}
				}
			}
			nVoteType++;
		}
		return inDanger.last(); //the one at the end has the highest Id so we eliminate it if they're still tied.
	}

	//returns which candidates have the less amount of #1's.
	public ArrayList<Integer> lessOnes(ArrayList<Set<Ballot>> votes) {
		//list of candidates with lowest amount of ones
		ArrayList<Integer> withLowestOnes = new ArrayList<Integer>(n); 
		int lessOnesNum = ballots.size(); //less amount of 1's
		for (int i = 0; i < votes.size(); i++) {
			if(!eliminatedIds.isMember(i)) {
				if(votes.get(i).size() < lessOnesNum) {
					//we find the minimum
					lessOnesNum = votes.get(i).size();
				}
			}
		}
		//checks which has the same as the minimum to add.
		for (int i = 0; i < votes.size(); i++) {
			if(!eliminatedIds.isMember(i)) {
				if(votes.get(i).size() == lessOnesNum) {
					withLowestOnes.add(i);
				}
			}
		}
		return withLowestOnes;
	}

	public int elimination(ArrayList<Set<Ballot>> votes) {
		ArrayList<Integer> lessOnes = this.lessOnes(votes);
		int newEliminated;
		//if there is only one with the lowest amount, we can just eliminate that one
		if(lessOnes.size() == 1) {
			newEliminated = lessOnes.get(0);
		}
		else { 
			//need to untie ALL the ones with less # of 1's
			newEliminated = unTieArray(lessOnes, votes);
		}
		return newEliminated; 	
	}

	public void winnerFile(PrintStream ps) {
		System.setOut(ps);
		int roundNum = 0;
		int winner = -1; //This candidate does not exist.
		ArrayList<Set<Ballot>> votes = this.ballotStorage(); 
		while(roundNum < n) { //we need at most n-1 rounds to have only one candidate left (the winner)
			roundNum++;
			for (int i = 0; i < n ; i++) {
				if(!eliminatedIds.isMember(i)) {
					if(((double) votes.get(i).size()/ (double) ballots.size()) > 0.50) { //wins if has more than 50% of 1's
						winner = i;
						System.out.println("Winner: " + candidateNames.get(winner) +" wins with " + votes.get(winner).size() +" #1's");
						return;
					}
				}
			}
			//eliminates and adds the id to set of eliminated.
			int eliminated = elimination(votes);
			System.out.println("Round " + roundNum + ": " + candidateNames.get(eliminated) + " was eliminated with " + votes.get(eliminated).size() + " #1's");
			this.eliminatedIds.add(eliminated);

			//I took the votes where the eliminated candidate was #1 to rearrange.
			Set<Ballot> votesToFix = votes.get(eliminated);
			//Here, I loop through each ballot, rearrange it and then add it to the 
			//new #1's set.
			for (Ballot vtf : votesToFix) {
				int newOne = vtf.eliminate(eliminated, this.eliminatedIds);
				(votes.get(newOne)).add(vtf);
			}
		}
		return; 
	}
}
