(ns day07
  (:require [clojure.string :as str]))

(def input (map #(Integer/parseInt (str/trim %))
                (-> (slurp "input/007.txt")
                    (str/split #","))))

(def part-1 (let [elms (sort input)
                  min  (first elms)
                  max  (last elms)
                  cost (fn [pos]
                         (reduce + (map #(Math/abs ^Integer (- pos %)) elms)))]
              (-> (map cost (range min (inc max)))
                  (sort)
                  (first))))

(def m-cost
  (memoize (fn [n]
             (condp = n
               0 0
               1 1
               (+ n (m-cost (dec n)))))))

(def part-2 (let [elms (sort input)
                  min  (first elms)
                  max  (last elms)
                  cost (fn [pos]
                         (reduce + (map #(m-cost (Math/abs ^Integer (- pos %))) elms)))]
              (-> (map cost (range min (inc max)))
                  (sort)
                  (first))))
