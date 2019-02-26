package edu.sbcc.cs107;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Jacob Liu CS 107: Disassembler Project
 *
 *         This code implements working with a Hex file. The hex file format is
 *         documented at http://www.keil.com/support/docs/1584/
 */
public class HexFile {

	/**
	 * This is where you load the hex file. By making it an ArrayList you can easily
	 * traverse it in order.
	 */
	private ArrayList<String> hexFile = null;

	/* Add other variables here. */
	int lineIndex = 0; // line index
	int elementIndex = 9; // element index
	int address; // to increment address
	int recordType; // record type

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */

	// Scanner for HexFile
	public HexFile(String hexFileName) throws FileNotFoundException {
		Scanner hexScanner = new Scanner(new File(hexFileName));
		hexFile = new ArrayList<String>();
		while (hexScanner.hasNextLine()) {
			hexFile.add(hexScanner.nextLine());
		}
		hexScanner.close();
	}

	/**
	 * Pulls the length of the data bytes from an individual record.
	 * 
	 * This extracts the length of the data byte field from an individual hex
	 * record. This is referred to as LL->Record Length in the documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return record length.
	 */
	
	public int getDataBytesOfRecord(String record) { // separating length and converting to hexadecimal
		int record16 = Integer.parseInt(record.substring(1, 3), 16);
		return record16;
	}

	/**
	 * Get the starting address of the data bytes.
	 * 
	 * Extracts the starting address for the data. This tells you where the data
	 * bytes start and are referred to as AAAA->Address in the documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return Starting address of where the data bytes go.
	 */
	public int getAddressOfRecord(String record) { // separating address and converting to hexadecimal
		int address16 = Integer.parseInt(record.substring(3, 7), 16);
		return address16;
	}

	/**
	 * Gets the record type.
	 * 
	 * The record type tells you what the record can do and determines what happens
	 * to the data in the data field. This is referred to as DD->Data in the
	 * documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return Record type.
	 */
	public int getRecordType(String record) { // separating record type and converting to hexadecimal
		int rtype = Integer.parseInt(record.substring(7, 9), 16);
		return rtype;
	}

	/**
	 * Returns the next halfword data byte.
	 * 
	 * This function will extract the next halfword from the Hex file. By repeatedly
	 * calling this function it will look like we are getting a series of halfwords.
	 * Behind the scenes we must parse the HEX file so that we are extracting the
	 * data from the data files as well as indicating the correct address. This
	 * requires us to handle the various record types. Some record types can effect
	 * the address only. These need to be processed and skipped. Only data from
	 * recordType 0 will result in something returned. When finished processing null
	 * is returned.
	 * 
	 * @return Next halfword.
	 */
	public Halfword getNextHalfword() {

		while (lineIndex < hexFile.size()) { // will break out of loop once recNdex is at end
			String rec = hexFile.get(lineIndex); // string
			int tt = getRecordType(rec);
			int aaaa = getAddressOfRecord(rec);
			int ll = getDataBytesOfRecord(rec);
			if (tt != 0) { // if record type != 0 then keep going, but if it's equal to 1, then
				lineIndex++;
				elementIndex = 9;
			} else {
				address = aaaa + (elementIndex - 9) / 2; // every time the byte element indexes move, then the address values increments by 2.
				String firstByte = rec.substring(elementIndex, elementIndex + 2); // first byte gets reassigned every time
				String secondByte = rec.substring(elementIndex + 2, elementIndex + 4); // second byte gets reassigned too
				String halfwordString = secondByte + firstByte; // putting second and first in order.
				Halfword hw = new Halfword(address, Integer.parseInt((halfwordString), 16));
				elementIndex += 4; // element index adds 4
				if (elementIndex >= 9 + ll * 2) { // continues the next line and data index starts at 9 again.
					lineIndex++;
					elementIndex = 9;
				}
				return hw;

			}

		}

		return null;
	}

}
