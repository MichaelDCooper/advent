// Only part 1 solution. Part 2 in python.
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main() {
  ifstream file("input2.txt");

  vector<string> grid;
  string line;
  while (getline(file, line)) {
    if (!line.empty()) {
      grid.push_back(line);
    }
  }
  file.close();

  if (grid.empty()) {
    cerr << "File is empty" << endl;
    return 1;
  }

  int rows = grid.size();
  int cols = grid[0].length();

  auto isValid = [rows, cols](int x, int y) -> bool {
    return x >= 0 && x < rows && y >= 0 && y < cols;
  };

  int count = 0;

  for (int i = 1; i < rows - 1; i++) {
    for (int j = 1; j < cols - 1; j++) {
      if (grid[i][j] == 'A') {
        bool mas_dr = (isValid(i - 1, j - 1) && grid[i - 1][j - 1] == 'M' &&
                       grid[i][j] == 'A' && isValid(i + 1, j + 1) &&
                       grid[i + 1][j + 1] == 'S');

        bool mas_dl = (isValid(i + 1, j - 1) && grid[i + 1][j - 1] == 'S' &&
                       grid[i][j] == 'A' && isValid(i - 1, j + 1) &&
                       grid[i - 1][j + 1] == 'M');

        if (mas_dr && mas_dl) {
          count++;
        }
      }
    }
  }

  cout << "Found " << count << " X-MAS patterns" << endl;
  return 0;
}
