use std::collections::HashMap;
use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;
use std::str::FromStr;

fn part1(lines: &Vec<Line>) -> usize {
    let mut entries: HashMap<Point, i32> = HashMap::new();
    for line in lines {
        // println!("{:?}", line);
        // println!("{:?}", line.points_hv());
        for point in line.points_hv() {
            *entries.entry(point).or_insert(0) += 1;
        }
    }
    entries.into_iter().filter(|entry| entry.1 >= 2).count()
}

fn part2(lines: &Vec<Line>) -> usize {
    let mut entries: HashMap<Point, i32> = HashMap::new();
    for line in lines {
        // println!("{:?}", line);
        // println!("{:?}", line.points_hv());
        for point in line.points_hvd() {
            *entries.entry(point).or_insert(0) += 1;
        }
    }
    entries.into_iter().filter(|entry| entry.1 >= 2).count()
}


pub fn main() -> std::io::Result<()> {
    let file = File::open("input/005.txt")?;
    let reader = BufReader::new(file);

    let lines = reader
        .lines()
        .map(|x| x.unwrap().parse::<Line>().unwrap())
        .collect::<Vec<_>>();
    println!("Part 1: {:?}", part1(&lines));
    println!("Part 2: {:?}", part2(&lines));

    Ok(())
}

#[derive(Hash, Clone, Debug, Eq, PartialEq)]
struct Point {
    x: i32,
    y: i32,
}

impl FromStr for Point {
    type Err = std::num::ParseIntError;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut res = s.split(',');
        let x = res.next().unwrap().trim().parse::<i32>()?;
        let y = res.next().unwrap().trim().parse::<i32>()?;
        Ok(Point { x, y })
    }
}

#[derive(Debug)]
struct Line {
    a: Point,
    b: Point,
}

fn range(a: i32, b: i32) -> Vec<i32> {
    if a == b {
        vec![a]
    } else if a < b {
        (a..=b).collect()
    } else {
        // a > b
        let size = a - b;
        (0..=size).map(|x| a - x).collect()
    }
}

impl Line {
    fn points_hv<'a>(&self) -> Vec<Point> {
        if self.a == self.b {
            vec![self.a.clone()]
        } else if self.a.x == self.b.x {
            range(self.a.y, self.b.y)
                .iter()
                .map(|&y| Point { x: self.a.x, y })
                .collect()
        } else if self.a.y == self.b.y {
            range(self.a.x, self.b.x)
                .iter()
                .map(|&x| Point { x, y: self.a.y })
                .collect()
        } else {
            vec![]
        }
    }

    fn points_hvd<'a>(&self) -> Vec<Point> {
        if self.a == self.b {
            vec![self.a.clone()]
        } else if self.a.x == self.b.x {
            range(self.a.y, self.b.y)
                .iter()
                .map(|&y| Point { x: self.a.x, y })
                .collect()
        } else if self.a.y == self.b.y {
            range(self.a.x, self.b.x)
                .iter()
                .map(|&x| Point { x, y: self.a.y })
                .collect()
        } else {
            // diagonal
            let xrange = range(self.a.x, self.b.x);
            let yrange = range(self.a.y, self.b.y);
            Iterator::zip(xrange.iter(), yrange.iter()).map(|(&x,&y)| Point { x, y }).collect()
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
