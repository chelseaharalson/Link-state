# Link-state
Project for networking class to simulate the link-state routing algorithm (like Dijkstra). Posting since deadline passed.

## Compiling the Code:
javac linkstate.java

## Running the Code:
java linkstate network.text

## Code Structure:
First, my code reads in the file and obtains the size. Next, my code parses the file and separates each element by commas. Then these number values are filled into a double arraylist.

There are two separate lists called distanceList and locationList to keep track of the distances and locations.

The lists are iterated through and the lowest distance is calculated. There is also a visited arraylist to keep track of the visited nodes. The winnerList contains the node that was picked to be brought down from each iteration.

Then there are some logic statements that implement the Dijkstra algorithm. If the calculated distance is less than the current distance, then the new calculated distance gets brought down and the predecessor node gets saved in the location list.

Once the distance, location, and winner lists are filled out, the path finding logic is implemented. The row and column are passed in the findPaths function. There is an arraylist called paths, and the column is first added to this arraylist. Then the fromCol is found by getting the predecessor node from the location list. The fromRow is found from the winnerList because it assigns what row to go to during the algorithm. This is how I implemented the routing.

For the table, I printed out the visited nodes, which I had stored in an arraylist, and what order they are visited in the N’ category.