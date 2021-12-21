#!/usr/bin/env python3
import itertools
import functools
import pprint
in16 = open("../input/016.txt", "r").readlines()[0].strip()

def bit_sum(bit_iter):
    n = 0
    for b in bit_iter:
        n = (n << 1) + b

    return n

def bit_iter(chars):
    for c in chars:
        val = int(c, 16)
        yield val >> 3 & 1
        yield val >> 2 & 1
        yield val >> 1 & 1
        yield val >> 0 & 1

def read_literal(bit_iter):
    val = 0
    while True:
        end = next(bit_iter) == 0
        val = (val << 4) + bit_sum(itertools.islice(bit_iter, 4))

        if end:
            return val

def read_packet(bits_iter):
    p_version = bit_sum(itertools.islice(bits_iter, 3))
    p_type_id = bit_sum(itertools.islice(bits_iter, 3))

    if p_type_id == 4:
        return {'version': p_version, 'type': p_type_id, 'value': read_literal(bits_iter)}

    p_length_type = next(bits_iter)
    if p_length_type == 0:
        bits = list(itertools.islice(bits_iter, 15))
        n_subpacket_bits = bit_sum(bits)
        subpacket = itertools.islice(bits_iter, n_subpacket_bits)
        subpackets = []
        # print("n_subpacket_bits: %s" % n_subpacket_bits)
        while(True):
            try:
                subpackets.append(read_packet(subpacket))
            except StopIteration:
                break
        return { 'version': p_version, 'type': p_type_id, 'value': subpackets }
    else:
        sub_packets = []
        n_packets = bit_sum(itertools.islice(bits_iter, 11))
        return {
            'version': p_version,
            'type': p_type_id,
            'value': [read_packet(bits_iter) for n in range(n_packets)]
        }

def version_sum(packet):
    total = packet['version']
    if type(packet['value']) is list:
        total = total + sum([version_sum(p) for p in packet['value']])
    return total

def calc(packet):
    type = packet['type']
    value = packet['value']
    if type == 0:
        return sum([calc(p) for p in value])
    elif type == 1:
        return functools.reduce(lambda x,y: x * calc(y), value, 1)
    elif type == 2:
        return functools.reduce(min, [calc(x) for x in value])
    elif type == 3:
        return functools.reduce(max, [calc(x) for x in value])
    elif type == 4:
        return value
    elif type == 5:
        return 1 if calc(value[0]) > calc(value[1]) else 0
    elif type == 6:
        return 1 if calc(value[0]) < calc(value[1]) else 0
    elif type == 7:
        return 1 if calc(value[0]) == calc(value[1]) else 0


def part1():
    print("Part1: ", version_sum(read_packet(bit_iter(in16))))

def part2():
    print("Part2: ", calc(read_packet(bit_iter(in16))))

# print(read_packet(bit_iter("38006F45291200")))
# print(read_packet(bit_iter("EE00D40C823060")))
# print(calc(read_packet(bit_iter("9C0141080250320F1802104A08"))))
# pprint.pprint(read_packet(bit_iter("9C0141080250320F1802104A08")))
