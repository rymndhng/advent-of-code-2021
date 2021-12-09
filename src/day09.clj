(ns day09
  (:require [clojure.java.io :as io]))

(def input (->> (io/file "input/009.txt")
                (io/reader)
                (line-seq)))

(def entries (for [[x row]    (map-indexed (fn [k v] [k v]) input)
                   [y height] (map-indexed (fn [k v] [k v]) row)]
               [[x y] (Character/digit ^Character height 10)]))
(def lookup (into {} entries))

(def basins (for [[[x y] height] entries
                  :let [up    (get lookup [x (inc y)] Integer/MAX_VALUE)
                        down  (get lookup [x (dec y)] Integer/MAX_VALUE)
                        left  (get lookup [(dec x) y] Integer/MAX_VALUE)
                        right (get lookup [(inc x) y] Integer/MAX_VALUE)]

                  :when (every? #(< height %) [up down left right])]
              [x y]))

(def part-1
  (apply + (map #(inc (get lookup %)) basins)))

(defn get-basin-size [start-point]
  (loop [size          0
         [cur & queue] [start-point]
         explored      #{}]
    (if (nil? cur)
      size
      (let [[x y]      cur
            height     (get lookup cur)
            directions [[x (inc y)] [x (dec y)] [(dec x) y] [(inc x) y]]
            directions (filter #(and (not= 9 (get lookup %))
                                     (< height (get lookup % -1))
                                     (not (contains? explored %))) directions)]
        (recur (inc size) (into queue directions) (into explored directions))))))

(def part-2
  (->> (map get-basin-size basins)
      (sort)
      (reverse)
      (take 3)
      (apply *)))
