(ns day03
  (:require [clojure.java.io :as io]))

(def example (-> (io/file "input/003.txt")
                  (io/reader)
                  (line-seq)
                  (vec)))

;; Yucky, is there a quicker way of applying a bit mask?
(def part1
  (let [threshold (/ (count example) 2)
        bits      (->> example
                       (map (fn [x] (mapv #(Integer/parseInt (str %)) x)))
                       (reduce (fn [x y] (map + x y)))
                       (map #(if (< threshold %) 1 0)))

        gamma   (reduce #(+ (bit-shift-left %1 1) %2) 0 bits)
        epsilon (reduce #(+ (bit-shift-left %1 1) %2) 0 (map #(bit-flip % 0) bits))]
    (* gamma epsilon)))

(defn filter-rating [data index mask]
  (let [{:keys [zeros ones]} (->> data
                                  (map #(get % index))
                                  (reduce #(if (= 1 %2)
                                             (update %1 :ones inc)
                                             (update %1 :zeros inc))
                                          {:ones 0 :zeros 0}))

        _ (println :zeros zeros :ones ones)
        match (if (= :high mask)
                (if (<= zeros ones) 1 0)
                (if (<= zeros ones) 0 1))]
    (filter #(= (get % index) match) data)))

(defn bit-array->int [bit-array]
  (reduce #(+ (bit-shift-left %1 1) %2) bit-array))

(def part2
  (let [data       (map (fn [x] (mapv #(Integer/parseInt (str %)) x)) example)
        bit-length (count (first data))

        oxygen-rating (loop [n    0
                             data data]
                        (if (and (< 1 (count data))
                                 (< n bit-length))
                          (recur (inc n) (filter-rating data n :high))
                          (first data)))

        co2-rating (loop [n    0
                          data data]
                     (if (and (< 1 (count data))
                              (< n bit-length))
                       (recur (inc n) (filter-rating data n :low))
                       (first data)))]
    {:oxygen-rating oxygen-rating
     :co2-rating    co2-rating
     :total (* (bit-array->int oxygen-rating)
               (bit-array->int co2-rating))}))
