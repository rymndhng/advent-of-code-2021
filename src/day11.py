import itertools

def parse(f):
    grid = dict()
    for (i,line) in enumerate(f.readlines()):
        for (j, char) in enumerate(line.strip()):
            grid[(i,j)] = int(char)
    return grid

def positions(grid, pos):
    (x, y) = pos
    for x1 in [-1, 0, 1]:
        for y1 in [-1, 0, 1]:
            if not (x1 == 0 and y1 == 0):
                pos = (x + x1, y + y1)
                if pos in grid:
                    yield pos

def is_pulsed(grid, key):
    return grid[key] == 0

def pulse(grid0):
    grid = { k: v + 1 for (k,v) in grid0.items()}
    while(True):
        energy = dict()
        for (k, v) in grid.items():
            if v > 9:
                grid[k] = 0     # pulsed
                for pos in positions(grid, k):
                    energy[pos] = energy.get(pos, 0) + 1

        # end if there are no energy
        if len(energy) == 0:
            return grid

        for (k,v) in energy.items():
            if not is_pulsed(grid, k):
                # print(f"pos: {k}, prev: {grid[k]} inc: {v}")
                grid[k] = grid[k] + v

def part1():
    f = open("../input/011.txt", "r")
    grid = parse(f)
    count = 0
    for i in range(100):
        grid = pulse(grid)
        for (k,v) in grid.items():
            if v == 0:
                count = count + 1
        print(f"Iteration {i}: {count}")
        # for x in range(5):
        #     for y in range(5):
        #         print(grid.get((x,y),""), end="")
        #     print("")

def part2():
    f = open("../input/011.txt", "r")
    grid = parse(f)
    for i in itertools.count():
        grid = pulse(grid)
        if sum([v for (k,v) in grid.items()]) == 0:
            return print(f"Blinking at iteration: {i+1}")

part1()
part2()
