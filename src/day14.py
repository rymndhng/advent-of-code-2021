#!/usr/bin/env python3
import re
import itertools

f = open("../input/014.txt", "r")
lines = f.readlines()
template = lines[0].strip()
rules = dict()

for line in lines[2:]:
    (rule, insertion) = line.strip().split(" -> ")
    rules[rule] = insertion

def pairwise(iterable):
    # pairwise('ABCDEFG') --> AB BC CD DE EF FG
    a, b = itertools.tee(iterable)
    next(b, None)
    return zip(a, b)

def step(template):
    items = []
    for (a,b) in pairwise(template):
        items.append(a)
        items.append(rules[a+b])
    items.append(template[-1])
    return ''.join(items)

def part1():
    template1 = template
    for _ in range(10):
        template1 = step(template1)

    frequencies = dict()
    for c in template1:
        frequencies[c] = frequencies.get(c, 0) + 1

    items = [x for x in frequencies.items()]
    items.sort(key=lambda x: x[1])
    diff = items[-1][1] - items[0][1]
    print("Part 1:", diff)

def part2():
    frequencies = dict()
    for pair in pairwise(template):
        (a,b) = pair
        if b != None:
            key = a+b
            frequencies[key] = frequencies.get(key, 0) + 1

    letters = dict()
    for c in template:
        letters[c] = letters.get(c,0) + 1

    # print("template", template)
    # print("letters", letters)
    # print("frequencies", frequencies)

    # how to double
    for i in range(40):
        # letters2 = dict()
        frequencies2 = dict()

        for k,v in frequencies.items():
            c = rules[k]
            a,b = k[0], k[1]
            letters[c] = letters.get(c, 0) + v

            frequencies2[a+c] = frequencies2.get(a+c, 0) + v
            frequencies2[c+b] = frequencies2.get(c+b, 0) + v
        frequencies = frequencies2

    # print("template", step(template))
    # print("letters", letters)
    # print("frequencies", frequencies)
    items = [x for x in letters.items()]
    items.sort(key=lambda x: x[1])
    diff = items[-1][1] - items[0][1]
    print("Part 2:", diff)

part1()
part2()
