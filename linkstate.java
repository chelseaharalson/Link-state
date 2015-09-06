import java.util.*;
import java.io.*;

public class linkstate {
	static String filename;

	public static void main(String[] args) {
		if (args.length != 1) 
        {
            System.err.println("Usage: java linkstate <network.txt>");
            System.exit(1);
        }
        filename = args[0];

        try {
			// Code to read in the file
			FileInputStream fstream = new FileInputStream(filename);
			FileInputStream fstream2 = new FileInputStream(filename);
			DataInputStream dataIn = new DataInputStream(fstream);
			DataInputStream dataIn2 = new DataInputStream(fstream2);
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(dataIn));
			BufferedReader buffReader2 = new BufferedReader(new InputStreamReader(dataIn2));

			String input;

			String readInput;
			readInput = buffReader2.readLine();
			String[] elements = readInput.split(",");
			int size = elements.length;
			//System.out.println(size);

			// Create outside array
			List<List<Integer>> listOfNodes = new ArrayList<List<Integer>>(size);
			List<List<Integer>> distanceList = new ArrayList<List<Integer>>(size);
			List<List<Integer>> locationList = new ArrayList<List<Integer>>(size);

			// Create inside array
			for (int i = 0; i < size; i++) {
				listOfNodes.add(new ArrayList<Integer>());
				distanceList.add(new ArrayList<Integer>());
				locationList.add(new ArrayList<Integer>());
			} 

			// Fill in array
			int counter = 0;
			String replacePeriod;
			String finalString;
			while ((input = buffReader.readLine()) != null && !input.equals("EOF.")) {
				replacePeriod = input.replaceAll("\\.", "");	// Replace period with comma
				finalString = replacePeriod.replaceAll("N", "99999");	// Replace N with 99999, which represents infinity
				String[] splited = finalString.split(",");	// Split by comma
				for(int i = 0; i < splited.length; i++) {
					listOfNodes.get(counter).add(Integer.parseInt(splited[i]));	// Fill in the list of nodes
				}
				counter++;
			}


			ArrayList<Integer> visited = new ArrayList<Integer>();
			ArrayList<Integer> node = new ArrayList<Integer>();
			ArrayList<Integer> path = new ArrayList<Integer>();
			ArrayList<Integer> winnerList = new ArrayList<Integer>();
			// Intialize ////////////////////////////////////////////////////////////////////////
			for (int i = 0; i < size; i++) {
				distanceList.get(0).add(listOfNodes.get(0).get(i));
				locationList.get(0).add(listOfNodes.get(0).get(0));
			}
			visited.add(0);
			/////////////////////////////////////////////////////////////////////////////////////

			int predNode = 0;
			int lowestDist = 99999;
			int nodeRow;
			int calculatedDist = 0;
			String visitedP = "";
			String visitedPath = "";
			ArrayList<String> No = new ArrayList<String>();

			for (nodeRow = 0; nodeRow < size; nodeRow++) {
				// Find lowest distance
				lowestDist = 99999;
				for (int i = 0; i < size; i++) {
					if (distanceList.get(nodeRow).get(i) < lowestDist && !visited.contains(i) && distanceList.get(nodeRow).get(i) != 0) {
						lowestDist = distanceList.get(nodeRow).get(i);
						predNode = i;
					}
				}

				visited.add(predNode);

				visitedP = printVisited(visited);
				visitedPath = removeComma(visitedP) + " ";
				No.add(visitedPath);
				
				//System.out.print(visitedPath);
				//System.out.println();

				distanceList.add(new ArrayList<Integer>());
				locationList.add(new ArrayList<Integer>());
				// Fill out the rows and columns of distance list and location list
				for (int col = 0; col < size; col++) {

					calculatedDist = lowestDist + listOfNodes.get(predNode).get(col);

					// If bringing down column because finished
					if (visited.contains(col)) {
						//System.out.println("Copy Finished");
						locationList.get(nodeRow + 1).add(locationList.get(nodeRow).get(col));
						distanceList.get(nodeRow + 1).add(-1);
						if (!winnerList.contains(col)) {
							winnerList.add(col);
						}
					}
					else if (listOfNodes.get(predNode).get(col) == 99999) {		// Bring down
						//System.out.println("Copy Infinite");
						distanceList.get(nodeRow + 1).add(distanceList.get(nodeRow).get(col));
						locationList.get(nodeRow + 1).add(locationList.get(nodeRow).get(col));
					}
					// If the calculated distance is > than the one now, then don't put it
					else if (calculatedDist > distanceList.get(nodeRow).get(col)) {
						//System.out.println("Copy Calc Distance >");
						distanceList.get(nodeRow + 1).add(distanceList.get(nodeRow).get(col));
						locationList.get(nodeRow + 1).add(locationList.get(nodeRow).get(col));
					}
					// If the calculate distance is < , then put it
					else {
						//System.out.println("Noderow: " + nodeRow + " Lowest Distance: " + lowestDist + " Distance: " + listOfNodes.get(predNode).get(col));
						distanceList.get(nodeRow + 1).add(calculatedDist);
						locationList.get(nodeRow + 1).add(predNode);
					}
				}
			}

			// Print out distance and location lists
			int rows = size;
			int cols = size;
			int cellSize = size;
			int rowLength = cols * 3 * cellSize + cols + 1;
			final char[] dividerArray = new char[rowLength];
			Arrays.fill(dividerArray, '-');
			String rowDivider = new String(dividerArray);

			System.out.println(rowDivider);
			System.out.print("Step \t Node' \t\t");
			for (int i = 1; i < size+1; i++) {
				System.out.print("D(" + i + "),p(" + i + ") \t");
			}
			System.out.println();
			System.out.println(rowDivider);
			for (int m = 0; m < size; m++) {
				System.out.print(m + "\t");
				//String paths = findPaths(winnerList, locationList, m, winnerList.get(m));	// Call findPaths function
				//System.out.print(removeComma(paths) + "\t\t");

				System.out.print(No.get(m) + "\t\t");

				for (int j = 0; j < size; j++) {
					if (distanceList.get(m).get(j) >= 0) {
						if (distanceList.get(m).get(j) == 99999) {
							System.out.print("INF,");
						}
						else {
							System.out.print(distanceList.get(m).get(j) + ",");
						}
						System.out.print(locationList.get(m).get(j) + 1 + "\t\t");
					}
					else {
						System.out.print("\t\t");
					}
				}
				System.out.println();
				System.out.println(rowDivider);
			}

			// Prints the winner list (used for testing)
			/*System.out.println();
			System.out.print("WINNER LIST: ");
			for (int i = 0; i < winnerList.size(); i++) {
				System.out.print(winnerList.get(i) + 1 + " ");
			}
			System.out.println();*/

			System.out.println();
			Integer y = 0;
			for (int x = 0; x < size; x++) {
				String paths = findPaths(winnerList, locationList, x, winnerList.get(x));	// Call findPaths function
				y = getLastNode(paths);
				System.out.println("From Node 1 to Node " + y + " the route is " + removeComma(paths));
			}

		dataIn.close();
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	// Function to find path from winner list and location list
	public static String findPaths (ArrayList<Integer> pWinnerList, List<List<Integer>> pLocationList, int fromRow, int fromCol) {
		ArrayList<Integer> paths = new ArrayList<Integer>();
		String result = "";

		while (fromCol > 0) {
			paths.add(fromCol);		// Add the column to go to, which is the predecessor node
			fromCol = pLocationList.get(fromRow).get(fromCol);	// Gets the predecessor node
			fromRow = pWinnerList.get(fromCol);		// Assigns row to go to
		}
		paths.add(0);	// Add the first one

		// Make the path string
		int p;
		for (int i = paths.size()-1; i >= 0; i--) {
			p = paths.get(i) + 1;
			result = result + p + ",";
		}

		return result;
	}

	// Remove comma from path because it's the last one
	public static String removeComma(String sPath) {
		return sPath.substring(0, sPath.length()-1);
	}

	public static String printVisited(ArrayList<Integer> pVisitedList) {
		String result = "";
		int p;
		for (int i = 0; i < pVisitedList.size()-1; i++) {
			p = pVisitedList.get(i) + 1;
			result = result + p + ",";
		}

		return result;
	}

	public static Integer getLastNode(String sPath) {
		String[] split = sPath.split(",");	// Split by comma
		Integer last = Integer.parseInt(split[split.length-1]);
		//System.out.println(last);
		return last;
	}
}