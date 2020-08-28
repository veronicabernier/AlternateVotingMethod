package election;

import ArrayList.ArrayList;
import Set.Set;
/**
 * I use an array list of integers to store the ranks for each ballot. Each index of the array list 
 * represents the candidate and the value in that index represents the rank given to that candidate. 
 * The rank given to candidate i will be located at the index (i-1). For example, the ranks for candidate #1 are at
 * the indexes of 0. The array list allows me to quickly access what rank a candidate has within a certain ballot by
 * just calling the votes in the ballot with the candidate's index.
 * @author Veronica
 *
 */
public class Ballot {

	private int ballotNum;
	private ArrayList<Integer> votes;
	private boolean isValid;
	private boolean isBlank;

	public Ballot(String str, int candidatesNum) {
		this.isValid = true;
		//This list will contain the ranks in order of candidate number.
		ArrayList<Integer> ranksByCandidate = new ArrayList<Integer>(candidatesNum); 
		//This list will contain the ranks in increasing order for validating purposes.
		ArrayList<Integer> sortedRanks = new ArrayList<Integer>(candidatesNum);
		//Initialize all the ranks to zero so I can set the ranks on the candidates index while I find it.
		for (int i = 0; i < candidatesNum; i++) {
			ranksByCandidate.add(0);
		}
		//Made array with elements separated by commas so I can identify the ballot number and the individual votes
		String[] byComma = str.split(",");
		this.ballotNum = Integer.parseInt(byComma[0]); //the first element should be the ballot number
		for (String candAndVote : byComma) {
			// separate each string that was separated by comma by colon
			String[] byColon = candAndVote.split(":");
			// if I get two values from the Colon separation, I have the candidate number
			// and the rank
			if (byColon.length == 2) {
				// check that the voter isn't voting for an inexistent candidate
				if (Integer.parseInt(byColon[0]) > candidatesNum) {
					this.isValid = false;
				}
				// check if the voter hadn't already voted for that candidate
				else if (ranksByCandidate.get(Integer.parseInt(byColon[0]) - 1) != 0) {
					this.isValid = false;
				} 
				else {
					// I order the ranks by candidate
					ranksByCandidate.set(Integer.parseInt(byColon[0]) - 1, Integer.parseInt(byColon[1]));
					// I add the ranks I get since they are already in increasing order
					sortedRanks.add(Integer.parseInt(byColon[1]));
				}
			}
		}
		this.votes = ranksByCandidate;
		if (this.isValid) {
			this.isBlank = true;
			for (Integer integer : sortedRanks) {
				if (integer != 0) {
					this.isBlank = false;
				}
			}
			if (!this.isBlank) {
				int oneCount = 0;
				for (int i = 0; i < sortedRanks.size(); i++) {
					if (sortedRanks.get(i) == 1) {
						oneCount++;
					}
					//there must be a first choice
					if (oneCount != 1) {
						this.isValid = false;
					}
					//the rank must be a positive value that is not bigger than the number of candidates
					else if (sortedRanks.get(i) < 0 || sortedRanks.get(i) > candidatesNum) {
						this.isValid = false;
					} 
					//if it's not the last element I have to check that the ranks are not repeated 
					//since they're order the next one would be the same 
					else if (i < sortedRanks.size() - 1) {
						if (sortedRanks.get(i) != 0 && (sortedRanks.get(i) - sortedRanks.get(i + 1)) == 0) {
							this.isValid = false;
						} 
						//it is also invalid if the voters skipped a rank
						else if (Math.abs(sortedRanks.get(i) - sortedRanks.get(i + 1)) > 1) {
							this.isValid = false;
						}
					}
				}
			}
		}
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isBlank() {
		return isBlank;
	}

	public ArrayList<Integer> getVotes() {
		return votes;
	}

	// returns the ballot number
	public int getBallotNum() { 
		return ballotNum;
	}

	// returns the candidate ID with a given rank
	public int getCandidateByRank(int rank) { 
		return this.votes.firstIndex(rank);
	}

	// returns the rank of a given candidate Id.
	// inside my program the candidate Id is the original Id - 1.
	public int getRankByCandidate(int candidateId) {
		return this.votes.get(candidateId);
	}

	// returns the new #1 in the ballot after eliminating the previous one.
	public int eliminate(int candidateId, Set<Integer> eliminated) { // eliminates a candidate
		if (candidateId >= 0 && candidateId < votes.size()) {
			while (eliminated.isMember(this.getCandidateByRank(1))) {
				for (int i = 0; i < votes.size(); i++) {
					if (votes.get(i) >= 1) {
						votes.set(i, votes.get(i) - 1);
					}
				}
			}
			return this.getCandidateByRank(1);
		}
		throw new IndexOutOfBoundsException(
				"Candidate ID must be bigger than -1 and " + "equal or smaller than the number of candidates.");
	}
}
