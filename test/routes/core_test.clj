(ns routes.core-test
  (:require [clojure.test :refer :all]
            [routes.core :as r]))


(defn route-test-1 [r]
  (-> r
      (r/line->routes-db ["C Gdansk Copenhagen 2010-03-04 12"])
      (r/line->routes-db ["C Warsaw Copenhagen 2010-03-04 32"])
      (r/search-routes ["Gdansk"]))) ; its easier than mocking console output it means "S Gdansk"

(defn route-test-2 [r]
  (-> r
      (r/line->routes-db ["C Gdansk Copenhagen 20104 12"]) ; testing malformed
      (r/line->routes-db ["C Gdansk Copenhagen 2010-03-04 12"])
      (r/line->routes-db ["C Warsaw Copenhagen 2010-03-04 32"])
      (r/search-routes ["Gdansk"]))) ; its easier than mocking console output it means "S Gdansk"

(defn route-test-3 [r]
  (-> r
      (r/line->routes-db ["C Gdansk Copenhagen 2010-03-04 12"])
      (r/line->routes-db ["C Warsaw Copenhagen 2010-03-04 32"])
      (r/line->routes-db ["C Warsaw Copenhagen 2010-03-04 32"
                          "R"]) ; only the last log line gets executed, 
                                ; but we need "full" log for R
      (r/search-routes ["Copenhagen"]))) 

(deftest line->routes-db-loop
  (is (= (route-test-1 [])
         "Gdansk Copenhagen 2010-03-04 12")) 
  (is (= (route-test-2 [])
         "Gdansk Copenhagen 2010-03-04 12"))
  (is (= (route-test-3 [])
         "Copenhagen Warsaw 2010-03-04 32"))) 
