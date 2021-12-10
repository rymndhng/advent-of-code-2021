(ns day08
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def input (->> (io/file "input/008.example.txt")
                (io/reader)
                (line-seq)
                (map (fn [s]
                       (let [[ex output] (str/split s #"\|")]
                         [(str/split (str/trim ex) #" ")
                          (str/split (str/trim output) #" ")])))))

(def part-1 (->> (map second input)
                 (flatten)
                 (filter #(contains? #{2 4 3 7} (count %)))
                 (count)))

;; -- possible mapping  --------------------------------------------------------


(def default-set #{\a \b \c \d \e \f \g})
(def default-state
  (reduce #(assoc %1 %2 default-set) {} default-set))
(def count->positions
  {2 #{\c \f}                ;1
   4 #{\b \c \d \f}          ;4
   3 #{\a \c \f}             ;7
   7 #{\a \b \c \d \e \f \g} ;8
   })

(def permutations
  (for [a default-set
        b (disj default-set a)
        c (disj default-set a b)
        d (disj default-set a b c)
        e (disj default-set a b c d)
        f (disj default-set a b c d e)
        g (disj default-set a b c d e f)]
    {a \a
     b \b
     c \c
     d \d
     e \e
     f \f
     g \g}))

(def decode
  {#{\a \b \c \e \f \g}    0
   #{\c \f}                1
   #{\a \c \d \e \g}       2
   #{\a \c \d \f \g}       3
   #{\b \c \d \f}          4
   #{\a \b \d \f \g}       5
   #{\a \b \d \e \f \g}    6
   #{\a \c \f}             7
   #{\a \b \c \d \e \f \g} 8
   #{\a \b \c \d \f \g}    9})

(defn get-numbers [mapping patterns]
  (for [pattern patterns]
    (let [pattern (into #{} (map #(get mapping %) pattern))]
      (get decode pattern))))

(defn translate [mapping output]
  (for [key output]
    (get mapping key)))

(defn solve [[signals outputs]]
  (let [mapping (first (filter #(every? identity (get-numbers % signals)) permutations))
        outputs (map #(into #{} (translate mapping %)) outputs)]
    (reduce (fn [acc x] (+ (* 10 acc)
                           (get decode x)))
            0 outputs)))

;; (solve (first input))
;; (solve [["acedgfb" "cdfbe" "gcdfa" "fbcad" "dab" "cefabd" "cdfgeb" "eafb" "cagedb" "ab"] ["cdfeb" "fcadb" "cdfeb" "cdbaf"]])
;; (solve "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
;; cdfeb fcadb cdfeb cdbaf")

;; Imma brute force it!
(def part-2 (apply + (map solve input)))


;; TODO: this section doesn't quite work yet -- my brain doesn't compute
(let [fives (map set ["cdfbe" "gcdfa" "fbcad"])
      thing (apply set/intersection fives)
      diff  (map #(set/difference % thing) fives)]
  (frequencies (reduce concat diff))
  thing
  )

(defn disjoint [a b]
  (into (set/difference a b) (set/difference b a)))

(defn find-common [a b c]
  (let [a+b (disjoint (set a) (set b))
        b+c (disjoint (set a) (set c))
        a+c (disjoint (set a) (set c))
        is-different (first (sort-by (comp - count) [a+b b+c a+c]))]
    (condp = is-different
      a+b [c [a b]]
      b+c [a [b c]]
      a+c [b [a c]])))

(defn solve-2 [[signals outputs]]
  (let [by-count (group-by count signals)
        answers  {(-> (get by-count 2) first) 1
                  (-> (get by-count 3) first) 7
                  (-> (get by-count 4) first) 4
                  (-> (get by-count 7) first) 8}

        [three two-and-five]  (apply find-common (get by-count 5))
        [nine & zero-and-six] (sort-by #(count (disjoint (set three) (set %))) (get by-count 6))
        [five two]            (sort-by #(count (disjoint (set nine) (set %))) two-and-five)
        [six zero]            (sort-by #(count (disjoint (set five) (set %))) zero-and-six)
        answers               (assoc answers
                                     three                       3
                                     nine                        9
                                     two                         2
                                     five                        5
                                     six                         6
                                     zero                        0)
        answers-set           (into {} (map (fn [[k v]] (vector (set k) v)) answers))]
    (reduce #(+ (* 10 %1) (get answers-set (set %2))) 0 outputs)))

(def part-2-again (map solve-2 input))

(solve-2 [["acedgfb" "cdfbe" "gcdfa" "fbcad" "dab" "cefabd" "cdfgeb" "eafb" "cagedb" "ab"] ["cdfeb" "fcadb" "cdfeb" "cdbaf"]])


;; (defn narrow-by-rules [state pattern]
;;   (let [pat-count (count pattern)]
;;     (if-let [positions (count->positions pat-count)]
;;       (reduce #(update %1 %2 set/intersection positions) state pattern)
;;       state)))

;; (defn reducer [state pattern]
;;   ;; reduce by the set of known points
;;   (let [pat-count   (count pattern)
;;         state       (if-let [positions (count->positions pat-count)]
;;                        (reduce #(update %1 %2 set/intersection positions) state pattern)
;;                        state)
;;         removal-set (for [c pattern]
;;                       [c (apply into #{} (map #(get state %) (disj pattern c)))])]
;;     (reduce (fn [state [c removals]]
;;               (update state c set/intersection removals)) state removal-set)
;;     ))

;; (defn solve [patterns]
;;   (let [patterns (map #(into #{} %) patterns)
;;         state    (reduce narrow-by-rules default-state patterns)

;;         ;; ;; pass one, let's get rid of the two numbers
;;         closed-set (->> (group-by second state)
;;                         (filter (fn [[k v]] (= (count k) (count v))))
;;                         (map (fn [[k v]] [k (into #{} (map first v))])))
;;         state     (reduce (fn [state [set1 values]]
;;                              (map (fn [[k v]] [k (if (contains? values k) v (set/difference v set1))]) state))
;;                            state
;;                            closed-set)
;;         ;; pass two
;;         closed-set (->> (group-by second state)
;;                         (filter (fn [[k v]] (= (count k) (count v))))
;;                         (map (fn [[k v]] [k (into #{} (map first v))])))
;;         state     (reduce (fn [state [set1 values]]
;;                             (map (fn [[k v]] [k (if (contains? values k) v (set/difference v set1))]) state))
;;                           state
;;                           closed-set)
;;         ]
;;     [state closed-set]))

;; (solve ["ab"])
;; (solve ["acedgfb" "cdfbe" "gcdfa" "fbcad" "dab" "cefabd" "cdfgeb" "eafb" "cagedb" "ab"])




;; (defn solve [patterns]
;;   (let [buckets (group-by count patterns)
;;         two     (first (get buckets 2))
;;         three  (first (get buckets 3))
;;         four   (first (get buckets 4))
;;         seven (first (get buckets 7))]

;;     )

;;   (let [[ones threes fours ]])(sort-by count patterns)

;;   )
