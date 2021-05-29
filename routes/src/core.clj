(def parse-input-re #"([Q])\s*(\w+)?\s*(\w+)?\s*([0-9-]+)?\s*(\d+)?")

(defn parse-input []
  (loop [conn-data {}]
    (let [line (read-line)
          [_ command & rest ] (re-find parse-input-re line)
          _ (prn "t" command rest)]
      (case command
        "Q" "run some"
        "R" "run some code")
            
      (if (= line "Q")
        (prn "Bye!")
        (recur (assoc conn-data :last line))))))
      

