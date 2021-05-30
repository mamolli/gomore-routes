(require '[clojure.string :as str])

(defn deep-merge [& maps]
  ; for merging maps recursively 
  ; (deep-merge {:a {:b 10}} {:a {:d 5}}) => {:a {:b 10 :d 5}}
  (letfn [(m [& xs]
            (if (some #(and (map? %) (not (record? %))) xs)
              (apply merge-with m xs)
              (last xs)))]
    (reduce m maps)))

(defn flatten-map [node]
  (lazy-seq
   (if-not (map? node)
     (list node)
     (mapcat flatten-map node))))

; patterns for matching
; todo: partial matching, for better help/error control flow

(def re-ws-split #"\s+")

(defn parse-command-re [line]
  (let [[command & args] (str/split line #"\s+")] ; split on whitespace
    ;(prn "Parsing line: " line " => " command args)
    [command args]))


(defn command-C [args]
  "Process C command args and return partial map"
  "Example return: {:Gdansk {:Copenhagen {:2010-11-03 10}}}"
  (when (not-any? nil? args); none of the args can be nil
    (let [[city-src city-dst date num-seats] args]
      {city-src
       {city-dst
        {date (Integer/parseInt num-seats)}}}))) ;using bigint as it parses string

(defn command-S
  ([data]
   (get-in data-map args))
  ([data city-src]
   (seq (get data city-src))
   (str city-src)))

(defn parse-input []
  (loop [conn-data {}
         log []]
    (let [_              (prn "Input command:")
          line           (read-line)
          [command args] (parse-command-re line)]
      (case command
        "Q"   (prn "Exiting, last state:" conn-data log)
        "C"   (recur (deep-merge conn-data (command-C args)) (conj log line))
        "S"   (prn (command-S conn-data args))
        ;default
        (do (prn "Error: Wrong command, please retry.")
            (recur conn-data log))))))


(def lines
  {:a "C Gdn Cop 2010 20"
   :missing "C Gdn Cop 2010"
   :b "C Gdn Cop 2011 15"})


(-> (:a lines)
    parse-command-re
    second ;second is args vector
    command-C)

(def data {"gdn" {"cpn" {"2010" 20 "2013" 43}
                  "waw" {"2011" 25}}})

(defn flt [s]
  (mapcat
   #(if (every? coll? %)
        (flt %)
        (list %)) s))

(defn flatten-data
  ([m]
   (flatten-data m []))
  ([m key]
   (if (map? m)
     (for [[k v] m] (flatten-data v (conj key k)))
     (conj key m))))

(flt (flatten-data data []))


(for [[k v] data]
  [k v])

(flatten-data data [])