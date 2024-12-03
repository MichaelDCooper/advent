#include <fstream>
#include <iostream>
#include <regex>
#include <sstream>
#include <string>

using namespace std;

int main() {
  ifstream inputFile("input.txt");
  if (!inputFile) {
    cerr << "Unable to open file input.txt" << endl;
    return 1;
  }

  stringstream buffer;
  buffer << inputFile.rdbuf();
  string content = buffer.str();

  regex pattern(R"(mul\((-?\d+),(-?\d+)\)|(do\(\))|(don't\(\)))");
  smatch matches;

  auto begin = sregex_iterator(content.begin(), content.end(), pattern);
  auto end = sregex_iterator();

  bool doEnabled = true;

  int counter = 0;
  for (auto it = begin; it != end; it++) {
    smatch match = *it;
    if (match[1].matched && match[2].matched) {
      int x = stoi(match[1].str());
      int y = stoi(match[2].str());
      int res = x * y;

      if (doEnabled) {
        counter += res;
      }
    } else if (match[3].matched) { // do() instruction
      doEnabled = true;
    } else if (match[4].matched) { // don't() instruction
      doEnabled = false;
    }
  }
  cout << "Counter: " << counter << endl;

  return 0;
}
