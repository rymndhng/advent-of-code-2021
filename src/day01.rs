use std::cmp::Ordering;
use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;

fn part1(numbers: &Vec<i32>) {
    let mut numbers = numbers.iter();
    // first number
    let init = numbers.next().unwrap();
    let state = numbers.fold((0, init), |state, n| {
        let (count, last_n) = state;

        let count = match n.cmp(&last_n) {
            Ordering::Greater => count + 1,
            _ => count,
        };

        (count, n)
    });
    println!("Part 1: count: {:?}, elements: {:?}", state.0, state.1);
}

fn part2(numbers: &Vec<i32>) {
    let mut numbers = numbers.windows(3).map(|nums| nums.iter().sum::<i32>());

    let init = numbers.next().unwrap();
    let state = numbers.fold((0, init), |state, n| {
        let (count, last_n) = state;

        let count = match n.cmp(&last_n) {
            Ordering::Greater => count + 1,
            _ => count,
        };

        (count, n)
    });
    println!("Part 2: count: {:?}, elements: {:?}", state.0, state.1);
}

pub fn main() -> std::io::Result<()> {
    let file = File::open("input/001.txt")?;
    let reader = BufReader::new(file);

    let numbers = reader
        .lines()
        .map(|x| x.unwrap().parse::<i32>().unwrap())
        .collect::<Vec<i32>>();

    part1(&numbers);
    part2(&numbers);
    Ok(())
}
