#!/usr/bin/env python3
from collections import namedtuple

Range = namedtuple('Range', ['min', 'max'])
inX = Range(156, 202)
inY = Range(-110, -69)

# inX = Range(20, 30)
# inY = Range(-10, -5)

def part1():
    print("Part 1:", sum(range(abs(inY.min))))

def part2():
    x_solutions = set()
    for x in range(inX.max + 1):
        dist = 0
        velocity = x
        for i in range(x):
            dist = dist + velocity
            velocity = velocity - 1
            if inX.min <= dist <= inX.max:
                x_solutions.add(x)

    solutions = set()

    # brute search
    for x in x_solutions:
        for y in range(abs(inY.min), -abs(inY.min) - 1, -1):
            x_dist = 0
            y_dist = 0
            x_velocity = x
            y_velocity = y
            for i in range(abs(inY.min)*2):
                x_dist = x_dist + x_velocity
                y_dist = y_dist + y_velocity
                x_velocity = max(x_velocity - 1, 0)
                y_velocity = y_velocity - 1
                if inY.min <= y_dist <= inY.max and inX.min <= x_dist <= inX.max:
                    solutions.add((x,y))

    print("Part2: ", len(solutions))
    # print("x_solutions", x_solutions)
    # print(solutions)
    # return solutions

solns = part2()
