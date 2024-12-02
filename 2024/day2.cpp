#include <fstream>
#include <iostream>
#include <sstream>
#include <string>

using namespace std;

bool isMonotonic(const vector<int> &values) {
  if (values.size() < 2) {
    return true;
  }

  bool increasing = true;
  bool decreasing = true;

  for (size_t i = 1; i < values.size(); ++i) {
    if (values[i] > values[i - 1]) {
      decreasing = false;
    }
    if (values[i] < values[i - 1]) {
      increasing = false;
    }
  }

  return (increasing && !decreasing) || (!increasing && decreasing);
}

bool isInBounds(const vector<int> &values) {
  for (size_t i = 1; i < values.size(); ++i) {
    int change = abs(values[i] - values[i - 1]);
    if (change < 1 || change > 3) {
      return false;
    }
  }
  return true;
}

bool validSequence(const vector<int> &values) {
  if (isInBounds(values) && isMonotonic(values)) {
    return true;
  }

  for (size_t i = 0; i < values.size(); ++i) {
    vector<int> temp = values;
    temp.erase(temp.begin() + i);
    if (isMonotonic(temp) && isInBounds(temp)) {
      return true;
    }
  }

  return false;
}

int main() {
  ifstream inputFile("input.txt");
  if (!inputFile) {
    cerr << "Unable to open file input.txt" << endl;
    return 1;
  }

  int safe = 0;
  string line;
  while (getline(inputFile, line)) {
    istringstream iss(line);
    int value;
    vector<int> values;
    while (iss >> value) {
      values.push_back(value);
    }

    if (validSequence(values)) {
      safe++;
    }
  }

  cout << "Safe sequences: " << safe << endl;

  return 0;
}
