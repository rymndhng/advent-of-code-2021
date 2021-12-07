use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;
use std::str::FromStr;
use std::collections::HashMap;

pub fn part1(lines: Vec<Line>) {
    let mut entries: HashMap<Point, i32> = HashMap::new();
    for line in lines {
        for points in line {
            entries.entry(point).or_insert(0) += 1;
        }
    }

    entries.filter(|k,v| v >= 2).sum()
}

pub fn main() -> std::io::Result<()> {
    let file = File::open("input/001.txt")?;
    let reader = BufReader::new(file);

    let lines = reader
        .lines()
        .map(|x| x.unwrap().parse::<Line>().unwrap())
        .collect::<Vec<_>>();

    part1(lines);

    Ok(())
}

struct Point {
    x: i32,
    y: i32,
}

impl FromStr for Point {
    type Err = std::num::ParseIntError;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut res = s.split(',');
        let x = res.next().unwrap().parse::<i32>()?;
        let y = res.next().unwrap().parse::<i32>()?;
        Ok(Point { x, y })
    }
}

struct Line {
    a: Point,
    b: Point,
}

impl Line {
    fn points<'a>(&self) -> std::slice::Iter<'_, Point> {
        if self.a.x == self.b.x {
            (self.a.y..=self.b.y).map(|y| Point { x: self.a.x, y })
        } else {
            (self.a.x..=self.b.x).map(|x| Point { x, y: self.a.y })
        }
    }
}

impl FromStr for Line {
    type Err = std::num::ParseIntError;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut res = s.split("->");
        let a = res.next().unwrap().parse::<Point>()?;
        let b = res.next().unwrap().parse::<Point>()?;
        Ok(Line { a, b })
    }
}
