(defn deep-merge [& maps]
  ; for merging maps recursively 
  ; (deep-merge {:a {:b 10}} {:a {:d 5}}) => {:a {:b 10 :d 5}}
  (letfn [(m [& xs]
            (if (some #(and (map? %) (not (record? %))) xs)
              (apply merge-with m xs)
              (last xs)))]
    (reduce m maps)))

; patterns for matching
; todo: partial matching, for better help/error control flow
(def re-command #"([C])\s+(\w+)\s+(\w+)\s+([0-9-]+)\s+(\d+)")
(def re-search #"([S])")


(defn parse-line [line]
  (let [[_ command & args] (re-find re-command line)]
    (prn "Parsing line: " line " => " command args)
    [command args]))

(defn command-C [args]
  "Process C command args and return partial map"
  "Example return: {:Gdansk {:Copenhagen {:2010-11-03 10}}}"
  (when (not-any? nil? args); none of the args can be nil
    (let [[city-src city-dst date num-seats] args]
      {(keyword city-src)
       {(keyword city-dst)
        {(keyword date) (Integer/parseInt num-seats)}}}))) ;using bigint as it parses string

(defn parse-input []
  (loop [conn-data {}
         log []]
    (let [line (read-line)
          [command args] (parse-line line)]
      (case command
        "Q" (prn "Exiting, last state:" conn-data log)
        "C" (recur (deep-merge conn-data (command-C args)) (conj log line))))))


(def lines
  {:a "C Gdn Cop 2010 20"
   :missing "C Gdn Cop 2010"
   :b "C Gdn Cop 2011 15"})

(-> (:a lines)
    parse-line
    second ;second is args vector
    command-C)


