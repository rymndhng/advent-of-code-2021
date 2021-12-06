use std::cmp::Ordering;
use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;

pub fn main() -> std::io::Result<()> {
    let file = File::open("input/001.txt")?;
    let reader = BufReader::new(file);

    let mut numbers = reader.lines().map(|x| x.unwrap().parse::<i32>().unwrap());

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

    println!("count: {:?}, elements: {:?}", state.0, state.1);
    Ok(())
}
