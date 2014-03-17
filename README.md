# ID3 
The goal of this project is to implement a ID3 partitioning.

## Machine Learning algorithm for decision trees
This algorithm keeps splitting nodes as long as the nodes have nonzero entropy and features are available. 
This projects goal is to infer imperfect decision trees with a small number of nodes and with small entropy. 

	$ Entropy(T|S) = For j=1 to k [Prob(Sj) * Entropy(T|Sj)]
	Gain(T,S) = Entropy(T)-Entropy(T|S) 

## Sample Input and Output

dataset.txt

10 4

0 0 0 0

0 0 1 0

1 0 1 0

1 1 0 1

0 0 1 1

0 1 1 1

1 1 1 1

1 1 1 0

0 0 1 0

1 0 1 0


partition.txt
X 1 10

Y 2 3 4 5

Z 6 7 8 9



	$ java -jar ID3_exe.jar
	Enter names of the files dataset input-partition output-partition
	dataset.txt partition.txt output.txt
	Partition Y was replaced with partitions Y0,Y1 using Feature 2

NOTE: 
In the same folder location verify "output" file is produced containing following data

output.txt

	X 1 10
	
	Y0 2 3 5
	
	Y1 4
	
	Z 6 7 8 9
	
	
## Other Tests:

dataset-1 partition-1 7 2 (2 3 5; 4)

dataset-1 partition-2 2 1 (1 2 5 9; 3 10)

dataset-2 partition-3 2 0 (1 5 6 7 9 10 11 12 13 14 15 16 22; )

 dataset-3 partition-4 2 1 (7 10 11 18 19 20 24 26 28 39 40;  1 4 9 12 14 25 31 41 44 45 50)
 


