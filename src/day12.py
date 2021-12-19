#!/usr/bin/env python3

f = open("../input/012.txt", "r")
input = {}

for line in f.readlines():
    k,v = line.split("-")
    v = v.strip()
    input[k] = input.get(k, []) + [v]
    input[v] = input.get(v, []) + [k]

def is_small_cave(cave):
    return cave.islower()

def directions(path):
    global input
    for cave in input[path[-1]]:
        if (path not in cave) or not is_small_cave(cave):
            yield cave

def part1(input):
    complete = []
    paths = [["start", x] for x in input["start"]]

    while(len(paths) > 0):
        newPaths = []
        for path in paths:
            for cave in input.get(path[-1], []):
                if cave == "end":
                    complete.append(path + [cave])
                elif (cave not in path) or not is_small_cave(cave):
                    newPaths.append(path + [cave])
        paths = newPaths

    print("Part 1:", len(complete))

def small_cave_frequencies(path):
    counts = dict()
    for cave in path:
        if is_small_cave(cave):
            counts[cave] = counts.get(cave, 0) + 1
    return counts

def part_2_allowed_caves(path):
    global input

    allow_revisit = not any([v >= 2 for v in small_cave_frequencies(path).values()])
    for cave in input[path[-1]]:
        if cave == 'start':
            None
        elif not is_small_cave(cave):
            yield cave
        elif cave not in path or allow_revisit:
            yield cave

def part2(input):
    complete = []
    paths = [["start", x] for x in input["start"]]

    while(len(paths) > 0):
        newPaths = []
        for path in paths:
            for cave in part_2_allowed_caves(path):
                if cave == "end":
                    complete.append(path + [cave])
                else:
                    newPaths.append(path + [cave])
        paths = newPaths

    # print("paths", complete)
    print("Part 2:", len(complete))


part1(input)
part2(input)
