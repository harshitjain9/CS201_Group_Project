# CS201_Project

Java Implementations of Unsorted List (Brute Force), Unbalanced KD Tree, Balanced KD Tree- Recursive Partioning, Balanced KD Tree- Presorting, and Vantage Point Trees to find the businesses within a cutoff distance in **both** dimensions of the xy plane from an input coordinate. 

a) To run the program, follow these instructions:

1. Place `yelp_academic_dataset_business.json` in `/CS201_Group_Project`. 

The json file contains 160,585 original businesses (businesses in the original `yelp_academic_dataset_business.json` available at https://www.yelp.com/dataset) and 839,425 (1,000,010 - 160,585) randomly generated businesses. The randomly generated businesses are added to test the performance of the algorithms with an input size of n = 1,000,000.

2. Run the following command: `mvn spring-boot:run` at `/CS201_Group_Project`.

3. Follow the command line instructions. 


b) If you want to change the input size n (number of businesses used from `yelp_academic_dataset_business.json` stored in the data structure), change the `nList` in the `main` function of `/CS201_Group_Project/Cs201ProjectApplication.java`. 

c) If you want to add more randomly generated businesses in `yelp_academic_dataset_business.json`, call the `appendJson` function in `/CS201_Group_Project/Cs201ProjectApplication.java`. 

# Acknowledgements 

Our implementations used the source code from the following before we tailored it to suit our specific problem statement and dataset:

1. [Building a Balanced k-d Tree in O(kn log n) Time](https://jcgt.org/published/0004/01/03/)
2. [Jvptree](https://github.com/jchambers/jvptree)
3. [Java Program to Find the Nearest Neighbor Using K-D Tree Search](https://www.sanfoundry.com/java-program-find-nearest-neighbour-using-k-d-tree-search/)
