/** 
 * @author Vishal Karande (vmk130030@utdalla.edu)
 */

package com.ml.id3;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The ID3 class implements an ID3 algorithm that partitions data based on a
 * selected attribute.
 */
public class ID3 {

	static int featureCount = 0;
	static int instanceCount = 0;
	static int partitionCount = 0;
	static String outputFile;
	static int[][] inputData;
	static int[] classCount;
	static String[] classNames;
	static int[][] classData;
	static double[] classEntropy;
	static double[][] conditionalEntropy;
	static double[][] informationGain;
	static int splitClassID;
	static int splitFeatureID;

	/**
	 * This function accepts data-set file, partition file and output file name
	 * from user and process it apply as an input to ID3
	 */
	public static void input() {
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		System.out
				.println("Enter names of the files dataset input-partition output-partition");
		String input = scanner.nextLine();

		String[] fileNames = input.split(" ");
		String dataset = fileNames[0];
		String inputPartition = fileNames[1];
		outputFile = fileNames[2];
		InputOutput.parseDataset(dataset);
		InputOutput.parsePartitions(inputPartition);
	}

	/**
	 * This function calculates the entropy for a particular data-set
	 * 
	 * @param noCount
	 *            is number of instances for which output is zero
	 * @param yesCount
	 *            is number of instances for which output is one
	 */
	public static double calculateEntropy(int noCount, int yesCount) {
		double ans = 0;
		double totalCount = noCount + yesCount;
		double s1 = (noCount / totalCount);
		double s2 = (yesCount / totalCount);

		if (s1 == 0 && s2 == 0)
			ans = 0;
		else if (s1 == 0 && s2 != 0)
			ans = (-1) * (s2 * ((Math.log(s2) / Math.log(2))));
		else if (s1 != 0 && s2 == 0)
			ans = (-1) * (s1 * ((Math.log(s1) / Math.log(2))));
		else
			ans = (-s1 * (Math.log(s1) / Math.log(2)) - (s2 * (Math.log(s2) / Math
					.log(2))));

		if (ans == -0.0)
			return 0;
		else
			return ans;
	}

	/**
	 * This function calculates the conditional entropy for a particular
	 * data-set
	 * 
	 * @param noCount0
	 *            is number of instances for which attribute value is zero and
	 *            output is zero
	 * @param yesCount0
	 *            is number of instances for which attribute value is zero and
	 *            output is one
	 * @param noCount1
	 *            is number of instances for which attribute value is one and
	 *            output is zero
	 * @param yesCount1
	 *            is number of instances for which attribute value is one and
	 *            output is one
	 */
	public static double calculateConditionalEntropy(int noCount0,
			int yesCount0, int noCount1, int yesCount1,int yesCount2, int noCount2) {
		double count0 = noCount0 + yesCount0;
		double count1 = noCount1 + yesCount1;
		double count2 = noCount2 + yesCount2;
		double totalCount = count0 + count1 + count2;

		
		if (count0 == 0 && count1 == 0 && count2==0)
			return 0;
		
		if (count0 == 0 && count1 == 0 && count2!=0)
			return (count2 / totalCount)
					* calculateEntropy(noCount2, yesCount2);
		
		if (count0 == 0 && count1 != 0 && count2==0)
			return (count1 / totalCount)
					* calculateEntropy(noCount1, yesCount1);
		
		if (count0 == 0 && count1 != 0 && count2!=0)
			return (count2 / totalCount)
					* calculateEntropy(noCount2, yesCount2)
					+ (count1 / totalCount)
					* calculateEntropy(noCount1, yesCount1);
		
		if (count0 != 0 && count1 == 0 && count2==0)
			return (count0 / totalCount)
					* calculateEntropy(noCount0, yesCount0);
		
		if (count0 != 0 && count1 == 0 && count2!=0)
			return (count0 / totalCount)
					* calculateEntropy(noCount0, yesCount0)
					+ (count2 / totalCount)
					* calculateEntropy(noCount2, yesCount2);
		
		if (count0 != 0 && count1 != 0 && count2==0)
			return (count0 / totalCount)
					* calculateEntropy(noCount0, yesCount0)
					+ (count1 / totalCount)
					* calculateEntropy(noCount1, yesCount1);
		
		if (count0 != 0 && count1 != 0 && count2!=0)
			return (count0 / totalCount)
					* calculateEntropy(noCount0, yesCount0)
					+ (count1 / totalCount)
					* calculateEntropy(noCount1, yesCount1)
					+ (count2 / totalCount)
					* calculateEntropy(noCount2, yesCount2);
		return 0; 
		
	}

	/**
	 * This function calculates information gain IG(S|A(i)) =
	 * Entropy(S)-Entropy(S|A(i))
	 */
	public static void findMaxGainFeature() {

		double maxF = 0;
		for (int i = 0; i < partitionCount; i++) {
			double f = 0;
			double maxValue = 0;
			int maxFeatureID = 0;
			for (int j = 0; j < featureCount - 1; j++) {
				if (informationGain[i][j] > maxValue) {
					maxValue = informationGain[i][j];
					maxFeatureID = j;
				}
			}
			f = maxValue * ((double) classCount[i] / instanceCount);
			
			//System.out.println("\n Information Gain:"+f);
			if (f > maxF) {
				maxF = f;
				splitClassID = i;
				splitFeatureID = maxFeatureID;
			}
		}
	}

	/**
	 * This main function implements ID3 partition algorithm
	 */
	public static void main(String[] args) {

		try {

			input();

			classEntropy = new double[partitionCount];

			for (int i = 0; i < partitionCount; i++) {
				int yesCount = 0;
				int noCount = 0;
				int tCount = 0;
				for (int j = 0; j < instanceCount; j++) {

					for (int k = 0; k < classCount[i]; k++) {
						if (classData[i][k] == (j + 1)
								&& inputData[j][featureCount - 1] == 1)
							yesCount++;
						if (classData[i][k] == (j + 1)
								&& inputData[j][featureCount - 1] == 0)
							noCount++;
					}
				}
				classEntropy[i] = calculateEntropy(noCount, yesCount);
			}

			conditionalEntropy = new double[partitionCount][featureCount - 1];
			informationGain = new double[partitionCount][featureCount - 1];

			for (int i = 0; i < partitionCount; i++) {
				for (int j = 0; j < featureCount - 1; j++) {
					int yesCount0 = 0;
					int noCount0 = 0;
					int yesCount1 = 0;
					int noCount1 = 0;
					int noCount2 =0;
					int yesCount2 =0;

					for (int k = 0; k < classCount[i]; k++) {
						if (inputData[classData[i][k] - 1][j] == 0
								&& inputData[classData[i][k] - 1][featureCount - 1] == 0)
							noCount0++;
						else if (inputData[classData[i][k] - 1][j] == 0
								&& inputData[classData[i][k] - 1][featureCount - 1] == 1)
							yesCount0++;
						else if (inputData[classData[i][k] - 1][j] == 1
								&& inputData[classData[i][k] - 1][featureCount - 1] == 0)
							noCount1++;
						else if (inputData[classData[i][k] - 1][j] == 1
								&& inputData[classData[i][k] - 1][featureCount - 1] == 1)
							yesCount1++;
						else if (inputData[classData[i][k] - 1][j] == 2
								&& inputData[classData[i][k] - 1][featureCount - 1] == 0)
							noCount2++;
						else if (inputData[classData[i][k] - 1][j] == 2
								&& inputData[classData[i][k] - 1][featureCount - 1] == 1)
							yesCount2++;
					}

					conditionalEntropy[i][j] = calculateConditionalEntropy(
							noCount0, yesCount0, noCount1, yesCount1, noCount2, yesCount2);
					informationGain[i][j] = classEntropy[i]
							- conditionalEntropy[i][j];
				}
			}
			findMaxGainFeature();
			InputOutput.writeOutput();

		} catch (Exception e) {
			System.out.println("Partition unsuccessful...");
		}
	}
}
