;; start with bb nrepl-server
(ns day01
  (:require [clojure.java.io :as io]))

(def input (mapv #(Integer/parseInt %) (line-seq (io/reader (io/file "input/001.txt")))))

(defn count-increasing
  [[total prev] n]
  (if (> n prev)
    [(inc total) n]
    [total n]))

(def part1 (let [[x & xs] input]
             (reduce count-increasing [0 x] xs)))

(def part2 (let [[x & xs] (map #(apply + %) (partition 3 1 input))]
             (reduce count-increasing [0 x] xs)))
