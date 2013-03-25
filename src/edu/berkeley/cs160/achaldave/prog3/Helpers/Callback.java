package edu.berkeley.cs160.achaldave.prog3.Helpers;

public interface Callback <I, O> {
	public O call(I data);
}
