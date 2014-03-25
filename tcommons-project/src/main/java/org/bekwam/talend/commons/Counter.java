package org.bekwam.talend.commons;

/**
 * This is an immutable object to keep track of the number of filtered and 
 * rejected records
 * 
 * In the basic scenario, numLines=numOk+numReject where numLines is equal
 * to the number of input records.
 * 
 * An alternative scenario will produce multiple rejects, one per rejected
 * rule.  In this case, numLines still equals numOk+numReject, but numLines
 * might not equal the number of input records.
 * 
 * @author Carl2
 * @version 1.0
 * @created 28-Nov-2012 10:05:33 PM
 */
final public class Counter {

	final private int numLines;
	final private int numLinesOk;
	final private int numLinesReject;

	public Counter() {
		numLines = 0;
		numLinesOk = 0;
		numLinesReject = 0;
	}
	
	public Counter(Counter addFrom, int numOk, int numReject) {
		
		if( numOk != 0 && numReject != 0 ) {
			throw new IllegalArgumentException("one of numOk and numReject must be 0");
		}
		
		numLines = addFrom.getNumLines() + (numOk+numReject);
		
		if( numOk != 0 ) {  // working w. ok param; carry over addFrom
			numLinesOk = addFrom.getNumLinesOk() + numOk;
			numLinesReject = addFrom.getNumLinesReject();
		}
		else { // working w. reject param; carry over addFrom
			numLinesOk = addFrom.getNumLinesOk();
			numLinesReject = addFrom.getNumLinesReject() + numReject;
		}
	}

	public int getNumLines() {
		return numLines;
	}

	public int getNumLinesOk() {
		return numLinesOk;
	}

	public int getNumLinesReject() {
		return numLinesReject;
	}

	@Override
	public String toString() {
		return "CounterObject [numLines=" + numLines + ", numLinesOk="
				+ numLinesOk + ", numLinesReject=" + numLinesReject + "]";
	}
	
}//end CounterObject