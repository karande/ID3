/** 
 * @author Vishal Karande (vmk130030@utdallas.edu)
 */

package com.ml.id3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The InputOutput class implements input and output processing functionalities
 * It does file handling functionalities
 */
public class InputOutput {

	/**
	 * This function processes partition file and store it in ID3 readable
	 * format
	 * 
	 * @param fileName
	 *            is name of partition file
	 */
	public static void parsePartitions(String fileName) {
		String line = null;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			while (null != (line = br.readLine())) {
				ID3.partitionCount++;
			}
			br = new BufferedReader(new FileReader(fileName));
			ID3.classNames = new String[ID3.partitionCount];
			ID3.classData = new int[ID3.partitionCount][ID3.instanceCount];
			ID3.classCount = new int[ID3.partitionCount];
			int j = 0;
			while (null != (line = br.readLine())) {
				String[] values = line.split(" ");
				ID3.classNames[j] = values[0];
				ID3.classCount[j] = 0;
				for (int i = 0; i < values.length - 1; i++) {
					ID3.classCount[j]++;
					ID3.classData[j][i] = Integer.parseInt(values[i + 1]);
				}
				j++;
			}
			br.close();
		} catch (FileNotFoundException ex) {
			System.out
					.println("Specified Partition file not found in the same directory.");
		} catch (IOException ex) {
			Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * This function processes data-set file and store it in ID3 readable format
	 * 
	 * @param fileName
	 *            is name of data-set file
	 */
	public static void parseDataset(String fileName) {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			String[] countString = line.split(" ");
			ID3.instanceCount = Integer.parseInt(countString[0]);
			ID3.featureCount = Integer.parseInt(countString[1]);
			ID3.inputData = new int[ID3.instanceCount][ID3.featureCount];

			int rowCount = 0;
			while (null != (line = br.readLine())) {
				String[] features = line.split(" ");
				for (int i = 0; i < features.length; i++) {
					ID3.inputData[rowCount][i] = Integer.parseInt(features[i]);
				}
				rowCount++;
			}

			br.close();
		} catch (FileNotFoundException ex) {
			System.out
					.println("Specified Dataset file not found in the same directory.");
		} catch (IOException ex) {
			Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * This function writes ID3 output into output file.
	 */
	public static void writeOutput() {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(
					ID3.outputFile));
			String line = "";
			for (int i = 0; i < ID3.partitionCount; i++) {
				if (i == ID3.splitClassID) {
					String split1 = ID3.classNames[i] + "0";
					String split2 = ID3.classNames[i] + "1";
					String split3 = ID3.classNames[i] + "2";
					for (int j = 0; j < ID3.classCount[i]; j++) {
						if (ID3.inputData[ID3.classData[i][j] - 1][ID3.splitFeatureID] == 1)
							split1 = split1 + " " + ID3.classData[i][j];
						if (ID3.inputData[ID3.classData[i][j] - 1][ID3.splitFeatureID] == 0)
							split2 = split2 + " " + ID3.classData[i][j];
						if (ID3.inputData[ID3.classData[i][j] - 1][ID3.splitFeatureID] == 2)
							split3 = split3 + " " + ID3.classData[i][j];
					}
					output.write(split1);
					output.write("\n");
					output.write(split2);
					output.write("\n");
					output.write(split3);
					if (i < ID3.partitionCount)
						output.write("\n");
					System.out.println("Partition " + ID3.classNames[i]
							+ " was replaced with partitions "
							+ ID3.classNames[i] + "0" + "," + ID3.classNames[i]
							+ "1" + " using Feature "
							+ (ID3.splitFeatureID + 1));
				} else {
					line = ID3.classNames[i];
					for (int j = 0; j < ID3.classCount[i]; j++) {
						line = line + " " + ID3.classData[i][j];
					}
					output.write(line);
					if (i < ID3.partitionCount)
						output.write("\n");
				}
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
