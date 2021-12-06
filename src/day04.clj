(ns day04
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input (-> (io/file "input/004.txt")
                  (io/reader)
                  (line-seq)
                  (vec)))

(def numbers (as-> (first input) %
                 (str/split % #",")
                 (map #(Integer/parseInt %) %)))

(def win-conditions
  (let [size       5
        horizontal (for [x (range size)]
                     (range (* x size) (+ (* x size) 5)))
        vertical   (for [x (range size)]
                     (range x (* size size) size))

        ;; diagonal [(range 0 (* size size) (+ size 1))
        ;;           (range (- size 1) (- (* size size) 1) (- size 1))]
        ]
    (concat horizontal vertical)
    ;; {:horizontal horizontal
    ;;  :vertical   vertical
    ;;  :diagonal   diagonal
    ;;  }
    ))

(defn win [board]
  (some (fn [condition]
          (when (every? #(= :filled %)
                        (map #(get board %) condition))
            {:board     board
             :condition condition}))
        win-conditions))

(def boards (->> (rest input)
                 (partition-by empty?)
                 (filter #(not= [""] %))
                 (map (fn [rows]
                        (for [row   rows
                              n     (str/split row #"\W+")
                              :when (not= n "")]
                          (Integer/parseInt n))))))

(defn mark-board [board n]
  (mapv #(if (= n %) :filled %) board))

(def part-1 (loop [[x & xs] numbers
                   boards   boards]
              (if (nil? x)
                :no-winners
                (let [boards' (map #(mark-board % x) boards)]
                  (if-let [{:keys [board condition]} (some win boards')]
                    (let [sum (apply + (filter #(not= % :filled) board))]
                      {:winner         board
                       :condition      condition
                       :winning-number x
                       :sum            sum
                       :answer         (* x sum)})
                    (recur xs boards'))))))

(def part-2
  (let [boards (for [board boards]
                 (loop [[x & xs] numbers
                        board board
                        n        0]
                   (if (nil? x)
                     :no-winner
                     (let [board (mark-board board x)]
                       (if-let [{:keys [board condition]} (win board)]
                         (let [sum (apply + (filter #(not= % :filled) board))]
                           {:winner         board
                            :condition      condition
                            :winning-number x
                            :sum            sum
                            :n              n
                            :answer         (* x sum)})
                         (recur xs board (inc n)))))))]
    (->> boards
         (sort-by :n))))

;; real answer
(last part-2)
