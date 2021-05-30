(require '[clojure.string :as str])
(require '[clj-time.core :as clj-time])
(require '[clj-time.format :as clj-timef])

(def date-formatter 
  (clj-timef/formatter "yyyy-MM-dd"))

(defn parse-command-re [line]
  (let [[command & args] (str/split line #"\s+")] ; split on whitespace
    [command args]))

(defn str->int [int-string]
  (Integer/parseInt int-string))

(defn str->date [date-string]
  (->> date-string
       (clj-timef/parse date-formatter)))

(defn args->route-map
  "Parses single line of input into route-map"
  [city-src city-dst date seats-num]
  {:city-src city-src
   :city-dst city-dst
   :date (str->date date)
   :seats (Integer/parseInt seats-num)})

(defn line->routes-db [routes-db line]
  (let [[command args] (parse-command-re line)]
    (case command
      "Q" (prn "Exiting, last state:" routes-db)
      "C" (try
            (conj routes-db (apply args->route-map args))
            (catch Exception e
              (do (prn "Error: Cannot parse args" line "error: " (.getMessage e))
                  routes-db)))
      "S" (prn (str routes-db args))
      "R" (prn "Redo")
     ;default
      (do (prn "Error: Wrong command, please retry.")
          routes-db))))

(defn parse-input []
  (loop [routes-db []
         log []]
    (let [_          (prn "Routes: " routes-db "\nLog: " log)
          _          (prn "Input command:")
          line       (read-line)
          routes-db  (line->routes-db line routes-db)]
      (when routes-db
        (recur routes-db (conj log line))))))


(defn filter-routes
  [routes-db city-src city-dst from-date* to-date seats-min]
  ; todo: this function takes routes-db + arguments as strings - it should take more concrete type
  ; from-date* behaves differently depending on to-date
  (->> routes-db
       (filter (if city-src
                 #(= (:city-src %) city-src)
                 identity))
       (filter (if city-dst
                 #(= (:city-dst %) city-dst)
                 identity))
       (filter (if (and from-date* (nil? to-date))
                 #(clj-time/equal? (:date %) (str->date from-date*))
                 identity))
       (filter (if (and from-date* to-date)
                 #(clj-time/within?
                   (str->date from-date*)
                   (str->date to-date)
                   (:date %))
                 identity))
       (filter (if seats-min
                 #(<= (str->int seats-min) (:seats %))
                 identity))))

(defn route->str [route]
  [(:city-src route) (:city-dst route) (:date route)]
  (str))



(route->str (first test-db))
(def route (first test-db))

(str/join " "
 [(:city-src route)
  (:city-dst route)
  (clj-timef/unparse 
   (clj-timef/formatter "yyyy-MM-dd") 
   (:date route))])

(def test-db
  (-> []
      (line->routes-db "C Gdn Krk 2010-03-04 11")
      (line->routes-db "C Gdn Cpg 2010-03-02 33")))



(def lines
  {:a "C Gdn Cop 2010 20"
   :missing "C Gdn Cop 2010"
   :b "C Gdn Cop 2011 15"})


