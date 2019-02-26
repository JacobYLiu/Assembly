package edu.sbcc.cs107;

/**
 * @author Jacob Liu CS 107: Disassembler Project
 * 
 *         This code implements the disassembler as well as pulling apart the
 *         Hex file. The hex file format is documented at
 *         http://www.keil.com/support/docs/1584/
 */
public class Disassembler {

	public int maskFunct(int value, int lsb, int msb) {
		return value >> lsb & (int) (Math.pow(2, msb - lsb + 1) - 1); // (2^n+1) - 1 to mask the values
	}

	/**
	 * Extracts the register operand from a halfword.
	 * 
	 * The register operand (e.g. r0) is used by many mnemonics and is embedded in
	 * the data halfword. It position is specified by the least significant bit and
	 * most significant bit. This value is extracted and concatenated with "r" to
	 * give us the desired register.
	 * 
	 * @param hw
	 *            Halfword that contains the machine code data.
	 * @param lsBitPosition
	 *            Encoded register value (LSB)
	 * @param msBitPosition
	 *            Encoded register value (MSB)
	 * @return Register field designation (e.g. r1)
	 */
	public String getRegister(Halfword hw, int lsBitPosition, int msBitPosition) {
		/* Your code here */
		// r + the 
		return "r" + maskFunct(hw.getData(), lsBitPosition, msBitPosition);

	}

	/**
	 * Extracts the immediate operand from a halfword.
	 * 
	 * 0* Same as the getRegister function but returns the embedded immediate value
	 * (e.g. #4).
	 * 
	 * @param hw
	 *            Halfword that contains the machine code data.
	 * @param lsBitPosition
	 *            Encoded immediate value (LSB)
	 * @param msBitPosition
	 *            Encoded immediate value (MSB)
	 * @return Immediate field designation (e.g. #12)
	 */
	public String getImmediate(Halfword hw, int lsBitPosition, int msBitPosition) {
		/* Your code here */
		return "#" + maskFunct(hw.getData(), lsBitPosition, msBitPosition);
	}

	/**
	 * Returns a formatted string consisting of the Mnemonic and Operands for the
	 * given halfword.
	 * 
	 * The halfword is decoded into its corresponding mnemonic and any optional
	 * operands. The return value is a formatted string with an 8 character wide
	 * field for the mnemonic (left justified) a single space and then any operands.
	 * 
	 * @param hw
	 *            Halfword that contains the machine code data.
	 * @return Formatted string containing the mnemonic and any operands.
	 */
	public String dissassembleToString(Halfword hw) {
		/* Your code here */
		String x = Integer.toBinaryString(hw.getData()); // converting it to binary
		StringBuilder x1 = new StringBuilder(x);
		for (int i = 0; i < 16 - x.length(); i++) { // adding missing 0's until the binary size is 16 bits
			x1.insert(0, 0);
		}
		x = x1.toString();

		String result = ""; // empty string to insert for return statement
		
		// ADCS
		if (x.substring(0, 10).equals("0100000101")) { // separating the binary from the instruction set (same for all
														// other instructions)
			String instruct = "ADCS     ";
			String rm = getRegister(hw, 3, 5); // separating rm from the encode
			String rdn = getRegister(hw, 0, 2) + ", "; // separating rdn from the encode
			result = instruct + rdn + rm;
			// ADDS
		} else if (x.substring(0, 7).equals("0001110")) { 
			String instruct = "ADDS     ";
			String rn = getRegister(hw, 3, 5) + ", ";
			String rd = getRegister(hw, 0, 2) + ", ";
			String immediate = getImmediate(hw, 6, 8);
			result = instruct + rd + rn + immediate;
			// CMN
		} else if (x.substring(0, 10).equals("0100001011")) {
			String instruct = "CMN      ";
			String rm = getRegister(hw, 3, 5);
			String rn = getRegister(hw, 0, 2) + ", ";
			result = instruct + rn + rm;
			// LDRSB
		} else if (x.substring(0, 7).equals("0101011")) {
			String instruct = "LDRSB    ";
			String rt = getRegister(hw, 0, 2) + ", ";
			String rn = "[" + getRegister(hw, 3, 5) + ",";
			String rm = getRegister(hw, 6, 8) + "]";
			result = instruct + rt + rn + rm;
			// register MOVS
		} else if (x.substring(0, 10).equals("0000000000")) {
			String instruct = "MOVS     ";
			String rd = getRegister(hw, 0, 2) + ", ";
			String rm = getRegister(hw, 3, 5);
			result = instruct + rd + rm;
			// immediate MOVS
		} else if (x.substring(0, 5).equals("00100")) {
			String instruct = "MOVS     ";
			String rd = getRegister(hw, 8, 10) + ", ";
			String immediate = getImmediate(hw, 0, 7);
			result = instruct + rd + immediate;
			// REV
		} else if (x.substring(0, 10).equals("1011101000")) {
			String instruct = "REV      ";
			String rd = getRegister(hw, 0, 2) + ", ";
			String rm = getRegister(hw, 3, 5);
			result = instruct + rd + rm;
			// B
		} else if (hw.getData() == 0xE7FE) {
			result = "B       .";
			
		}

		return result;
	}

}
