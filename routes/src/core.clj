(require '[clojure.string :as str])
(require '[clj-time.core :as clj-time])
(require '[clj-time.format :as clj-timef])

(def date-formatter
  "Date formatter that we use for serializing/deserializing"
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
  "Takes list of arguments and converts them to route-map"
  [[city-src city-dst date seats-num]]
  {:city-src city-src
   :city-dst city-dst
   :date (str->date date)
   :seats (Integer/parseInt seats-num)})­

(defn swap-indexes 
  "Takes vector-able v, two indexes i j, swaps values between indexes"
  [v i j]
  (let [v (vec v)]
    (assoc v j (v i) i (v j))))

(defn add-route [routes-db args]
  (try
    (conj routes-db (args->route-map args))
    (catch Exception e
      (do (prn "Error: Cannot parse args" args "error: " (.getMessage e))
          routes-db))))

(defn last-create-log [log]
  (last (filter #(re-matches #"C.*" %) log)))

(defn route->str [route]
  (str/join " "
   [(:city-src route)
    (:city-dst route)
    (clj-timef/unparse 
     (clj-timef/formatter "yyyy-MM-dd") 
     (:date route))
    (:seats route)]))

(defn filter-routes
  "Filtering routes using positional parameters"
  [routes-db [city-src city-dst from-date* to-date seats-min]]
  ; todo: this function takes routes-db + arguments as list of strings
  ; it should take more concrete structures 
  ; from-date* behaves differently depending on to-date
  ; we are filtering on each param treating nils as skips
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

(defn search-routes [routes-db args]
  (->> [routes-db args]
       (apply filter-routes)
       (map route->str)
       (str/join "\n")))

;todo: print should be taken outside of this function
;in this whole function, we route based on last line from log
;we take routes-db and log and return routes-db or nil
;routes-db is actively modified, log is only for reading
(defn line->routes-db [routes-db log]
  (let [line           (last log) ; todo: verify if string or vec
        [command args] (parse-command-re line)]
    (case command
      "Q" (prn "Exiting, last state:" routes-db)
      "C" (add-route routes-db args)
      "S" (let [output]
            (do
              (println output)
              routes-db))
      ;todo: R, actually executes an effectful command that is not logged/expanded 
      "R" (let  [[_ last-args] (-> log
                                   last-create-log
                                   parse-command-re)
                 last-args-r   (swap-indexes last-args 0 1)] ; we swap city-src and city-dest
            (add-route routes-db last-args-r))
      ;default
      (do (prn "Error: Wrong command, please retry.")
          routes-db))))

(defn parse-input []
  (loop [routes-db []
         log []]
    (let [
          _          (prn "Input command:")
          line       (read-line)
          log*       (conj log line)
          routes-db* (line->routes-db routes-db log*)]
      (when routes-db*
        (recur routes-db* log*)))))




(def lx ["C 21321312 321" "D 2121 " "R 2121" "C G K 2010-30-01 2"])




(route->str (first test-db))
(def route (first test-db))



(def test-db
  (-> []
      (line->routes-db "C Gdn Krk 2010-03-04 11")
      (line->routes-db "C Gdn Cpg 2010-03-02 33")))



(def lines
  {:a "C Gdn Cop 2010 20"
   :missing "C Gdn Cop 2010"
   :b "C Gdn Cop 2011 15"})


