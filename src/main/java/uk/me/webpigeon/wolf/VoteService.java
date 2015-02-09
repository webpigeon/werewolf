package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VoteService <T> {
	private Map<T, T> votes;
	private Map<T, Integer> totals;
	private Collection<T> voteTokens;
	private boolean suddenDeath;
	
	public VoteService(Collection<T> voteTokens) {
		this.votes = new HashMap<T, T>();
		this.totals = new HashMap<T, Integer>();
		this.voteTokens = voteTokens;
		this.suddenDeath = false;
	}
	
	public void setSuddenDeath(boolean t) {
		this.suddenDeath = t;
	}
	
	public synchronized void vote(T voter, T votee) {
		T currentVote = votes.get(voter);
		if (currentVote != null) {
			int totalVotes = getVotesFor(currentVote);
			totals.put(currentVote, totalVotes-1);
		}
		
		votes.put(voter, votee);
		totals.put(votee, getVotesFor(votee) + 1);
	}
	
	public synchronized int getVotesFor(T votee) {
		Integer voteTotal = totals.get(votee);
		return voteTotal==null?0:voteTotal;
	}

	public synchronized boolean isFinished() {		
		return votes.size() == voteTokens.size() || suddenDeath;
	}

	public synchronized T getResult() {
		return getHighest();
	}
	
	private T getHighest() {
		T bestVote = null;
		int voteCount = 0;
		
		for (Entry<T, Integer> currentEntry : totals.entrySet()) {
			if (currentEntry.getValue() > voteCount) {
				bestVote = currentEntry.getKey();
				voteCount = currentEntry.getValue();
			}
		}
		
		return bestVote;
	}

}
