#!/usr/bin/env python3
import re

f = open("../input/013.txt", "r")
dots = []
folds = []

reader = f.readlines()

for line in reader:
    if line == "\n":
        break

    x, y = line.split(",")
    dots.append((int(x), int(y)))

p = re.compile("fold along (\w)=(\d+)")
for line in reader:
    m = p.match(line)
    if m:
        (dim, pos) = m.groups()
        folds.append((dim, int(pos)))

def fold(dots, fold):
    (direction, pos) = fold
    output = set()
    for (x, y) in dots:
        if direction == "x":
            x = pos - (x - pos) if x >= pos else x
            output.add((x, y))
        else:
            y = pos - (y - pos) if y >= pos else y
            output.add((x, y))

    return output


def part1():
    print("part 1:", len(fold(dots, folds[0])))

def part2(dots, folds):
    dots1 = dots.copy()
    for f in folds:
        dots1 = fold(dots1, f)

    for y in range(40):
        for x in range(40):
            if (x,y) in dots1:
                print("#", end="")
            else:
                print(" ", end="")
        print()



part1()
part2(dots, folds)
