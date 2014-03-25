package org.bekwam.talend.component.scriptrules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import org.bekwam.talend.commons.Counter;

/**
 * @author Carl2
 * @version 1.0
 * @created 28-Nov-2012 10:05:19 PM
 */
final public class Result {

	final private Object inputRow;
	final private Object filterRow;
	final private Object rejectRow;
	final public List<Loopable> loopables;
	final public Counter counter;

	public Result(Object inputRow, 
						Object filterRow, 
						Object rejectRow, 
						List<Loopable> loopables, 
						Counter counter) {
		
		this.inputRow = inputRow;
		this.filterRow = filterRow;
		this.rejectRow = rejectRow;
		this.loopables = loopables;
		this.counter = counter;
	}

	public Object getInputRow() {
		return inputRow;
	}

	public Object getFilterRow() {
		return filterRow;
	}

	public Object getRejectRow() {
		return rejectRow;
	}

	public List<Loopable> getLoopables() {
		return loopables;
	}

	public Counter getCounter() {
		return counter;
	}
	
	public boolean success() {
		return CollectionUtils.isNotEmpty(loopables) && loopables.get(0) instanceof Success;
	}
	
}//end ResultObject