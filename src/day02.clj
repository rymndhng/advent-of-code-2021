(ns day02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (-> (io/file "input/002.txt")
               (io/reader)
               (line-seq)))

(defn parse-command [s]
  (let [[dir n] (str/split s #" ")]
    {:dir (keyword dir)
     :n (Integer/parseInt n)}))

(def commands (map parse-command input))

(def part1
  (let [{:keys [x y]} (reduce (fn [state {:keys [dir n]}]
                                (condp = dir
                                  :forward (update state :x + n)
                                  :up      (update state :y - n)
                                  :down    (update state :y + n))) {:x 0 :y 0} commands)]
    (* x y)))

(def part2
  (let [{:keys [x y]} (reduce (fn [state {:keys [dir n]}]
                                (condp = dir
                                  :forward (-> state
                                               (update :x + n)
                                               (update :y + (* n (:aim state))))
                                  :up      (update state :aim - n)
                                  :down    (update state :aim + n))) {:x 0 :y 0 :aim 0} commands)]
    (* x y)))
