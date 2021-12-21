#!/usr/bin/env python3
from math import inf
import sys
from queue import PriorityQueue

f = open("../input/015.txt", "r")
input = dict()
dim = 0

for (x, line) in enumerate(f.readlines()):
    dim = len(line.strip())
    for (y, c) in enumerate(line.strip()):
        input[(x, y)] = int(c)


# TODO: incorrect assumption that I can only move right/down
# def part1():
#     # known risks
#     risks = dict()
#     risks[(0,0)] = 0
#     # seed the state
#     for i in range(1, dim):
#         risks[(i, 0)] = input[(i, 0)] + risks[(i - 1, 0)]
#         risks[(0, i)] = input[(0, i)] + risks[(0, i - 1)]

#     for i in range(1, dim):
#         for j in range(i, dim):
#             # fixed x, variable y
#             pos = (i, j)
#             risks[pos] = input[pos] + min(risks[(i, j - 1)], risks[(i - 1, j)])

#             # y direction
#             pos = (j, i)
#             risks[pos] = input[pos] + min(risks[(j, i -1)], risks[(j - 1, i)])

#     return risks

# cost: min value traversing from any direction?

# What is A* search again?


def neighbors(node, dim):
    (x, y) = node
    return filter(
        lambda pos: pos[0] >= 0 and pos[1] >= 0 and pos[0] < dim and pos[1] < dim,
        [(x + 1, y), (x - 1, y), (x, y - 1), (x, y + 1)],
    )

def part1():
    # djstras algorithm!
    visited = set()
    queue = PriorityQueue()
    queue.put((0, (0, 0)))
    costs = dict()
    costs[(0,0)] = 0

    while not queue.empty():
        (prio, pos) = queue.get()
        if pos in visited:
            continue
        else:
            visited.add(pos)

        # print("visited: %s visiting %s, %s" % (len(visited), pos, prio))
        cost = costs.get(pos)
        for neighbour in neighbors(pos, dim):
            if neighbour not in visited:
                nCost = min(costs.get(neighbour, 99999), cost + input[neighbour])
                costs[neighbour] = nCost
                queue.put((nCost, neighbour))

    return costs

def node_cost(node):
    (x,y) = node
    originalNode = (x % dim, y % dim)
    cost = input[originalNode] + x // dim + y // dim
    if cost > 9:
        return cost % 9
    else:
        return cost

def part2():
    dim2 = dim * 5
    visited = set()
    queue = PriorityQueue()
    queue.put((0, (0, 0)))
    costs = dict()
    costs[(0,0)] = 0

    while not queue.empty():
        (prio, pos) = queue.get()
        if pos in visited:
            continue
        else:
            visited.add(pos)

        # print("visited: %s visiting %s, %s" % (len(visited), pos, prio))
        cost = costs.get(pos)
        for neighbour in neighbors(pos, dim2):
            if neighbour not in visited:
                nCost = min(costs.get(neighbour, 99999), cost + node_cost(neighbour))
                costs[neighbour] = nCost
                queue.put((nCost, neighbour))

    return costs

# def debug(data):
#     for x in range(dim):
#         for y in range(dim):
#             print("%3d" % data[(x, y)], end=" ")
#         print()


# risks = part1()
# print("Part 1: %s" % risks[(dim - 1, dim - 1)])

risks2 = part2()
print("Part 2: %s" % risks2[(dim * 5 - 1,  dim * 5 - 1)])
