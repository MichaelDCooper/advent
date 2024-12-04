def find_xmas_patterns(grid):
    rows = len(grid)
    cols = len(grid)
    count = 0
    
    def is_mas(chars):
        return chars == ['M', 'A', 'S'] or chars == ['S', 'A', 'M']
    
    def check_x_pattern(r, c):
        if r == 0 or r >= rows - 1 or c == 0 or c >= cols - 1:
            return 0
            
        patterns = 0
        
        diagonal1 = [grid[r-1][c-1], grid[r][c], grid[r+1][c+1]]  # top-left to bottom-right
        diagonal2 = [grid[r-1][c+1], grid[r][c], grid[r+1][c-1]]  # top-right to bottom-left
        
        if is_mas(diagonal1) and is_mas(diagonal2):
            patterns += 1
            
        return patterns
    
    for row in range(1, rows-1):
        for col in range(1, cols-1):
            if grid[row][col] == 'A':
                count += check_x_pattern(row, col)
                
    return count

with open('input.txt', 'r') as file:
    input_text = file.read().strip()
    
grid = [list(line.strip()) for line in input_text.split('\n')]

result = find_xmas_patterns(grid)
print(f"Result: {result}")
