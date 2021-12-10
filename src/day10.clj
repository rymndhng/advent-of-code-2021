(ns day10
  (:require [clojure.java.io :as io]))

(def input (->> (io/file "input/010.txt")
                (io/reader)
                (line-seq)))

(def rules {\[ \]
            \{ \}
            \< \>
            \( \)})

(def open-pairs (into #{} (map first rules)))
(def close-pairs (into #{} (map second rules)))

(defn compile-syntax [text]
  (loop [[x & xs] (seq text)
         stack    []]
    (cond
      (nil? x)
      stack                   ;return stack

      (contains? open-pairs x)
      (recur xs (conj stack x))

      (contains? close-pairs x)
      (if (= (rules (peek stack)) x)
        (recur xs (pop stack))
        x                               ; invalid pair
        )

      :else
      (throw (ex-info "unknwon char" {:char x})))))

(def part-1 (let [freq (->> (map compile-syntax input)
                            (filter identity)
                            (frequencies))]
              (+ (* 3 (get freq \)))
                 (* 57 (get freq \]))
                 (* 1197 (get freq \}))
                 (* 25137 (get freq \>)))))

(def points {\) 1
             \] 2
             \} 3
             \> 4})

(defn score [s]
  (let [completion (map #(get rules %) (reverse s))]
    (reduce #(+ (* 5 %1)
                (points %2))
            0
            completion)))

(def part-2 (let [scores (->> (map compile-syntax input)
                              (filter coll?)
                              (map score)
                              (sort)
                              (into []))
                  middle (/ (count scores) 2)]
              (nth scores middle)))
