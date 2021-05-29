
(defn parse-input []
  (loop [conn-data {}]
    (let [line (read-line)
          _ (prn "db" conn-data)]
      (if (= line "Q")
        (prn "Bye!")
        (recur (assoc conn-data :last line))))))
      

