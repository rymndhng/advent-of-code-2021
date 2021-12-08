(ns day06
  (:require [clojure.string :as str]))

(def input (map #(Integer/parseInt (str/trim %))
                (-> (slurp "input/006.txt")
                    (str/split #","))))

(defn step [fish]
  (let [fish (dec fish)]
    (if (< fish 0)
      [6 8]
      [fish])))

(defn step-all [fishes]
  (for [fish fishes
        fish (step fish)]
    fish))

(def part-1 (count (nth (iterate step-all input) 80)))


(defn step-part-2
  [[fish n]]
  (let [fish (dec fish)]
    (if (< fish 0)
      {6 n 8 n}
      {fish n})))

(defn step-all-part-2 [fishes]
  (reduce (fn [fishes fish]
            (merge-with + fishes (step-part-2 fish)))
          {}
          fishes))

(def part-2 (as-> (frequencies input) %
              (iterate step-all-part-2 %)
              (nth % 256)
              (vals %)
              (apply + %)))
