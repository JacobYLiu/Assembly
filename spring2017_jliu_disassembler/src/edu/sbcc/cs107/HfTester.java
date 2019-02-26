package edu.sbcc.cs107;

import java.io.FileNotFoundException;

public class HfTester {

	public static void main(String[] args) throws FileNotFoundException {
		HexFile hf = new HexFile("sample1.hex");

		hf.getNextHalfword().getAddress();
		hf.getNextHalfword().getData();
		return;
		
	}

}
