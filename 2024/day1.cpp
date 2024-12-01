#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <unordered_map>

using namespace std;
int main() {
  ifstream inputFile("input.txt");
  if (!inputFile) {
    cerr << "Unable to open file input.txt" << endl;
    return 1;
  }

  vector<int> leftValues;
  unordered_map<int, int> rightValueMap;

  string line;
  while (getline(inputFile, line)) {
    istringstream iss(line);
    int value1, value2;
    if (iss >> value1 >> value2) {
      leftValues.push_back(value1);
      rightValueMap[value2]++;
    } else {
      cerr << "Error parsing line: " << line << endl;
    }
  }

  int similarityScore = 0;
  for (int value : leftValues) {
    if (auto it = rightValueMap.find(value); it != rightValueMap.end()) {
      similarityScore += value * it->second;
    }
  }
  cout << "Similarity score: " << similarityScore << endl;

  return 0;
}
