
; patterns for matching
(def parse-input-re #"([Q])\s*(\w+)?\s*(\w+)?\s*([0-9-]+)?\s*(\d+)?")
(def parse-search-re #"([S])")

(defn parse-line [line]
  (let [[_ command & args] (re-find parse-input-re line)]
    (prn "Parsing line: " line " => " command args)
    [command args]))

(defn parse-input []
  (loop [conn-data {}
         log []]
    (let [line (read-line)
          [command args] (parse-line line)]
      (case command
        "Q" (prn "Exiting, last state:" conn-data log)
        "C" (assoc conn-data :last line)))))

