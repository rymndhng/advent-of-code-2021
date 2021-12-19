#!/usr/bin/env python3

f = open("../input/012.txt", "r")
input = {}

for line in f.readlines():
    k, v = line.split("-")
    v = v.strip()
    input[k] = input.get(k, []) + [v]
    input[v] = input.get(v, []) + [k]


def is_small_cave(cave):
    return cave.islower()


def find_complete_paths(allowed_caves):
    global input
    complete = []
    paths = [["start", x] for x in input["start"]]

    while len(paths) > 0:
        newPaths = []
        for path in paths:
            for cave in allowed_caves(path):
                if cave == "end":
                    complete.append(path + [cave])
                else:
                    newPaths.append(path + [cave])
        paths = newPaths

    return complete


def part1(input):
    def get_allowed_caves(path):
        for cave in input.get(path[-1], []):
            if (cave not in path) or not is_small_cave(cave):
                yield cave

    paths = find_complete_paths(get_allowed_caves)
    print("Part 1:", len(paths))


def part2(input):
    def allowed_caves(path):
        visits = dict()
        for cave in path:
            visits[cave] = counts.get(cave, 0) + 1

        allow_revisit = all([v <= 2 for v in visits(path).values()])
        for cave in input[path[-1]]:
            if cave == "start":
                None
            elif not is_small_cave(cave):
                yield cave
            elif cave not in path or allow_revisit:
                yield cave

    paths = find_complete_paths(part_2_allowed_caves)
    print("Part 2:", len(paths))


part1(input)
part2(input)
